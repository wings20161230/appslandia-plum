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

import java.util.HashMap;
import java.util.Map;

import com.appslandia.common.utils.AssertUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class CorsPolicyProvider {

	protected final Map<String, CorsPolicy> corsPolicyMap = new HashMap<>();

	public CorsPolicy getCorsPolicy(String name) throws IllegalArgumentException {
		CorsPolicy policy = this.corsPolicyMap.get(name);
		if (policy == null) {
			throw new IllegalArgumentException("corsPolicy is not found (name=" + name + ")");
		}
		return policy;
	}

	protected CorsPolicyProvider addCorsPolicy(String name, CorsPolicy policy) {
		AssertUtils.assertFalse(this.corsPolicyMap.containsKey(name), "corsPolicy is already existed.");
		this.corsPolicyMap.put(name, policy);
		return this;
	}
}
