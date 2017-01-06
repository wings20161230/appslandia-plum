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
import java.io.PrintWriter;

import javax.servlet.jsp.JspWriter;

import com.appslandia.common.base.StringWriter;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class MockJspWriter extends JspWriter {

	final StringWriter buffer;
	final PrintWriter writer;

	public MockJspWriter() {
		super(0, false);
		this.buffer = new StringWriter();
		this.writer = new PrintWriter(this.buffer);
	}

	public String getContent() {
		return this.buffer.toString();
	}

	@Override
	public void newLine() throws IOException {
		this.writer.println();
	}

	@Override
	public void print(boolean b) throws IOException {
		this.writer.print(b);
	}

	@Override
	public void print(char c) throws IOException {
		this.writer.print(c);
	}

	@Override
	public void print(int i) throws IOException {
		this.writer.print(i);
	}

	@Override
	public void print(long l) throws IOException {
		this.writer.print(l);
	}

	@Override
	public void print(float f) throws IOException {
		this.writer.print(f);
	}

	@Override
	public void print(double d) throws IOException {
		this.writer.print(d);
	}

	@Override
	public void print(char[] s) throws IOException {
		this.writer.print(s);
	}

	@Override
	public void print(String s) throws IOException {
		this.writer.print(s);
	}

	@Override
	public void print(Object obj) throws IOException {
		this.writer.print(obj);
	}

	@Override
	public void println() throws IOException {
		this.writer.println();
	}

	@Override
	public void println(boolean x) throws IOException {
		this.writer.println(x);
	}

	@Override
	public void println(char x) throws IOException {
		this.writer.println(x);
	}

	@Override
	public void println(int x) throws IOException {
		this.writer.println(x);
	}

	@Override
	public void println(long x) throws IOException {
		this.writer.println(x);
	}

	@Override
	public void println(float x) throws IOException {
		this.writer.println(x);
	}

	@Override
	public void println(double x) throws IOException {
		this.writer.println(x);
	}

	@Override
	public void println(char[] x) throws IOException {
		this.writer.println(x);
	}

	@Override
	public void println(String x) throws IOException {
		this.writer.println(x);
	}

	@Override
	public void println(Object x) throws IOException {
		this.writer.println(x);
	}

	@Override
	public void clear() throws IOException {
	}

	@Override
	public void clearBuffer() throws IOException {
	}

	@Override
	public void flush() throws IOException {
		this.writer.flush();
	}

	@Override
	public void close() throws IOException {
		this.writer.close();
	}

	@Override
	public int getRemaining() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
		this.writer.write(cbuf, off, len);
	}

	@Override
	public String toString() {
		return this.buffer.toString();
	}
}
