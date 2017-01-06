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
import com.appslandia.plum.base.ServletUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public abstract class SessionCsrfTokenManager extends CsrfTokenManager {

	public static final int DEFAULT_CACHE_SIZE = 3;

	public int getCacheSize() {
		return DEFAULT_CACHE_SIZE;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void saveCsrfToken(HttpServletRequest request, String csrfToken) throws Exception {
		HttpSession session = request.getSession();
		synchronized (ServletUtils.getMutex(session)) {
			LruCache<String, String> cache = (LruCache<String, String>) session.getAttribute(ServletUtils.SESSION_ATTRIBUTE_CSRF_CACHE);
			if (cache == null) {
				cache = new LruCache<>(getCacheSize());
			}
			cache.put(csrfToken, null);

			// Notify updated
			session.setAttribute(ServletUtils.SESSION_ATTRIBUTE_CSRF_CACHE, cache);
		}
		request.setAttribute(ServletUtils.REQUEST_ATTRIBUTE_CSRF_TOKEN, csrfToken);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected boolean isCsrfTokenValid(HttpServletRequest request, String csrfToken, boolean reuse) throws Exception {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return false;
		}
		synchronized (ServletUtils.getMutex(session)) {
			LruCache<String, String> cache = (LruCache<String, String>) session.getAttribute(ServletUtils.SESSION_ATTRIBUTE_CSRF_CACHE);
			if (cache == null) {
				return false;
			}

			boolean hasToken = cache.contains(csrfToken);
			if (hasToken && (reuse == false)) {
				cache.remove(csrfToken);

				// Notify updated
				session.setAttribute(ServletUtils.SESSION_ATTRIBUTE_CSRF_CACHE, cache);
			}
			return hasToken;
		}
	}
}
