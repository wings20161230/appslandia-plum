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
public class UserReauth implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final int STATUS_INIT = 1;
	public static final int STATUS_VERIFYING = 2;
	public static final int STATUS_REAUTHED = 3;

	private String series;
	private String token;
	private String verCode;

	private int userId;
	private String sesToken;
	private int status;
	private long expires;

	public String getSeries() {
		return this.series;
	}

	public UserReauth setSeries(String series) {
		this.series = series;
		return this;
	}

	public String getToken() {
		return this.token;
	}

	public UserReauth setToken(String token) {
		this.token = token;
		return this;
	}

	public String getVerCode() {
		return this.verCode;
	}

	public UserReauth setVerCode(String verCode) {
		this.verCode = verCode;
		return this;
	}

	public int getUserId() {
		return this.userId;
	}

	public UserReauth setUserId(int userId) {
		this.userId = userId;
		return this;
	}

	public String getSesToken() {
		return this.sesToken;
	}

	public UserReauth setSesToken(String sesToken) {
		this.sesToken = sesToken;
		return this;
	}

	public int getStatus() {
		return this.status;
	}

	public UserReauth setStatus(int status) {
		this.status = status;
		return this;
	}

	public long getExpires() {
		return this.expires;
	}

	public UserReauth setExpires(long expires) {
		this.expires = expires;
		return this;
	}

	public UserReauth copy() {
		UserReauth obj = new UserReauth();
		obj.series = this.series;
		obj.token = this.token;
		obj.verCode = this.verCode;

		obj.userId = this.userId;
		obj.sesToken = this.sesToken;
		obj.status = this.status;
		obj.expires = this.expires;
		return obj;
	}
}
