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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class MockHttpHeader {

	private List<Object> headerValues = new ArrayList<>();

	public void addValues(Object... values) {
		for (Object value : values) {
			if (this.headerValues.contains(value) == false) {
				this.headerValues.add(value);
			}
		}
	}

	public void setValues(Object... values) {
		this.headerValues.clear();
		addValues(values);
	}

	public List<Object> getValues() {
		return Collections.unmodifiableList(this.headerValues);
	}

	public List<String> getStringValues() {
		List<String> stringList = new ArrayList<String>(this.headerValues.size());
		for (Object value : this.headerValues) {
			stringList.add(value.toString());
		}
		return Collections.unmodifiableList(stringList);
	}

	public Object getValue() {
		return (this.headerValues.isEmpty() == false ? this.headerValues.get(0) : null);
	}

	public String getStringValue() {
		return (this.headerValues.isEmpty() == false ? this.headerValues.get(0).toString() : null);
	}

	public static MockHttpHeader getByName(Map<String, MockHttpHeader> headers, String name) {
		for (String headerName : headers.keySet()) {
			if (headerName.equalsIgnoreCase(name)) {
				return headers.get(headerName);
			}
		}
		return null;
	}
}
