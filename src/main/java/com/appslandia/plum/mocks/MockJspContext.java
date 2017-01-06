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
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.el.ELContext;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import com.appslandia.common.utils.AssertUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class MockJspContext extends PageContext {

	private HttpServletRequest request;
	private HttpServletResponse response;
	private JspWriter jspWriter;

	private ServletConfig servletConfig;
	private Exception exception;

	private Map<String, Object> pageScopeMap = new HashMap<>();
	private Map<String, Object> requestScopeMap = new HashMap<>();
	private Map<String, Object> sessionScopeMap = new HashMap<>();
	private Map<String, Object> applicationScopeMap = new HashMap<>();

	public MockJspContext(HttpServletRequest request, HttpServletResponse response) {
		this(request, response, new MockJspWriter());
	}

	public MockJspContext(HttpServletRequest request, HttpServletResponse response, JspWriter jspWriter) {
		this.request = request;
		this.response = response;
		this.jspWriter = jspWriter;
	}

	public MockHttpServletRequest getMockRequest() {
		return (MockHttpServletRequest) this.request;
	}

	public MockHttpServletResponse getMockResponse() {
		return (MockHttpServletResponse) this.response;
	}

	public MockHttpSession getMockSession() {
		return (MockHttpSession) this.getSession();
	}

	@Override
	public void initialize(Servlet servlet, ServletRequest request, ServletResponse response, String errorPageURL, boolean needsSession, int bufferSize, boolean autoFlush) throws IOException, IllegalStateException, IllegalArgumentException {
	}

	@Override
	public void release() {
	}

	@Override
	public HttpSession getSession() {
		return this.request.getSession();
	}

	@Override
	public Object getPage() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ServletRequest getRequest() {
		return this.request;
	}

	@Override
	public ServletResponse getResponse() {
		return this.response;
	}

	@Override
	public Exception getException() {
		return this.exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	@Override
	public ServletConfig getServletConfig() {
		AssertUtils.assertNotNull(this.servletConfig, "servletConfig is required.");
		return this.servletConfig;
	}

	public void setServletConfig(MockServletConfig servletConfig) {
		this.servletConfig = servletConfig;
	}

	@Override
	public ServletContext getServletContext() {
		return this.request.getServletContext();
	}

	@Override
	public void forward(String relativeUrlPath) throws ServletException, IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void include(String relativeUrlPath) throws ServletException, IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void include(String relativeUrlPath, boolean flush) throws ServletException, IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void handlePageException(Exception e) throws ServletException, IOException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void handlePageException(Throwable t) throws ServletException, IOException {
		throw new UnsupportedOperationException();
	}

	private Map<String, Object> getScopeMap(int scope) {
		if (scope == PAGE_SCOPE) {
			return this.pageScopeMap;
		}
		if (scope == REQUEST_SCOPE) {
			return this.requestScopeMap;
		}
		if (scope == SESSION_SCOPE) {
			return this.sessionScopeMap;
		}
		if (scope == APPLICATION_SCOPE) {
			return this.applicationScopeMap;
		}
		throw new IllegalArgumentException("The given scope is invalid.");
	}

	@Override
	public void setAttribute(String name, Object value) {
		setAttribute(name, value, PAGE_SCOPE);
	}

	@Override
	public void setAttribute(String name, Object value, int scope) {
		if (value == null) {
			getScopeMap(scope).remove(name);
		} else {
			getScopeMap(scope).put(name, value);
		}
	}

	@Override
	public Object getAttribute(String name) {
		return getAttribute(name, PAGE_SCOPE);
	}

	@Override
	public Object getAttribute(String name, int scope) {
		return getScopeMap(scope).get(name);
	}

	@Override
	public Object findAttribute(String name) {
		int scope = getAttributesScope(name);
		if (scope > 0) {
			return getScopeMap(scope).get(name);
		}
		return null;
	}

	@Override
	public void removeAttribute(String name) {
		this.pageScopeMap.remove(name);
		this.requestScopeMap.remove(name);
		this.sessionScopeMap.remove(name);
		this.applicationScopeMap.remove(name);
	}

	@Override
	public void removeAttribute(String name, int scope) {
		getScopeMap(scope).remove(name);
	}

	@Override
	public int getAttributesScope(String name) {
		if (this.pageScopeMap.containsKey(name))
			return PAGE_SCOPE;

		if (this.requestScopeMap.containsKey(name))
			return REQUEST_SCOPE;

		if (this.sessionScopeMap.containsKey(name))
			return SESSION_SCOPE;

		if (this.applicationScopeMap.containsKey(name))
			return APPLICATION_SCOPE;

		return 0;
	}

	@Override
	public Enumeration<String> getAttributeNamesInScope(int scope) {
		return Collections.enumeration(this.getScopeMap(scope).keySet());
	}

	@Override
	public JspWriter getOut() {
		return this.jspWriter;
	}

	@Deprecated
	@Override
	public javax.servlet.jsp.el.ExpressionEvaluator getExpressionEvaluator() {
		throw new UnsupportedOperationException();
	}

	@Deprecated
	@Override
	public javax.servlet.jsp.el.VariableResolver getVariableResolver() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ELContext getELContext() {
		throw new UnsupportedOperationException();
	}

}
