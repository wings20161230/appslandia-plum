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

import com.appslandia.sweetsop.http.HttpClient;
import com.appslandia.sweetsop.http.HttpUrl;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public abstract class FacebookLogin extends OAuthLogin {

	public String getLoginUrl(String state, String redirectUri) {
		return getLoginUrl(state, redirectUri, false, null, false, null);
	}

	@Override
	public String getLoginUrl(String state, String redirectUri, boolean offlineAccess, String prompt, boolean includeGrantedScopes, String loginHint) {
		HttpUrl url = new HttpUrl("https://www.facebook.com/dialog/oauth");
		url.query("client_id", getOAuthConfig().getAppId());
		url.query("redirect_uri", redirectUri);
		url.query("scope", "email", false);
		url.query("response_type", "code", false);
		url.query("state", state);

		return url.toString();
	}

	@Override
	public OAuthAccess getOAuthAccess(String code, String redirectUri) throws IOException {
		HttpUrl url = new HttpUrl("https://graph.facebook.com/v2.3/oauth/access_token");
		url.query("client_id", getOAuthConfig().getAppId());
		setAppSecret("client_secret", url);

		url.query("redirect_uri", redirectUri);
		url.query("code", code);

		HttpClient client = HttpClient.get(url.toString());
		client.setResultReader(getJsonObjectReader()).setResultClass(HashMap.class).execute();
		Map<String, Object> result = client.getResultObject();
		if (result == null) {
			throw new IOException("status=" + client.getResponseCode() + "; error=" + client.getErrorObject());
		}
		String accessToken = (String) result.get("access_token");
		return new OAuthAccess(accessToken, accessToken);
	}

	@Override
	public OAuthUser getOAuthUser(String accessToken) throws IOException {
		HttpUrl url = new HttpUrl("https://graph.facebook.com/me");
		url.query("access_token", accessToken);
		url.query("fields", "id,email,first_name,middle_name,last_name");

		HttpClient client = HttpClient.get(url.toString());
		client.setResultReader(getJsonObjectReader()).setResultClass(HashMap.class).execute();
		Map<String, Object> result = client.getResultObject();
		if (result == null) {
			throw new IOException("status=" + client.getResponseCode() + "; error=" + client.getErrorObject());
		}
		OAuthUser user = new OAuthUser();
		user.setUserId((String) result.get("id"));
		user.setEmail((String) result.get("email"));
		user.setFirstName((String) result.get("first_name"));
		user.setMiddleName((String) result.get("middle_name"));
		user.setLastName((String) result.get("last_name"));
		return user;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isTokenValid(String accessToken) throws IOException {
		HttpUrl url = new HttpUrl("https://graph.facebook.com/debug_token");
		url.query("input_token", accessToken);
		url.query("access_token", getOAuthConfig().getAppId() + "|" + getOAuthConfig().getAppSecret());

		HttpClient client = HttpClient.get(url.toString());
		client.setResultReader(getJsonObjectReader()).setResultClass(HashMap.class).execute();
		Map<String, Object> result = client.getResultObject();
		if (result == null) {
			throw new IOException("status=" + client.getResponseCode() + "; error=" + client.getErrorObject());
		}
		Map<String, Object> dataMap = (Map<String, Object>) result.get("data");
		return dataMap != null && Boolean.TRUE.equals(dataMap.get("is_valid"));
	}

}
