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

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import com.appslandia.common.base.CaseInsensitiveMap;
import com.appslandia.common.base.MemoryStream;
import com.appslandia.common.utils.AssertUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class MockHttpServletResponse implements HttpServletResponse {

	private MockServletContext servletContext;

	private String contentType;
	private String encoding;
	private int status = HttpServletResponse.SC_OK;
	private boolean committed;

	private MemoryStream content = new MemoryStream();
	private PrintWriter outWriter;
	private ServletOutputStream outStream;

	private List<Cookie> cookies = new ArrayList<Cookie>();
	private Map<String, MockHttpHeader> headers = new CaseInsensitiveMap<MockHttpHeader>();

	public MockHttpServletResponse(MockServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public MemoryStream getContent() {
		return this.content;
	}

	@Override
	public String getCharacterEncoding() {
		if (this.encoding == null) {
			return StandardCharsets.ISO_8859_1.name();
		}
		return this.encoding;
	}

	@Override
	public String getContentType() {
		AssertUtils.assertNotNull(this.contentType, "contentType is required.");
		return this.contentType;
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		if (this.outWriter != null) {
			throw new IllegalStateException("getWriter has been called on this response.");
		}
		if (this.outStream == null) {
			this.outStream = new ServletOutputStreamImpl();
		}
		return this.outStream;
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		if (this.outStream != null) {
			throw new IllegalStateException("getOutputStream has been called on this response.");
		}
		if (this.outWriter == null) {
			this.outWriter = new PrintWriter(new OutputStreamWriter(this.content, this.getCharacterEncoding()));
		}
		return this.outWriter;
	}

	@Override
	public void setCharacterEncoding(String encoding) {
		this.encoding = encoding;
	}

	@Override
	public void setContentLength(int len) {
	}

	@Override
	public void setContentLengthLong(long len) {
	}

	@Override
	public void setContentType(String type) {
		this.contentType = type;
	}

	@Override
	public void setBufferSize(int size) {
	}

	@Override
	public int getBufferSize() {
		throw new UnsupportedOperationException();
	}

	private void doFlush() throws IOException {
		if (this.outWriter != null) {
			this.outWriter.flush();

		} else if (this.outStream != null) {
			this.outStream.flush();
		}
	}

	@Override
	public void flushBuffer() throws IOException {
		doFlush();
		setCommitted(true);
	}

	@Override
	public void resetBuffer() {
		assertNotCommitted();
		try {
			doFlush();
		} catch (IOException ex) {
		}
		this.content.reset();
	}

	@Override
	public boolean isCommitted() {
		return this.committed;
	}

	public void setCommitted(boolean committed) {
		this.committed = committed;
	}

	@Override
	public void reset() {
		assertNotCommitted();

		this.encoding = null;
		this.cookies.clear();
		this.headers.clear();

		this.status = HttpServletResponse.SC_OK;
		this.resetBuffer();
	}

	@Override
	public void setLocale(Locale loc) {
	}

	@Override
	public Locale getLocale() {
		return Locale.getDefault();
	}

	@Override
	public String encodeURL(String url) {
		return url;
	}

	@Override
	public String encodeRedirectURL(String url) {
		return url;
	}

	@Override
	public String encodeUrl(String url) {
		return url;
	}

	@Override
	public String encodeRedirectUrl(String url) {
		return url;
	}

	private void assertNotCommitted() {
		if (isCommitted()) {
			throw new IllegalStateException("Response is already committed");
		}
	}

	@Override
	public void sendError(int sc, String msg) throws IOException {
		assertNotCommitted();
		this.status = sc;
		this.resetBuffer();
		setCommitted(true);
	}

	@Override
	public void sendError(int sc) throws IOException {
		assertNotCommitted();
		this.status = sc;
		this.resetBuffer();
		setCommitted(true);
	}

	@Override
	public void sendRedirect(String location) throws IOException {
		assertNotCommitted();
		setHeader("Location", location);
		this.status = 302;
		resetBuffer();
		setCommitted(true);
	}

	@Override
	public void setStatus(int sc) {
		assertNotCommitted();
		this.status = sc;
	}

	@Override
	public void setStatus(int sc, String sm) {
		assertNotCommitted();
		this.status = sc;
	}

	@Override
	public int getStatus() {
		return this.status;
	}

	@Override
	public void addCookie(Cookie cookie) {
		this.cookies.add(cookie);
	}

	public Cookie getCookie(String name) {
		return MockServletUtils.getCookie(this.cookies, name);
	}

	public void addCookie(String name, String value, int maxAge) {
		addCookie(name, value, maxAge, false, false, null, null);
	}

	public void addCookie(String name, String value, int maxAge, boolean isSecure, boolean httpOnly) {
		addCookie(name, value, maxAge, isSecure, httpOnly, null, null);
	}

	public void addCookie(String name, String value, int maxAge, boolean isSecure, boolean httpOnly, String domain, String path) {
		this.cookies.add(MockServletUtils.createCookie(this.servletContext, name, value, maxAge, isSecure, httpOnly, domain, path));
	}

	public void addSessionCookie(String value) {
		this.cookies.add(MockServletUtils.createSessionCookie(this.servletContext, value));
	}

	public void addHeaderValues(String name, Object... values) {
		MockServletUtils.addHeaderValues(this.headers, name, values);
	}

	public void setHeaderValues(String name, Object... values) {
		if ("Content-Length".equalsIgnoreCase(name) == false) {
			MockServletUtils.setHeaderValues(this.headers, name, values);
		}
	}

	@Override
	public void setDateHeader(String name, long date) {
		setHeaderValues(name, MockServletUtils.toDateHeaderString(date));
	}

	@Override
	public void addDateHeader(String name, long date) {
		addHeaderValues(name, MockServletUtils.toDateHeaderString(date));
	}

	@Override
	public void setHeader(String name, String value) {
		setHeaderValues(name, value);
	}

	@Override
	public void addHeader(String name, String value) {
		addHeaderValues(name, value);
	}

	@Override
	public void setIntHeader(String name, int value) {
		setHeaderValues(name, value);
	}

	@Override
	public void addIntHeader(String name, int value) {
		addHeaderValues(name, value);
	}

	public long getDateHeader(String name) {
		return MockServletUtils.getDateHeader(this.headers, name);
	}

	public int getIntHeader(String name) {
		return MockServletUtils.getIntHeader(this.headers, name);
	}

	@Override
	public boolean containsHeader(String name) {
		return MockHttpHeader.getByName(this.headers, name) != null;
	}

	@Override
	public String getHeader(String name) {
		MockHttpHeader header = MockHttpHeader.getByName(this.headers, name);
		return header != null ? header.getStringValue() : null;
	}

	@Override
	public Collection<String> getHeaders(String name) {
		MockHttpHeader header = MockHttpHeader.getByName(this.headers, name);
		if (header != null) {
			return header.getStringValues();
		} else {
			return Collections.emptyList();
		}
	}

	@Override
	public Collection<String> getHeaderNames() {
		return this.headers.keySet();
	}

	private class ServletOutputStreamImpl extends ServletOutputStream {

		@Override
		public void write(int b) throws IOException {
			content.write(b);
		}

		@Override
		public void write(byte[] b, int off, int len) throws IOException {
			content.write(b, off, len);
		}

		@Override
		public void write(byte[] b) throws IOException {
			content.write(b);
		}

		@Override
		public boolean isReady() {
			return true;
		}

		@Override
		public void setWriteListener(WriteListener writeListener) {
			throw new UnsupportedOperationException();
		}
	}
}
