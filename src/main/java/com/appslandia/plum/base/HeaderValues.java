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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class HeaderValues {

	final List<Pair> pairs = new ArrayList<>();
	final String separator;
	final char kvPairSep;

	public HeaderValues() {
		this(", ");
	}

	public HeaderValues(String separator) {
		this(separator, '=');
	}

	public HeaderValues(String separator, char kvPairSep) {
		this.separator = separator;
		this.kvPairSep = kvPairSep;
	}

	public HeaderValues add(String value) {
		return add(value, null);
	}

	public HeaderValues add(String key, String value) {
		this.pairs.add(new Pair(key, value));
		return this;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Pair pair : this.pairs) {
			if (sb.length() > 0) {
				sb.append(this.separator);
			}
			sb.append(pair.key);
			if (pair.value != null) {
				sb.append(this.kvPairSep).append(pair.value);
			}
		}
		return sb.toString();
	}

	private static class Pair {
		final String key;
		final String value;

		public Pair(String key, String value) {
			this.key = key;
			this.value = value;
		}
	}

	public static String toValueList(String[] values) {
		return toValueList(values, ", ");
	}

	public static String toValueList(String[] values, String valueSep) {
		StringBuilder sb = new StringBuilder();
		for (String value : values) {
			if (sb.length() > 0) {
				sb.append(valueSep);
			}
			sb.append(value);
		}
		return sb.toString();
	}
}
