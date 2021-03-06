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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.appslandia.plum.base.Authenticator;
import com.appslandia.plum.base.ServletUtils;
import com.appslandia.plum.base.UserData;
import com.appslandia.plum.base.UserInfo;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class MockWebApiAuthenticator implements Authenticator {

	@Override
	public UserData authenticate(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return (UserData) request.getAttribute(ServletUtils.ATTRIBUTE_USER_DATA);
	}

	@Override
	public UserData createUserData(UserInfo userInfo) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object onUserLogin(HttpServletRequest request, HttpServletResponse response, UserInfo userInfo) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public void onUserLogout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		throw new UnsupportedOperationException();
	}
}
