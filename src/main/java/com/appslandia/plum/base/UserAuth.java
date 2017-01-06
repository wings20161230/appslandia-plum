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

import java.io.Serializable;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class UserAuth implements Serializable {
	private static final long serialVersionUID = 1L;

	private String series;
	private String token;

	private int userId;
	private String remoteIp;
	private String clientId;

	private long expires;
	private long lastModified;

	public String getSeries() {
		return this.series;
	}

	public UserAuth setSeries(String series) {
		this.series = series;
		return this;
	}

	public String getToken() {
		return this.token;
	}

	public UserAuth setToken(String token) {
		this.token = token;
		return this;
	}

	public int getUserId() {
		return this.userId;
	}

	public UserAuth setUserId(int userId) {
		this.userId = userId;
		return this;
	}

	public String getRemoteIp() {
		return this.remoteIp;
	}

	public UserAuth setRemoteIp(String remoteIp) {
		this.remoteIp = remoteIp;
		return this;
	}

	public String getClientId() {
		return this.clientId;
	}

	public UserAuth setClientId(String clientId) {
		this.clientId = clientId;
		return this;
	}

	public long getExpires() {
		return this.expires;
	}

	public UserAuth setExpires(long expires) {
		this.expires = expires;
		return this;
	}

	public long getLastModified() {
		return this.lastModified;
	}

	public UserAuth setLastModified(long lastModified) {
		this.lastModified = lastModified;
		return this;
	}

	public UserAuth copy() {
		UserAuth obj = new UserAuth();
		obj.series = this.series;
		obj.token = this.token;

		obj.userId = this.userId;
		obj.remoteIp = this.remoteIp;
		obj.clientId = this.clientId;

		obj.expires = this.expires;
		obj.lastModified = this.lastModified;
		return obj;
	}
}
