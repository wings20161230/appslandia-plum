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

import com.appslandia.common.base.PropertiesMap;
import com.appslandia.plum.base.AppConfig;
import com.appslandia.plum.base.ServletUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class MockAppConfig extends PropertiesMap implements AppConfig {
	private static final long serialVersionUID = 1L;

	public MockAppConfig() {
		put(ServletUtils.CONFIG_LOGIN_URL, "/app/login.html");
	}

	@Override
	public int getHttpPort() {
		return getInt(ServletUtils.CONFIG_HTTP_PORT, 8080);
	}

	@Override
	public int getHttpsPort() {
		return getInt(ServletUtils.CONFIG_HTTPS_PORT, 8181);
	}

	public boolean isEnableHttps() {
		return getBool(ServletUtils.CONFIG_ENABLE_HTTPS, false);
	}

	public boolean isProduction() {
		return getBool(ServletUtils.CONFIG_PRODUCTION, false);
	}
}
