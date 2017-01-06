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

package com.appslandia.plum.results;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.appslandia.plum.base.ActionContext;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class JavaScriptResult extends ContentResult {

	public JavaScriptResult(String content) {
		super(content, "application/javacript", null);
	}

	public JavaScriptResult(String content, String contentType) {
		super(content, contentType, null);
	}

	public JavaScriptResult(String content, String contentType, String contentEncoding) {
		super(content, contentType, contentEncoding);
	}

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response, ActionContext actionContext) throws Exception {
		response.setContentType(this.contentType);

		if (this.contentEncoding != null) {
			response.setCharacterEncoding(this.contentEncoding);
		}

		try (PrintWriter writer = response.getWriter()) {
			writer.write("<script type=\"text/javascript\">");
			writer.write(this.content);
			writer.write("</script>");
		}
	}
}
