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
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.appslandia.common.base.TextGenerator;
import com.appslandia.common.crypto.TextDigester;
import com.appslandia.common.utils.AssertUtils;
import com.appslandia.plum.base.Authenticator;
import com.appslandia.plum.base.ServletUtils;
import com.appslandia.plum.base.UserAuth;
import com.appslandia.plum.base.UserAuthManager;
import com.appslandia.plum.base.UserData;
import com.appslandia.plum.base.UserInfo;
import com.appslandia.plum.base.UserInfoManager;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public abstract class AuthTokenAuthenticator implements Authenticator {

	public abstract UserAuthManager getUserAuthManager();

	public abstract AuthTokenHandler getAuthTokenHandler();

	public abstract UserInfoManager getUserInfoManager();

	public abstract TokenTheftHandler getTokenTheftHandler();

	public abstract PrefCookieHandler getPrefCookieHandler();

	public abstract TextGenerator getTokenGenerator();

	public abstract TextDigester getTokenDigester();

	@Override
	public UserData authenticate(HttpServletRequest request, HttpServletResponse response) throws Exception {
		final HttpSession session = request.getSession(false);
		UserData userData = null;

		// Authenticated?
		if (session != null) {
			if ((userData = (UserData) session.getAttribute(ServletUtils.ATTRIBUTE_USER_DATA)) != null) {
				return userData;
			}
		}
		final long nowMillis = System.currentTimeMillis();

		// AuthToken
		AuthToken authToken = getAuthTokenHandler().parseToken(request, response);
		if ((authToken == AuthToken.EMPTY) || (authToken.getExpires() <= nowMillis)) {
			return null;
		}

		// session == null
		if (session == null) {
			UserInfo userInfo = doAuthenticate(request, response, authToken, nowMillis);
			if (userInfo != null) {
				userData = createUserData(userInfo);
				request.getSession().setAttribute(ServletUtils.ATTRIBUTE_USER_DATA, userData);
				return userData;
			} else {
				getAuthTokenHandler().removeToken(response);
				return null;
			}
		}

		// session != null
		synchronized (ServletUtils.getMutex(session)) {
			if ((userData = (UserData) session.getAttribute(ServletUtils.ATTRIBUTE_USER_DATA)) != null) {
				return userData;
			}
			UserInfo userInfo = doAuthenticate(request, response, authToken, nowMillis);
			if (userInfo != null) {
				userData = createUserData(userInfo);
				session.setAttribute(ServletUtils.ATTRIBUTE_USER_DATA, userData);

				request.changeSessionId();
				return userData;
			} else {
				getAuthTokenHandler().removeToken(response);
				return null;
			}
		}
	}

	protected UserInfo doAuthenticate(HttpServletRequest request, HttpServletResponse response, AuthToken authToken, long nowMillis) throws Exception {
		// userAuth
		UserAuth userAuth = this.getUserAuthManager().getUserAuth(authToken.getSeries());
		if (userAuth == null) {
			return null;
		}

		// Token matched?
		if (getTokenDigester().verify(authToken.getToken(), userAuth.getToken()) == false) {
			getTokenTheftHandler().handleTheft(request, userAuth.getUserId());
			return null;
		}

		// Expired?
		if (userAuth.getExpires() <= nowMillis) {
			return null;
		}

		// userInfo
		UserInfo userInfo = getUserInfoManager().getUserById(userAuth.getUserId());
		AssertUtils.assertNotNull(userInfo);

		// Locked?
		if (getUserInfoManager().isUserLocked(userInfo.getStatus())) {
			return null;
		}

		// reIssueToken
		reIssueToken(request, response, userAuth, authToken, nowMillis);

		// savePrefCookie
		getPrefCookieHandler().savePrefCookie(request, response, userInfo);
		return userInfo;
	}

	protected void reIssueToken(HttpServletRequest request, HttpServletResponse response, UserAuth curUserAuth, AuthToken curAuthToken, long nowMillis) throws Exception {
		// userAuth
		UserAuth userAuth = curUserAuth.copy();
		String newToken = getTokenGenerator().generate();
		userAuth.setToken(getTokenDigester().digest(newToken)).setLastModified(nowMillis);
		getUserAuthManager().reIssueUserAuth(userAuth);

		// authToken
		AuthToken authToken = curAuthToken.copy();
		authToken.setToken(newToken);

		int curAge = (int) ((curUserAuth.getExpires() - nowMillis) / 1000L);
		getAuthTokenHandler().saveToken(response, authToken, curAge);
	}

	@Override
	public Object onUserLogin(HttpServletRequest request, HttpServletResponse response, UserInfo userInfo) throws Exception {
		final long nowMillis = System.currentTimeMillis();

		// UserAuth
		UserAuth userAuth = new UserAuth();
		String token = getTokenGenerator().generate();
		userAuth.setToken(getTokenDigester().digest(token)).setLastModified(nowMillis);
		userAuth.setUserId(userInfo.getUserId()).setRemoteIp(ServletUtils.getRemoteIp(request));

		int maxAge = getAuthTokenHandler().getTokenAge();
		userAuth.setExpires(nowMillis + maxAge * 1000L);
		String series = getUserAuthManager().insertUserAuth(userAuth);

		// AuthToken
		AuthToken authToken = new AuthToken();
		authToken.setSeries(series).setToken(token);
		authToken.setExpires(userAuth.getExpires());
		getAuthTokenHandler().saveToken(response, authToken, maxAge);
		return series;
	}

	@Override
	public void onUserLogout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		getAuthTokenHandler().removeToken(response);

		AuthToken authToken = this.getAuthTokenHandler().parseToken(request, response);
		if (authToken != AuthToken.EMPTY) {
			getUserAuthManager().removeUserAuth(authToken.getSeries());
		}
	}
}
