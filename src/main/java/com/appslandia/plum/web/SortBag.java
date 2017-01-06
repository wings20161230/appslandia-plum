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

import com.appslandia.common.utils.AssertUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class SortBag extends HashMap<String, SortField> {
	private static final long serialVersionUID = 1L;

	private SortField current;

	public SortBag put(String... sortFields) {
		for (String sortField : sortFields) {
			this.put(sortField, new SortField(sortField, true));
		}
		return this;
	}

	public SortBag put(String sortField, boolean defAsc) {
		this.put(sortField, new SortField(sortField, defAsc));
		return this;
	}

	public void setCurrent(SortField current, String defIfNotFound) {
		SortField refField = current.getSortBy() != null ? this.get(current.getSortBy()) : (null);
		if (refField != null) {
			if (current.getSortAsc() != null) {
				refField.setSortAsc(current.getSortAsc());
			}
		} else {
			refField = this.get(defIfNotFound);
			AssertUtils.assertNotNull(refField);
		}
		if (refField.getSortAsc() == null) {
			refField.setSortAsc(refField.isDefAsc());
		}
		this.current = refField;
	}

	public String getSortBy() {
		return AssertUtils.assertNotNull(this.current).getSortBy();
	}

	public boolean isSortAsc() {
		return AssertUtils.assertNotNull(this.current).getSortAsc();
	}
}
