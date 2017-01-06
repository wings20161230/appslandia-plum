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

import java.util.regex.Pattern;

import com.appslandia.common.utils.PatternUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class RemoteIpFilter {

	private Pattern[] denies = PatternUtils.EMPTY_PATTERNS;
	private Pattern[] allows = PatternUtils.EMPTY_PATTERNS;

	public RemoteIpFilter setDenies(String ips) {
		this.denies = PatternUtils.compilePatterns(ips);
		return this;
	}

	public RemoteIpFilter setAllows(String ips) {
		this.allows = PatternUtils.compilePatterns(ips);
		return this;
	}

	public boolean filter(String remoteIp) throws Exception {

		// Check the deny patterns, if any
		if (PatternUtils.matches(this.denies, remoteIp)) {
			return false;
		}

		// Check the allow patterns, if any
		if (PatternUtils.matches(this.allows, remoteIp)) {
			return true;
		}

		// Deny if allows specified but not denies
		if ((this.allows.length != 0) && (this.denies.length == 0)) {
			return false;
		}

		// Default allow
		return true;
	}
}
