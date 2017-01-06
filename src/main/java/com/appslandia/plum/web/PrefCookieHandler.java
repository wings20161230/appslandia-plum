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

import java.util.concurrent.TimeUnit;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.appslandia.common.utils.BaseEncodingUtils;
import com.appslandia.plum.base.ServletUtils;
import com.appslandia.plum.base.UserInfo;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public abstract class PrefCookieHandler {

	public static final String DEFAULT_COOKIE_NAME = "__pref";
	public static final int DEFAULT_COOKIE_MAX_AGE = (int) TimeUnit.SECONDS.convert(90, TimeUnit.DAYS);

	public abstract SessionConfig getSessionConfig();

	public String getCookieName() {
		return DEFAULT_COOKIE_NAME;
	}

	public int getCookieMaxAge() {
		return DEFAULT_COOKIE_MAX_AGE;
	}

	public PrefCookie parsePrefCookie(HttpServletRequest request, HttpServletResponse response) {
		String cookieValue = ServletUtils.getCookieValue(request, this.getCookieName());
		if (cookieValue == null) {
			return PrefCookie.EMPTY;
		}

		String prefString = null;
		try {
			prefString = BaseEncodingUtils.decodeBase64(cookieValue);
		} catch (Exception ex) {
			return PrefCookie.EMPTY;
		}

		// PrefCookie
		PrefCookie prefCookie = new PrefCookie();
		prefCookie.parse(prefString);
		return prefCookie;
	}

	public void savePrefCookie(HttpServletResponse response, PrefCookie prefCookie) {
		String prefString = prefCookie.toString();
		if (prefString.isEmpty()) {
			return;
		}
		String cookieValue = BaseEncodingUtils.encodeBase64(prefString);

		// Cookie
		Cookie cookie = new Cookie(this.getCookieName(), cookieValue);
		cookie.setMaxAge(this.getCookieMaxAge());

		if (this.getSessionConfig().getCookieDomain() != null) {
			cookie.setDomain(this.getSessionConfig().getCookieDomain());
		}
		cookie.setPath(this.getSessionConfig().getCookiePath());
		response.addCookie(cookie);
	}

	public void removePrefCookie(HttpServletResponse response) {
		ServletUtils.removeCookie(response, this.getCookieName(), this.getSessionConfig().getCookieDomain(), this.getSessionConfig().getCookiePath());
	}

	public void savePrefCookie(HttpServletRequest request, HttpServletResponse response, UserInfo userInfo) {
		PrefCookie prefCookie = (PrefCookie) request.getAttribute(ServletUtils.REQUEST_ATTRIBUTE_PREF_COOKIE);
		PrefCookie copy = prefCookie.copy();

		copy.setDisplayName(userInfo.getDisplayName());
		request.setAttribute(ServletUtils.REQUEST_ATTRIBUTE_PREF_COOKIE, copy);
		savePrefCookie(response, copy);
	}

	public void savePrefCookie(HttpServletRequest request, HttpServletResponse response, String language) {
		PrefCookie prefCookie = (PrefCookie) request.getAttribute(ServletUtils.REQUEST_ATTRIBUTE_PREF_COOKIE);
		PrefCookie copy = prefCookie.copy();

		copy.setLanguage(language);
		request.setAttribute(ServletUtils.REQUEST_ATTRIBUTE_PREF_COOKIE, copy);
		savePrefCookie(response, copy);
	}
}
