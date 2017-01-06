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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpUpgradeHandler;
import javax.servlet.http.Part;

import com.appslandia.common.base.CaseInsensitiveMap;
import com.appslandia.common.utils.AssertUtils;
import com.appslandia.plum.base.ServletUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class MockHttpServletRequest implements HttpServletRequest {

	private MockHttpSession session;
	private ServletContext servletContext;

	private byte[] content = {};
	private String contentType;
	private String characterEncoding;

	private MockServletInputStream inputStream;
	private BufferedReader reader;

	private String scheme = "http";
	private String serverName = "localhost";
	private int serverPort = 8080;
	private String remoteAddr = "127.0.0.1";
	private boolean isAsyncStarted = false;
	private boolean isRequestedSessionIdValid = true;

	private boolean authenticated = false;
	private String remoteUser;
	private String authType;
	private Principal userPrincipal;
	private String[] userRoles;
	private MockUserInfoManager mockUserInfoManager;

	private DispatcherType dispatcherType = DispatcherType.REQUEST;
	private String method = "GET";

	// requestURI = contextPath + servletPath + pathInfo
	// pathTranslated = <documentRoot> + pathInfo

	private String requestURI;
	private String queryString;
	private String servletPath;
	private String pathInfo;

	private Map<String, Object> attributes = new HashMap<>();
	private Map<String, String[]> parameterMap = new HashMap<>();
	private List<Cookie> cookies = new ArrayList<>();
	private Map<String, MockHttpHeader> headers = new CaseInsensitiveMap<MockHttpHeader>();

	public MockHttpServletRequest(ServletContext servletContext) {
		this(servletContext, null);
	}

	public MockHttpServletRequest(ServletContext servletContext, MockHttpSession session) {
		this.servletContext = servletContext;
		this.session = session;
	}

	public HttpServletRequest getRequestWrapper() {
		return (HttpServletRequest) getAttribute(ServletUtils.REQUEST_ATTRIBUTE_REQUEST_WRAPPER);
	}

	protected byte[] getContent() {
		AssertUtils.assertNotNull(this.content, "content is required.");
		return this.content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	@Override
	public String getContentType() {
		AssertUtils.assertNotNull(this.contentType, "contentType is required.");
		return this.contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	@Override
	public int getContentLength() {
		return this.getContent().length;
	}

	@Override
	public long getContentLengthLong() {
		return getContentLength();
	}

	@Override
	public String getCharacterEncoding() {
		return this.characterEncoding;
	}

	@Override
	public void setCharacterEncoding(String characterEncoding) {
		this.characterEncoding = characterEncoding;
	}

	@Override
	public MockServletInputStream getInputStream() throws IOException {
		if (this.reader != null) {
			throw new IllegalStateException("getReader has been called on this request.");
		}
		if (this.inputStream == null) {
			this.inputStream = new MockServletInputStream(new ByteArrayInputStream(this.getContent()));
		}
		return this.inputStream;
	}

	@Override
	public BufferedReader getReader() throws IOException {
		if (this.inputStream != null) {
			throw new IllegalStateException("getInputStream has been called on this request.");
		}
		if (this.reader == null) {
			String encoding = this.characterEncoding;
			if (encoding == null) {
				encoding = StandardCharsets.ISO_8859_1.name();
			}
			this.reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(this.getContent()), encoding));
		}
		return this.reader;
	}

	@Override
	public String getProtocol() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getScheme() {
		AssertUtils.assertNotNull(this.scheme, "scheme is required.");
		return this.scheme;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	@Override
	public boolean isSecure() {
		return "https".equalsIgnoreCase(this.scheme);
	}

	@Override
	public String getServerName() {
		AssertUtils.assertNotNull(this.serverName, "serverName is required.");
		return this.serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	@Override
	public int getServerPort() {
		AssertUtils.assertTrue(this.serverPort > 0, "serverPort is required.");
		return this.serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	@Override
	public String getRemoteAddr() {
		AssertUtils.assertNotNull(this.serverName, "remoteAddr is required.");
		return this.remoteAddr;
	}

	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}

	@Override
	public String getRemoteHost() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getRemotePort() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getLocalAddr() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getLocalName() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getLocalPort() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getMethod() {
		AssertUtils.assertNotNull(this.method, "method is required.");
		return this.method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	@Override
	public String getRequestURI() {
		AssertUtils.assertNotNull(this.requestURI, "requestURI is required.");
		return this.requestURI;
	}

	public void setRequestURI(String servletPath) {
		setRequestURI(servletPath, null);
	}

	public void setRequestURI(String servletPath, String pathInfo) {
		AssertUtils.assertNotNull(servletPath, "servletPath is required.");

		this.servletPath = servletPath;
		this.pathInfo = pathInfo;

		if (pathInfo != null) {
			this.requestURI = this.servletContext.getContextPath() + servletPath + pathInfo;
		} else {
			this.requestURI = this.servletContext.getContextPath() + servletPath;
		}
	}

	@Override
	public StringBuffer getRequestURL() {
		StringBuffer url = new StringBuffer(this.scheme).append("://").append(this.serverName);

		if ((("http".equalsIgnoreCase(this.scheme) && this.serverPort != 80) || ("https".equalsIgnoreCase(this.scheme) && this.serverPort != 443))) {
			url.append(':').append(this.serverPort);
		}

		url.append(getRequestURI());
		return url;
	}

	@Override
	public String getQueryString() {
		return this.queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	@Override
	public String getServletPath() {
		AssertUtils.assertNotNull(this.servletPath, "servletPath is required.");
		return this.servletPath;
	}

	@Override
	public String getPathInfo() {
		return this.pathInfo;
	}

	public void setPathInfo(String pathInfo) {
		this.pathInfo = pathInfo;
	}

	@Override
	public String getPathTranslated() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getRealPath(String path) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getContextPath() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Locale getLocale() {
		return Locale.getDefault();
	}

	@Override
	public Enumeration<Locale> getLocales() {
		throw new UnsupportedOperationException();
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String path) {
		return new MockRequestDispatcher(path);
	}

	@Override
	public DispatcherType getDispatcherType() {
		AssertUtils.assertNotNull(this.dispatcherType, "dispatcherType is required.");
		return this.dispatcherType;
	}

	public void setDispatcherType(DispatcherType dispatcherType) {
		this.dispatcherType = dispatcherType;
	}

	@Override
	public ServletContext getServletContext() {
		return this.servletContext;
	}

	@Override
	public AsyncContext startAsync() throws IllegalStateException {
		throw new UnsupportedOperationException();
	}

	@Override
	public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isAsyncStarted() {
		return this.isAsyncStarted;
	}

	public void setAsyncStarted(boolean isAsyncStarted) {
		this.isAsyncStarted = isAsyncStarted;
	}

	@Override
	public boolean isAsyncSupported() {
		throw new UnsupportedOperationException();
	}

	@Override
	public AsyncContext getAsyncContext() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Cookie[] getCookies() {
		if (this.cookies.isEmpty()) {
			return null;
		}
		return MockServletUtils.toCookies(this.cookies);
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

	public void addCookie(Cookie cookie) {
		this.cookies.add(cookie);
	}

	public void addSessionCookie(String value) {
		this.cookies.add(MockServletUtils.createSessionCookie(this.servletContext, value));
	}

	public void removeCookie(final String name) {
		this.cookies.removeIf(e -> e.getName().equalsIgnoreCase(name));
	}

	public void addHeaderValues(String name, Object... values) {
		MockServletUtils.addHeaderValues(this.headers, name, values);
	}

	public void setHeaderValues(String name, Object... values) {
		MockServletUtils.setHeaderValues(this.headers, name, values);
	}

	public void setDateHeader(String name, long date) {
		setHeaderValues(name, MockServletUtils.toDateHeaderString(date));
	}

	public void addDateHeader(String name, long date) {
		addHeaderValues(name, MockServletUtils.toDateHeaderString(date));
	}

	public void setHeader(String name, String value) {
		setHeaderValues(name, value);
	}

	public void addHeader(String name, String value) {
		addHeaderValues(name, value);
	}

	public void setIntHeader(String name, int value) {
		setHeaderValues(name, value);
	}

	public void addIntHeader(String name, int value) {
		addHeaderValues(name, value);
	}

	public void setXForwardedFor(String value) {
		setHeader("X-Forwarded-For", value);
	}

	public void setAcceptEncoding(String value) {
		setHeader("Accept-Encoding", value);
	}

	@Override
	public long getDateHeader(String name) {
		return MockServletUtils.getDateHeader(this.headers, name);
	}

	@Override
	public int getIntHeader(String name) {
		return MockServletUtils.getIntHeader(this.headers, name);
	}

	@Override
	public String getHeader(String name) {
		MockHttpHeader header = MockHttpHeader.getByName(this.headers, name);
		return (header != null ? header.getStringValue() : null);
	}

	@Override
	public Enumeration<String> getHeaders(String name) {
		MockHttpHeader header = MockHttpHeader.getByName(this.headers, name);
		return Collections.enumeration(header != null ? header.getStringValues() : Collections.emptyList());
	}

	@Override
	public Enumeration<String> getHeaderNames() {
		return Collections.enumeration(this.headers.keySet());
	}

	@Override
	public String getParameter(String name) {
		String[] values = this.parameterMap.get(name);
		return (values != null) ? values[0] : null;
	}

	@Override
	public Enumeration<String> getParameterNames() {
		return Collections.enumeration(this.parameterMap.keySet());
	}

	@Override
	public String[] getParameterValues(String name) {
		return this.parameterMap.get(name);
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		return this.parameterMap;
	}

	public void addParameter(String name, String... vals) {
		MockServletUtils.addParameter(this.parameterMap, name, vals);
	}

	public void setParameter(String name, String... vals) {
		this.parameterMap.remove(name);
		MockServletUtils.addParameter(this.parameterMap, name, vals);
	}

	public void clearParameters() {
		this.parameterMap.clear();
	}

	@Override
	public Object getAttribute(String name) {
		return this.attributes.get(name);
	}

	@Override
	public void setAttribute(String name, Object value) {
		if (value != null) {
			this.attributes.put(name, value);
		} else {
			removeAttribute(name);
		}
	}

	@Override
	public void removeAttribute(String name) {
		this.attributes.remove(name);
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		return Collections.enumeration(this.attributes.keySet());
	}

	@Override
	public String getAuthType() {
		if (this.authenticated == false) {
			return null;
		}
		AssertUtils.assertNotNull(this.authType, "authenticated but authType is null.");
		return this.authType;
	}

	@Override
	public String getRemoteUser() {
		if (this.authenticated == false) {
			return null;
		}
		AssertUtils.assertNotNull(this.remoteUser, "authenticated but remoteUser is null.");
		return this.remoteUser;
	}

	@Override
	public boolean isUserInRole(String role) {
		if (this.authenticated == false) {
			return false;
		}
		for (String userRole : this.userRoles) {
			if (role.equalsIgnoreCase(userRole)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Principal getUserPrincipal() {
		if (this.authenticated == false) {
			return null;
		}
		AssertUtils.assertNotNull(this.userPrincipal, "authenticated but userPrincipal is null.");
		return this.userPrincipal;
	}

	@Override
	public HttpSession getSession(boolean create) {
		if (this.session != null && this.session.isInvalidated()) {
			this.session = null;
		}
		if (create) {
			if (this.session == null) {
				this.session = new MockHttpSession(this.servletContext);
			}
		}
		return this.session;
	}

	@Override
	public HttpSession getSession() {
		return getSession(true);
	}

	@Override
	public String changeSessionId() {
		if (this.session != null) {
			return this.session.changeSessionId();
		}
		return (null);
	}

	@Override
	public boolean isRequestedSessionIdValid() {
		return this.isRequestedSessionIdValid;
	}

	public void setRequestedSessionIdValid(boolean isRequestedSessionIdValid) {
		this.isRequestedSessionIdValid = isRequestedSessionIdValid;
	}

	@Override
	public String getRequestedSessionId() {
		return getCookie(this.servletContext.getSessionCookieConfig().getName()).getValue();
	}

	@Override
	public boolean isRequestedSessionIdFromCookie() {
		return true;
	}

	@Override
	public boolean isRequestedSessionIdFromURL() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isRequestedSessionIdFromUrl() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean authenticate(HttpServletResponse response) throws IOException, ServletException {
		throw new UnsupportedOperationException();
	}

	protected MockUserInfoManager getMockUserInfoManager() {
		AssertUtils.assertNotNull(this.mockUserInfoManager, "mockUserInfoManager is required.");
		return this.mockUserInfoManager;
	}

	public void setMockUserInfoManager(MockUserInfoManager mockUserInfoManager) {
		this.mockUserInfoManager = mockUserInfoManager;
	}

	@Override
	public void login(String remoteUser, String password) throws ServletException {
		MockUser user = this.getMockUserInfoManager().findMockUser(remoteUser);
		if ((user == null) || (user.getPassword().equals(password) == false)) {
			throw new ServletException("login");
		}

		this.authenticated = true;
		this.remoteUser = remoteUser;
		this.userPrincipal = new MockPrincipal(remoteUser);
		this.authType = "mock-auth";
		this.userRoles = user.getUserRoles();
	}

	@Override
	public void logout() throws ServletException {
		this.authenticated = false;

		this.userPrincipal = null;
		this.remoteUser = null;
		this.authType = null;
	}

	public void login(String remoteUser) {
		MockUser user = this.getMockUserInfoManager().findMockUser(remoteUser);
		AssertUtils.assertNotNull(user);

		this.authenticated = true;
		this.remoteUser = remoteUser;
		this.userPrincipal = new MockPrincipal(remoteUser);
		this.authType = "mock-auth";
		this.userRoles = user.getUserRoles();
	}

	@Override
	public Collection<Part> getParts() throws IOException, ServletException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Part getPart(String name) throws IOException, ServletException {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T extends HttpUpgradeHandler> T upgrade(Class<T> handlerClass) throws IOException, ServletException {
		throw new UnsupportedOperationException();
	}
}
