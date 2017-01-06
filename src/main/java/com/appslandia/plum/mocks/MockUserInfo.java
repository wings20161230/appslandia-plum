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

import java.util.Collections;
import java.util.Set;

import com.appslandia.plum.base.UserInfo;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class MockUserInfo implements UserInfo {
	private static final long serialVersionUID = 1L;

	private int userId;
	private String sid;
	private String userName;
	private int authType;
	private int status;

	private Set<String> userRoles = Collections.emptySet();

	@Override
	public int getUserId() {
		return this.userId;
	}

	public MockUserInfo setUserId(int userId) {
		this.userId = userId;
		return this;
	}

	@Override
	public String getSid() {
		return this.sid;
	}

	public MockUserInfo setSid(String sid) {
		this.sid = sid;
		return this;
	}

	@Override
	public String getUserName() {
		return this.userName;
	}

	public MockUserInfo setUserName(String userName) {
		this.userName = userName;
		return this;
	}

	@Override
	public String getDisplayName() {
		return this.userName;
	}

	@Override
	public int getAuthType() {
		return this.authType;
	}

	public MockUserInfo setAuthType(int authType) {
		this.authType = authType;
		return this;
	}

	@Override
	public int getStatus() {
		return this.status;
	}

	public MockUserInfo setStatus(int status) {
		this.status = status;
		return this;
	}

	@Override
	public Set<String> getUserRoles() {
		return this.userRoles;
	}

	public MockUserInfo setUserRoles(Set<String> userRoles) {
		this.userRoles = userRoles;
		return this;
	}
}
