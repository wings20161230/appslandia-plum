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

import java.io.Serializable;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class Message implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final int TYPE_INFO = 1;
	public static final int TYPE_NOTICE = 2;
	public static final int TYPE_ERROR = 3;
	public static final int TYPE_SUCCESS = 4;

	final int type;
	final String text;

	public Message(int type, String text) {
		this.type = type;
		this.text = text;
	}

	public int getType() {
		return this.type;
	}

	public String getText() {
		return this.text;
	}

	public boolean isInfo() {
		return this.type == TYPE_INFO;
	}

	public boolean isNotice() {
		return this.type == TYPE_NOTICE;
	}

	public boolean isError() {
		return this.type == TYPE_ERROR;
	}

	public boolean isSuccess() {
		return this.type == TYPE_SUCCESS;
	}
}
