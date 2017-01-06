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

package com.appslandia.plum.oauth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.appslandia.sweetsop.bodys.FormBody;
import com.appslandia.sweetsop.http.HttpClient;
import com.appslandia.sweetsop.http.HttpUrl;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public abstract class GoogleLogin extends OAuthLogin {

	public String getLoginUrl(String state, String redirectUri) {
		return getLoginUrl(state, redirectUri, true, null, true, null);
	}

	@Override
	public String getLoginUrl(String state, String redirectUri, boolean offlineAccess, String prompt, boolean includeGrantedScopes, String loginHint) {
		HttpUrl url = new HttpUrl("https://accounts.google.com/o/oauth2/v2/auth");
		url.query("client_id", getOAuthConfig().getAppId());
		url.query("redirect_uri", redirectUri);
		url.query("scope", "email+profile", false);
		url.query("response_type", "code", false);
		url.query("state", state);

		if (offlineAccess) {
			url.query("access_type", "offline", false);
		}
		if (prompt != null) {
			url.query("prompt", prompt, false);
		}
		if (includeGrantedScopes) {
			url.query("include_granted_scopes", "true", false);
		}
		if (loginHint != null) {
			url.query("login_hint", loginHint);
		}
		return url.toString();
	}

	@Override
	public OAuthAccess getOAuthAccess(String code, String redirectUri) throws IOException {
		HttpClient client = HttpClient.post("https://www.googleapis.com/oauth2/v4/token");

		FormBody body = new FormBody();
		body.param("client_id", getOAuthConfig().getAppId());
		setAppSecret("client_secret", body);

		body.param("grant_type", "authorization_code", false);
		body.param("redirect_uri", redirectUri);
		body.param("code", code);
		client.setRequestBody(body);

		client.setResultReader(getJsonObjectReader()).setResultClass(HashMap.class).execute();
		Map<String, Object> result = client.getResultObject();
		if (result == null) {
			throw new IOException("status=" + client.getResponseCode() + "; error=" + client.getErrorObject());
		}

		String accessToken = (String) result.get("access_token");
		String refreshToken = (String) result.get("refresh_token");
		return new OAuthAccess(accessToken, refreshToken);
	}

	@Override
	public OAuthUser getOAuthUser(String accessToken) throws IOException {
		HttpUrl url = new HttpUrl("https://www.googleapis.com/oauth2/v3/userinfo");
		url.query("access_token", accessToken);

		HttpClient client = HttpClient.get(url.toString());
		client.setResultReader(getJsonObjectReader()).setResultClass(HashMap.class).execute();
		Map<String, Object> result = client.getResultObject();
		if (result == null) {
			throw new IOException("status=" + client.getResponseCode() + "; error=" + client.getErrorObject());
		}

		OAuthUser user = new OAuthUser();
		user.setUserId((String) result.get("sub"));
		user.setEmail((String) result.get("email"));
		user.setFirstName((String) result.get("given_name"));
		user.setMiddleName((String) result.get("middle_name"));
		user.setLastName((String) result.get("family_name"));
		return user;
	}

	@Override
	public boolean isTokenValid(String refreshToken) throws IOException {
		HttpClient client = HttpClient.post("https://www.googleapis.com/oauth2/v4/token");

		FormBody body = new FormBody();
		body.param("client_id", getOAuthConfig().getAppId());
		setAppSecret("client_secret", body);

		body.param("grant_type", "refresh_token", false);
		body.param("refresh_token", refreshToken);
		client.setRequestBody(body);

		client.setResultReader(getJsonObjectReader()).setResultClass(HashMap.class).execute();
		Map<String, Object> result = client.getResultObject();
		if (result == null) {
			throw new IOException("status=" + client.getResponseCode() + "; error=" + client.getErrorObject());
		}
		return result.get("access_token") != null;
	}
}
