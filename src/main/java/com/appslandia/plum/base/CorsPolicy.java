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

package com.appslandia.plum.base;

import com.appslandia.common.utils.StringUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class CorsPolicy {

	public static final String POLICY_DEFAULT = "default";

	private String[] allowDomains = StringUtils.EMPTY_ARRAY;
	private String allowHeaders;
	private String exposeHeaders;

	private boolean allowCredentials;
	private int maxAge;

	public String[] getAllowDomains() {
		return this.allowDomains;
	}

	public CorsPolicy setAllowDomains(String... allowDomains) {
		this.allowDomains = allowDomains;
		return this;
	}

	public String getAllowHeaders() {
		return this.allowHeaders;
	}

	public CorsPolicy setAllowHeaders(String allowHeaders) {
		this.allowHeaders = allowHeaders;
		return this;
	}

	public String getExposeHeaders() {
		return this.exposeHeaders;
	}

	public CorsPolicy setExposeHeaders(String exposeHeaders) {
		this.exposeHeaders = exposeHeaders;
		return this;
	}

	public boolean isAllowCredentials() {
		return this.allowCredentials;
	}

	public CorsPolicy setAllowCredentials(boolean allowCredentials) {
		this.allowCredentials = allowCredentials;
		return this;
	}

	public int getMaxAge() {
		return this.maxAge;
	}

	public CorsPolicy setMaxAge(int maxAge) {
		this.maxAge = maxAge;
		return this;
	}
}
