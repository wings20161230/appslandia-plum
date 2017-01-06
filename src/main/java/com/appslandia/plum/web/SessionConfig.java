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

import javax.servlet.ServletContext;

import com.appslandia.common.base.InitializeObject;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public abstract class SessionConfig extends InitializeObject {

	public abstract ServletContext getServletContext();

	protected String path;

	@Override
	protected void init() throws Exception {
		this.path = this.getServletContext().getSessionCookieConfig().getPath();
		if (this.path == null) {
			this.path = this.getServletContext().getContextPath();
			if (this.path.isEmpty()) {
				this.path = "/";
			}
		}
	}

	public String getCookieDomain() {
		initialize();
		return this.getServletContext().getSessionCookieConfig().getDomain();
	}

	public String getCookiePath() {
		initialize();
		return this.path;
	}

	public boolean isCookieSecure() {
		initialize();
		return this.getServletContext().getSessionCookieConfig().isSecure();
	}

	public boolean isCookieHttpOnly() {
		initialize();
		return this.getServletContext().getSessionCookieConfig().isHttpOnly();
	}
}
