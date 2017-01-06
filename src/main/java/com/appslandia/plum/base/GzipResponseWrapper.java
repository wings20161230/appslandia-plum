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

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class GzipResponseWrapper extends HttpServletResponseWrapper implements ResponseWrapper {

	private GZIPServletOutputStream outStream;
	private PrintWriter outWriter;
	private Boolean usedWriter;

	public GzipResponseWrapper(HttpServletResponse response) {
		super(response);
		super.setHeader("Content-Encoding", "gzip");
	}

	@Override
	public void finishWrapper() throws IOException {
		if (this.outWriter != null) {
			this.outWriter.flush();
			this.outStream.finish();

		} else if (this.outStream != null) {
			this.outStream.finish();
		}
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		if (Boolean.TRUE.equals(this.usedWriter)) {
			throw new IllegalStateException("getWriter has been called on this response.");
		}
		if (this.outStream == null) {
			this.outStream = new GZIPServletOutputStream(super.getOutputStream());
			this.usedWriter = Boolean.FALSE;
		}
		return this.outStream;
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		if (Boolean.FALSE.equals(this.usedWriter)) {
			throw new IllegalStateException("getOutputStream has been called on this response.");
		}
		if (this.outWriter == null) {
			this.outStream = new GZIPServletOutputStream(super.getOutputStream());
			this.outWriter = new PrintWriter(new OutputStreamWriter(this.outStream, this.getCharacterEncoding()));
			this.usedWriter = Boolean.TRUE;
		}
		return this.outWriter;
	}

	@Override
	public void flushBuffer() throws IOException {
		if (this.outWriter != null) {
			this.outWriter.flush();

		} else if (this.outStream != null) {
			this.outStream.flush();
		}
	}

	@Override
	public void setContentLength(int len) {
	}

	@Override
	public void setContentLengthLong(long len) {
	}

	@Override
	public void setHeader(String name, String value) {
		if ("Content-Length".equalsIgnoreCase(name) == false) {
			super.setHeader(name, value);
		}
	}

	@Override
	public void setIntHeader(String name, int value) {
		if ("Content-Length".equalsIgnoreCase(name) == false) {
			super.setIntHeader(name, value);
		}
	}

	@Override
	public void sendError(int sc) throws IOException {
		setStatus(sc);
		resetBuffer();
	}

	@Override
	public void sendError(int sc, String msg) throws IOException {
		sendError(sc);
	}

	private static class GZIPServletOutputStream extends ServletOutputStream {

		final ServletOutputStream os;
		final GZIPOutputStream gos;

		public GZIPServletOutputStream(ServletOutputStream os) throws IOException {
			this.os = os;
			this.gos = new GZIPOutputStream(os);
		}

		@Override
		public void write(int b) throws IOException {
			this.gos.write(b);
		}

		@Override
		public void write(byte[] b, int off, int len) throws IOException {
			this.gos.write(b, off, len);
		}

		@Override
		public void write(byte[] b) throws IOException {
			this.gos.write(b);
		}

		@Override
		public void flush() throws IOException {
			this.gos.flush();
		}

		@Override
		public void close() throws IOException {
			this.gos.close();
		}

		@Override
		public boolean isReady() {
			return this.os.isReady();
		}

		@Override
		public void setWriteListener(WriteListener writeListener) {
			this.os.setWriteListener(writeListener);
		}

		public void finish() throws IOException {
			this.gos.finish();
		}
	}
}
