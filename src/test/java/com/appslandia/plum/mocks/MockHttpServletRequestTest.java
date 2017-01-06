package com.appslandia.plum.mocks;

import javax.servlet.DispatcherType;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.appslandia.plum.base.ServletUtils;

public class MockHttpServletRequestTest {

	MockServletContext servletContext;

	@Before
	public void initialize() {
		servletContext = new MockServletContext(new MockSessionCookieConfig());
	}

	@Test
	public void test() {
		MockHttpServletRequest request = new MockHttpServletRequest(servletContext);

		Assert.assertNotNull(request.getServletContext());
		Assert.assertNull(request.getSession(false));
		Assert.assertNotNull(request.getSession(true));

		Assert.assertEquals(request.getSession(true), request.getSession());

		request.setScheme("https");
		Assert.assertEquals(request.getScheme(), "https");

		request.setMethod("POST");
		Assert.assertEquals(request.getMethod(), "POST");

		request.setCharacterEncoding("UTF-8");
		Assert.assertEquals(request.getCharacterEncoding(), "UTF-8");

		request.setDispatcherType(DispatcherType.ERROR);
		Assert.assertEquals(request.getDispatcherType(), DispatcherType.ERROR);

		request.setQueryString("var1=value1&var2=value2");
		Assert.assertEquals(request.getQueryString(), "var1=value1&var2=value2");

		request.setRequestURI("/user/index.html");
		Assert.assertEquals(request.getServletPath(), "/user/index.html");
	}

	@Test
	public void test_attributes() {
		MockHttpServletRequest request = new MockHttpServletRequest(servletContext);

		request.setAttribute("attr1", "value1");
		request.setAttribute("attr2", "value2");

		request.setAttribute("attr3", "value3");
		request.setAttribute("attr3", null);

		Assert.assertEquals(request.getAttribute("attr1"), "value1");
		Assert.assertNull(request.getAttribute("attr3"));

		request.removeAttribute("attr2");
		Assert.assertNull(request.getAttribute("attr2"));
	}

	@Test
	public void test_parameters() {
		MockHttpServletRequest request = new MockHttpServletRequest(servletContext);

		request.addParameter("p1", "v1");
		request.addParameter("p2", "v21", "v22");

		request.addParameter("p3", "v31");
		request.addParameter("p3", "v32");

		Assert.assertEquals(request.getParameter("p1"), "v1");
		Assert.assertEquals(request.getParameter("p2"), "v21");
		Assert.assertEquals(request.getParameter("p3"), "v31");

		Assert.assertArrayEquals(request.getParameterValues("p2"), new String[] { "v21", "v22" });
		Assert.assertArrayEquals(request.getParameterValues("p3"), new String[] { "v31", "v32" });
	}

	@Test
	public void test_headers() {
		MockHttpServletRequest request = new MockHttpServletRequest(servletContext);

		request.setHeaderValues("h1", "v1");
		request.setHeaderValues("h2", "v21", "v22");
		request.setHeaderValues("h3", 1, 2);

		Assert.assertEquals(request.getHeader("h1"), "v1");
		Assert.assertEquals(request.getHeader("h2"), "v21");
		Assert.assertEquals(request.getHeaders("h2").nextElement(), "v21");

		Assert.assertEquals(request.getHeader("h3"), "1");
		Assert.assertEquals(request.getIntHeader("h3"), 1);
		Assert.assertEquals(request.getHeaders("h3").nextElement(), "1");

		request.addSessionCookie("session-1");
		Assert.assertEquals(request.getCookie(request.getServletContext().getSessionCookieConfig().getName()).getValue(), "session-1");
	}

	@Test
	public void test_cookies() {
		MockHttpServletRequest request = new MockHttpServletRequest(servletContext);
		Assert.assertNull(request.getCookies());

		request.addCookie("cookie1", "v1", 0);
		Assert.assertNotNull(request.getCookies());

		String cookie = ServletUtils.getCookieValue(request, "cookie1");
		Assert.assertEquals(cookie, "v1");

		request.addSessionCookie("session-1");
		Assert.assertEquals(request.getRequestedSessionId(), "session-1");
	}

	@Test
	public void test_invalidate() {
		MockHttpServletRequest request = new MockHttpServletRequest(servletContext);
		request.getSession().invalidate();
		Assert.assertNull(request.getSession(false));
		Assert.assertNotNull(request.getSession());
	}
}
