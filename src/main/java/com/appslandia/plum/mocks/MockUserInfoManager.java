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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.appslandia.common.utils.CollectionUtils;
import com.appslandia.plum.base.UserInfo;
import com.appslandia.plum.base.UserInfoManager;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class MockUserInfoManager implements UserInfoManager {

	final Map<Integer, MockUser> mockUserMap = new HashMap<>();
	final AtomicInteger idSeq = new AtomicInteger(0);

	public void addMockUser(MockUser user) {
		user.setUserId(this.idSeq.incrementAndGet());
		user.setSid(Integer.toString(user.getUserId()));
		this.mockUserMap.put(user.getUserId(), user);
	}

	public MockUser findMockUser(String userName) {
		for (MockUser user : this.mockUserMap.values()) {
			if (user.getUserName().equalsIgnoreCase(userName)) {
				return user;
			}
		}
		return null;
	}

	@Override
	public boolean validateUser(String userName, String password) throws Exception {
		MockUser user = this.findMockUser(userName);
		if (user == null) {
			return false;
		}
		return password.equals(user.getPassword());
	}

	@Override
	public UserInfo getUserByName(String userName) throws Exception {
		MockUser user = this.findMockUser(userName);
		if (user == null) {
			return null;
		}
		return createUserInfo(user);
	}

	@Override
	public UserInfo getUserById(int userId) throws Exception {
		MockUser user = this.mockUserMap.get(userId);
		if (user == null) {
			return null;
		}
		return createUserInfo(user);
	}

	private MockUserInfo createUserInfo(MockUser user) {
		MockUserInfo userInfo = new MockUserInfo();

		userInfo.setUserId(user.getUserId());
		userInfo.setSid(user.getSid());
		userInfo.setUserName(user.getUserName());
		userInfo.setAuthType(user.getAuthType());
		userInfo.setStatus(user.getStatus());

		userInfo.setUserRoles(CollectionUtils.unmodifiableSet(user.getUserRoles()));
		return userInfo;
	}

	@Override
	public boolean isUserLocked(int status) {
		return false;
	}
}
