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

package com.appslandia.plum.mocks;

import javax.inject.Inject;

import com.appslandia.common.base.TextGenerator;
import com.appslandia.common.base.UrlSafeTextGenerator;
import com.appslandia.common.crypto.SaltedTextDigester;
import com.appslandia.common.crypto.TextDigester;
import com.appslandia.plum.base.UserAuthManager;
import com.appslandia.plum.base.UserData;
import com.appslandia.plum.base.UserInfo;
import com.appslandia.plum.base.UserInfoManager;
import com.appslandia.plum.web.AuthTokenAuthenticator;
import com.appslandia.plum.web.AuthTokenHandler;
import com.appslandia.plum.web.PrefCookieHandler;
import com.appslandia.plum.web.TokenTheftHandler;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class MockAuthTokenAuthenticator extends AuthTokenAuthenticator {

	@Inject
	private UserAuthManager userAuthManager;

	@Inject
	private AuthTokenHandler authTokenHandler;

	@Inject
	private UserInfoManager userInfoManager;

	@Inject
	private TokenTheftHandler tokenTheftHandler;

	@Inject
	private PrefCookieHandler prefCookieHandler;

	final TextGenerator tokenGenerator = new UrlSafeTextGenerator();

	final TextDigester tokenDigester = new SaltedTextDigester();

	@Override
	public UserAuthManager getUserAuthManager() {
		return this.userAuthManager;
	}

	@Override
	public AuthTokenHandler getAuthTokenHandler() {
		return this.authTokenHandler;
	}

	@Override
	public UserInfoManager getUserInfoManager() {
		return this.userInfoManager;
	}

	@Override
	public PrefCookieHandler getPrefCookieHandler() {
		return this.prefCookieHandler;
	}

	@Override
	public TokenTheftHandler getTokenTheftHandler() {
		return this.tokenTheftHandler;
	}

	@Override
	public TextGenerator getTokenGenerator() {
		return this.tokenGenerator;
	}

	@Override
	public TextDigester getTokenDigester() {
		return this.tokenDigester;
	}

	@Override
	public UserData createUserData(UserInfo userInfo) {
		return new MockUserData(userInfo.getUserId(), userInfo.getAuthType(), userInfo.getUserRoles());
	}
}
