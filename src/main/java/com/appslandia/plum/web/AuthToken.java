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
public class AuthToken extends QueryParams {
	private static final long serialVersionUID = 1L;

	public static final String FIELD_SERIES = "ser";
	public static final String FIELD_TOKEN = "tok";
	public static final String FIELD_EXPIRES = "exp";

	public static final AuthToken EMPTY = new AuthToken(true);

	public AuthToken() {
		this(false);
	}

	protected AuthToken(final boolean locked) {
		super(new HashMap<String, String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String put(String key, String value) {
				if (locked) {
					throw new IllegalStateException("AuthToken is locked.");
				}
				return super.put(key, value);
			}
		});
	}

	public String getSeries() {
		return this.entries.get(FIELD_SERIES);
	}

	public AuthToken setSeries(String series) {
		this.entries.put(FIELD_SERIES, series);
		return this;
	}

	public String getToken() {
		return this.entries.get(FIELD_TOKEN);
	}

	public AuthToken setToken(String token) {
		this.entries.put(FIELD_TOKEN, token);
		return this;
	}

	public long getExpires() {
		String expires = this.entries.get(FIELD_EXPIRES);
		if (expires != null) {
			try {
				return Long.parseLong(expires);
			} catch (NumberFormatException ex) {
			}
		}
		return 0;
	}

	public AuthToken setExpires(long expires) {
		this.entries.put(FIELD_EXPIRES, Long.toString(expires));
		return this;
	}

	@Override
	public AuthToken parse(String keyValues) {
		super.parse(keyValues);
		return this;
	}

	public AuthToken copy() {
		AuthToken copy = new AuthToken();
		if (this == EMPTY) {
			return copy;
		}
		for (Entry<String, String> entry : this.entries.entrySet()) {
			copy.entries.put(entry.getKey(), entry.getValue());
		}
		return copy;
	}
}
