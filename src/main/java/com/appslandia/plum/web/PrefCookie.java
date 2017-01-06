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

import java.util.HashMap;

import com.appslandia.common.base.QueryParams;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class PrefCookie extends QueryParams {
	private static final long serialVersionUID = 1L;

	public static final String PARAM_LANGUAGE = "language";
	public static final String PARAM_DISPLAY_NAME = "displayName";

	public static final PrefCookie EMPTY = new PrefCookie(true);

	public PrefCookie() {
		this(false);
	}

	protected PrefCookie(final boolean locked) {
		super(new HashMap<String, String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String put(String key, String value) {
				if (locked) {
					throw new IllegalStateException("PrefCookie is locked.");
				}
				return super.put(key, value);
			}
		});
	}

	public String getLanguage() {
		return this.entries.get(PARAM_LANGUAGE);
	}

	public PrefCookie setLanguage(String language) {
		this.entries.put(PARAM_LANGUAGE, language);
		return this;
	}

	public String getDisplayName() {
		return this.entries.get(PARAM_DISPLAY_NAME);
	}

	public PrefCookie setDisplayName(String displayName) {
		this.entries.put(PARAM_DISPLAY_NAME, displayName);
		return this;
	}

	@Override
	public PrefCookie parse(String queryString) {
		super.parse(queryString);
		return this;
	}

	public PrefCookie copy() {
		PrefCookie copy = new PrefCookie();
		if (this == EMPTY) {
			return copy;
		}
		for (Entry<String, String> entry : this.entries.entrySet()) {
			copy.entries.put(entry.getKey(), entry.getValue());
		}
		return copy;
	}
}
