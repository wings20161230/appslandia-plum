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

import java.io.Serializable;

import com.appslandia.common.utils.AssertUtils;
import com.appslandia.plum.base.AuthTypes;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class MockUser implements Serializable {
	private static final long serialVersionUID = 1L;

	private int userId;
	private String sid;
	private String userName;
	private String password;
	private String[] userRoles = {};
	private int status;
	private int authType = AuthTypes.APP;

	public int getUserId() {
		AssertUtils.assertTrue(this.userId > 0, "userId is required.");
		return this.userId;
	}

	public MockUser setUserId(int userId) {
		this.userId = userId;
		return this;
	}

	public String getSid() {
		AssertUtils.assertNotNull(this.sid, "sid is required.");
		return this.sid;
	}

	public MockUser setSid(String sid) {
		this.sid = sid;
		return this;
	}

	public String getUserName() {
		AssertUtils.assertNotNull(this.userName, "userName is required.");
		return this.userName;
	}

	public MockUser setUserName(String userName) {
		this.userName = userName;
		return this;
	}

	public String getPassword() {
		AssertUtils.assertNotNull(this.password, "password is required.");
		return this.password;
	}

	public MockUser setPassword(String password) {
		this.password = password;
		return this;
	}

	public String[] getUserRoles() {
		AssertUtils.assertNotNull(this.userRoles, "userRoles is required.");
		return this.userRoles;
	}

	public MockUser setUserRoles(String... userRoles) {
		this.userRoles = userRoles;
		return this;
	}

	public int getStatus() {
		return this.status;
	}

	public MockUser setStatus(int status) {
		this.status = status;
		return this;
	}

	public int getAuthType() {
		AssertUtils.assertTrue(this.authType > 0, "authType is required.");
		return this.authType;
	}

	public MockUser setAuthType(int authType) {
		this.authType = authType;
		return this;
	}
}
