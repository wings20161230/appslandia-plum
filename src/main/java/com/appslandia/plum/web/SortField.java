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

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class SortField {

	private String sortBy;
	private boolean defAsc;
	private Boolean sortAsc;

	public SortField() {
	}

	public SortField(String sortBy, boolean defAsc) {
		this.sortBy = sortBy;
		this.defAsc = defAsc;
	}

	public String getSortBy() {
		return this.sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public boolean isDefAsc() {
		return this.defAsc;
	}

	public void setDefAsc(boolean defAsc) {
		this.defAsc = defAsc;
	}

	public Boolean getSortAsc() {
		return this.sortAsc;
	}

	public void setSortAsc(Boolean sortAsc) {
		this.sortAsc = sortAsc;
	}

	public Boolean getReverseAsc() {
		if (this.sortAsc == null) {
			return null;
		}
		if (Boolean.TRUE.equals(this.sortAsc)) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
}
