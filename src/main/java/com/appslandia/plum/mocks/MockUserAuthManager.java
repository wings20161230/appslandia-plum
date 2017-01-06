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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.appslandia.common.base.TextGenerator;
import com.appslandia.common.base.UUIDGenerator;
import com.appslandia.common.utils.AssertUtils;
import com.appslandia.plum.base.UserAuth;
import com.appslandia.plum.base.UserAuthManager;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class MockUserAuthManager implements UserAuthManager {

	private ConcurrentMap<String, UserAuth> userAuthMap = new ConcurrentHashMap<>();
	private TextGenerator seriesGenerator = new UUIDGenerator();

	@Override
	public String insertUserAuth(UserAuth userAuth) throws Exception {
		String series = this.seriesGenerator.generate();

		UserAuth obj = userAuth.copy();
		obj.setSeries(series);
		userAuth.setSeries(series);

		this.userAuthMap.put(series, obj);
		return series;
	}

	@Override
	public UserAuth getUserAuth(String series) throws Exception {
		UserAuth obj = this.userAuthMap.get(series);
		return obj != null ? obj.copy() : null;
	}

	@Override
	public void reIssueUserAuth(UserAuth userAuth) throws Exception {
		UserAuth obj = this.userAuthMap.get(userAuth.getSeries());
		AssertUtils.assertNotNull(obj);

		obj.setToken(userAuth.getToken());
		obj.setExpires(userAuth.getExpires());
		obj.setLastModified(userAuth.getLastModified());
	}

	@Override
	public void removeUserAuth(String series) throws Exception {
		this.userAuthMap.remove(series);
	}

	@Override
	public void removeUserAuth(final int userId) throws Exception {
		this.userAuthMap.entrySet().removeIf(e -> e.getValue().getUserId() == userId);
	}
}
