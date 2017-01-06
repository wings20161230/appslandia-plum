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

import com.appslandia.common.crypto.TextEncryptor;
import com.appslandia.plum.base.ServletUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public abstract class ReauthTokenHandler {

	public static final String DEFAULT_TOKEN_NAME = "__reauth";
	public static final int DEFAULT_TOKEN_AGE = (int) TimeUnit.SECONDS.convert(15, TimeUnit.MINUTES);

	public abstract SessionConfig getSessionConfig();

	public abstract TextEncryptor getTokenSigner();

	public String getTokenName() {
		return DEFAULT_TOKEN_NAME;
	}

	public int getTokenAge() {
		return DEFAULT_TOKEN_AGE;
	}

	protected String getTokenString(HttpServletRequest request) {
		return ServletUtils.getCookieValue(request, this.getTokenName());
	}

	public ReauthToken parseToken(HttpServletRequest request, HttpServletResponse response) {
		String tokenString = getTokenString(request);
		if (tokenString == null) {
			return ReauthToken.EMPTY;
		}
		try {
			tokenString = getTokenSigner().decrypt(tokenString);
			ReauthToken token = new ReauthToken().parse(tokenString);

			if ((token == null) || (token.getSeries() == null) || (token.getToken() == null)) {
				return ReauthToken.EMPTY;
			}
			return token;
		} catch (Exception ex) {
			return ReauthToken.EMPTY;
		}
	}

	public void saveToken(HttpServletResponse response, ReauthToken token, int maxAge) throws Exception {
		Cookie cookie = new Cookie(this.getTokenName(), getTokenSigner().encrypt(token.toString()));
		cookie.setMaxAge(maxAge);

		if (getSessionConfig().getCookieDomain() != null) {
			cookie.setDomain(getSessionConfig().getCookieDomain());
		}
		cookie.setPath(getSessionConfig().getCookiePath());

		cookie.setHttpOnly(getSessionConfig().isCookieHttpOnly());
		cookie.setSecure(getSessionConfig().isCookieSecure());
		response.addCookie(cookie);
	}

	public void removeToken(HttpServletResponse response) {
		ServletUtils.removeCookie(response, this.getTokenName(), getSessionConfig().getCookieDomain(), getSessionConfig().getCookiePath());
	}
}
