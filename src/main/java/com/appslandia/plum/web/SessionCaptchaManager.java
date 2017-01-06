// The MIT License (MIT)
// Copyright © 2015 AppsLandia. All rights reserved.

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

package com.appslandia.plum.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.appslandia.common.base.LruCache;
import com.appslandia.common.base.TextGenerator;
import com.appslandia.common.utils.StringUtils;
import com.appslandia.plum.base.ServletUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public abstract class SessionCaptchaManager implements CaptchaManager {

	public static final int DEFAULT_CACHE_SIZE = 3;

	public abstract TextGenerator getCaptchaIdGenerator();

	public abstract TextGenerator getWordsGenerator();

	public int getCacheSize() {
		return DEFAULT_CACHE_SIZE;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initCaptcha(HttpServletRequest request) throws Exception {
		String captchaId = getCaptchaIdGenerator().generate();
		String captchaWords = getWordsGenerator().generate();

		HttpSession session = request.getSession();
		synchronized (ServletUtils.getMutex(session)) {
			LruCache<String, String> cache = (LruCache<String, String>) session.getAttribute(ServletUtils.SESSION_ATTRIBUTE_CAPTCHA_CACHE);
			if (cache == null) {
				cache = new LruCache<>(getCacheSize());
			}
			cache.put(captchaId, captchaWords);

			// Notify updated
			session.setAttribute(ServletUtils.SESSION_ATTRIBUTE_CAPTCHA_CACHE, cache);
		}
		request.setAttribute(ServletUtils.REQUEST_ATTRIBUTE_CAPTCHA_ID, captchaId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getCaptchaWords(HttpServletRequest request, String captchaId) throws Exception {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return null;
		}
		synchronized (ServletUtils.getMutex(session)) {
			LruCache<String, String> cache = (LruCache<String, String>) session.getAttribute(ServletUtils.SESSION_ATTRIBUTE_CAPTCHA_CACHE);
			if (cache == null) {
				return null;
			}
			return cache.get(captchaId);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isCaptchaValid(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return false;
		}
		synchronized (ServletUtils.getMutex(session)) {
			LruCache<String, String> cache = (LruCache<String, String>) session.getAttribute(ServletUtils.SESSION_ATTRIBUTE_CAPTCHA_CACHE);
			if (cache == null) {
				return false;
			}
			String captchaId = StringUtils.trimToNull(request.getParameter(ServletUtils.PARAM_CAPTCHA_ID));
			if (captchaId == null) {
				return false;
			}

			String captchaWords = cache.remove(captchaId);
			if (captchaWords == null) {
				return false;
			}

			// Notify updated
			session.setAttribute(ServletUtils.SESSION_ATTRIBUTE_CAPTCHA_CACHE, cache);

			String userCaptchaWords = StringUtils.trimToNull(request.getParameter(ServletUtils.PARAM_CAPTCHA_WORDS));
			if (userCaptchaWords == null) {
				return false;
			}
			return userCaptchaWords.equalsIgnoreCase(captchaWords);
		}
	}
}
