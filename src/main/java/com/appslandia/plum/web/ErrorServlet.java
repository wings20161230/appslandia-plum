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

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import javax.servlet.DispatcherType;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.appslandia.common.utils.ContentTypes;
import com.appslandia.plum.base.ServletUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public abstract class ErrorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected abstract void writeError(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

	protected void doRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getDispatcherType().equals(DispatcherType.ERROR) == false) {
			writeSimpleError(request, response, HttpServletResponse.SC_FORBIDDEN, "Forbidden");
			return;
		}
		try {
			writeError(request, response);

		} catch (Exception ex) {
			if (response.isCommitted()) {
				response.flushBuffer();
			} else {
				// Not committed
				response.resetBuffer();
				writeSimpleError(request, response, response.getStatus(), "Internal Server Error!");
			}
		}
	}

	public static void writeSimpleError(HttpServletRequest request, HttpServletResponse response, int status, String message) throws ServletException, IOException {
		response.setContentType(ContentTypes.TEXT_HTML);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setStatus(status);

		try (PrintWriter writer = ServletUtils.getWriter(response)) {
			writer.println("<!DOCTYPE html>");
			writer.println("<html>");
			writer.println();
			writer.println("<head>");
			writer.println(" <meta charset=\"utf-8\">");
			writer.println(" <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">");
			writer.format(" <title>HTTP Status %d</title>", status);
			writer.println();
			writer.println("</head>");
			writer.println("<body>");
			writer.format(" <h2>HTTP Status %d - %s</h2>", status, message);
			writer.println();
			writer.println("</body>");
			writer.print("</html>");
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doRequest(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doRequest(request, response);
	}

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doRequest(request, response);
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doRequest(request, response);
	}

	@Override
	protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doRequest(request, response);
	}

	@Override
	protected void doTrace(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doRequest(request, response);
	}
}
