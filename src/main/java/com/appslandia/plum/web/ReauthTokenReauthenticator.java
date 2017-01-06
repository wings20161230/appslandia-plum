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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.appslandia.common.base.ParameterMap;
import com.appslandia.common.base.TextGenerator;
import com.appslandia.common.crypto.TextDigester;
import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.DateUtils;
import com.appslandia.common.utils.StringUtils;
import com.appslandia.plum.base.AppConfig;
import com.appslandia.plum.base.AuthTypes;
import com.appslandia.plum.base.ServletUtils;
import com.appslandia.plum.base.UserData;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public abstract class ReauthTokenReauthenticator implements Reauthenticator {

	public static final int DEFAULT_VER_CODE_AGE = (int) TimeUnit.SECONDS.convert(5, TimeUnit.MINUTES);

	public abstract AppConfig getAppConfig();

	public abstract UserReauthManager getUserReauthManager();

	public abstract ReauthTokenHandler getReauthTokenHandler();

	public abstract TextGenerator getVerCodeGenerator();

	public abstract TextGenerator getTokenGenerator();

	public abstract TextDigester getTokenDigester();

	public abstract TextDigester getSesTokenGenerator();

	public int getVerCodeAge() {
		return DEFAULT_VER_CODE_AGE;
	}

	protected String buildSessionInfo(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		AssertUtils.assertNotNull(session);
		return ServletUtils.getRemoteIp(request) + "|" + session.getId();
	}

	@Override
	public void askReauth(HttpServletRequest request, HttpServletResponse response, UserData userData) throws Exception {
		String token = getTokenGenerator().generate();

		// userReauth
		UserReauth userReauth = new UserReauth();
		userReauth.setToken(getTokenDigester().digest(token));
		userReauth.setUserId(userData.getUserId());
		userReauth.setSesToken(getSesTokenGenerator().digest(buildSessionInfo(request)));
		userReauth.setExpires(System.currentTimeMillis() + DateUtils.__1HR_MS);

		String series = getUserReauthManager().insertUserReauth(userReauth);
		ParameterMap params = new ParameterMap().of(ServletUtils.PARAM_REAUTH_SERIES, series).of(ServletUtils.PARAM_REAUTH_TOKEN, token);

		if (userData.getAuthType() == AuthTypes.APP) {
			String loginUrl = getAppConfig().getRequiredString(ServletUtils.CONFIG_LOGIN_URL);
			response.sendRedirect(response.encodeRedirectURL(ServletUtils.getRedirectUrl(request, loginUrl, params)));
		} else {
			String reauthUrl = getAppConfig().getRequiredString(ServletUtils.CONFIG_REAUTH_URL);
			response.sendRedirect(response.encodeRedirectURL(ServletUtils.getRedirectUrl(request, reauthUrl, params)));
		}
	}

	@Override
	public UserReauth verifyReauth(HttpServletRequest request, UserData userData, String verCode) throws Exception {
		String series = StringUtils.trimToNull(request.getParameter(ServletUtils.PARAM_REAUTH_SERIES));
		String token = StringUtils.trimToNull(request.getParameter(ServletUtils.PARAM_REAUTH_TOKEN));
		if ((series == null) || (token == null)) {
			return null;
		}
		// userReauth
		UserReauth userReauth = getUserReauthManager().getUserReauth(series);
		if ((userReauth == null) || (userReauth.getExpires() <= System.currentTimeMillis())) {
			return null;
		}

		// verCode?
		if (verCode != null) {
			if (verCode.equals(userReauth.getVerCode()) == false) {
				return null;
			}
		}

		// Token matched?
		if (getTokenDigester().verify(token, userReauth.getToken()) == false) {
			return null;
		}

		// Others
		if (userReauth.getUserId() != userData.getUserId()) {
			return null;
		}
		if (getSesTokenGenerator().verify(buildSessionInfo(request), userReauth.getSesToken()) == false) {
			return null;
		}
		if (userReauth.getStatus() == UserReauth.STATUS_REAUTHED) {
			return null;
		}
		return userReauth;
	}

	@Override
	public String issueVerCode(UserReauth userReauth) throws Exception {
		String verCode = getVerCodeGenerator().generate();
		long expires = System.currentTimeMillis() + getVerCodeAge() * 1000;
		userReauth.setVerCode(verCode).setExpires(expires);
		userReauth.setStatus(UserReauth.STATUS_VERIFYING);

		getUserReauthManager().issueVerCode(userReauth);
		return verCode;
	}

	@Override
	public void issueReauthed(HttpServletRequest request, HttpServletResponse response, UserReauth userReauth) throws Exception {
		// userReauth
		int maxAge = getReauthTokenHandler().getTokenAge();
		long expires = System.currentTimeMillis() + maxAge * 1000;
		userReauth.setSesToken(getSesTokenGenerator().digest(buildSessionInfo(request)));
		userReauth.setExpires(expires);
		userReauth.setStatus(UserReauth.STATUS_REAUTHED);

		getUserReauthManager().issueReauthed(userReauth);

		String token = StringUtils.trimToNull(request.getParameter(ServletUtils.PARAM_REAUTH_TOKEN));
		AssertUtils.assertNotNull(token);

		// reauthToken
		ReauthToken reauthToken = new ReauthToken();
		reauthToken.setSeries(userReauth.getSeries()).setToken(token).setExpires(expires);
		getReauthTokenHandler().saveToken(response, reauthToken, maxAge);
	}

	@Override
	public boolean isReauthed(HttpServletRequest request, HttpServletResponse response, UserData userData) throws Exception {
		final long nowMillis = System.currentTimeMillis();

		// reauthToken
		ReauthToken reauthToken = getReauthTokenHandler().parseToken(request, response);
		if ((reauthToken == ReauthToken.EMPTY) || (reauthToken.getExpires() <= nowMillis)) {
			return false;
		}

		// userReauth
		UserReauth userReauth = this.getUserReauthManager().getUserReauth(reauthToken.getSeries());
		if ((userReauth == null) || (userReauth.getExpires() <= nowMillis)) {
			return false;
		}

		// Token matched?
		if (getTokenDigester().verify(reauthToken.getToken(), userReauth.getToken()) == false) {
			return false;
		}

		// Others
		if (userReauth.getUserId() != userData.getUserId()) {
			return false;
		}
		if (getSesTokenGenerator().verify(buildSessionInfo(request), userReauth.getSesToken()) == false) {
			return false;
		}
		if (userReauth.getStatus() != UserReauth.STATUS_REAUTHED) {
			return false;
		}
		return true;
	}

	@Override
	public void onUserLogout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		getReauthTokenHandler().removeToken(response);

		ReauthToken reauthToken = getReauthTokenHandler().parseToken(request, response);
		if (reauthToken != ReauthToken.EMPTY) {
			getUserReauthManager().removeReauthed(reauthToken.getSeries());
		}
	}
}
