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

import com.appslandia.common.utils.ExceptionUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class Result {

	private int errcode;
	private String message;
	private String exception;
	private String link;
	private Object data;

	public int getErrcode() {
		return this.errcode;
	}

	public Result asError() {
		return setErrcode(1);
	}

	public Result setErrcode(int errcode) {
		this.errcode = errcode;
		return this;
	}

	public String getMessage() {
		return this.message;
	}

	public Result setMessage(String message) {
		this.message = message;
		return this;
	}

	public String getException() {
		return this.exception;
	}

	public Result setException(Throwable exception) {
		this.exception = ExceptionUtils.toString(exception);
		return this;
	}

	public String getLink() {
		return this.link;
	}

	public Result setLink(String link) {
		this.link = link;
		return this;
	}

	public Object getData() {
		return this.data;
	}

	public Result setData(Object data) {
		this.data = data;
		return this;
	}
}
