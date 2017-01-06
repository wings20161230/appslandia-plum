package com.appslandia.plum.mocks;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MockHttpServletResponseTest {

	MockServletContext servletContext;

	@Before
	public void initialize() {
		servletContext = new MockServletContext(new MockSessionCookieConfig());
	}

	@Test
	public void test() {
		MockHttpServletResponse response = new MockHttpServletResponse(servletContext);
		response.setCharacterEncoding("UTF-8");
		Assert.assertEquals(response.getCharacterEncoding(), "UTF-8");

		response.setCommitted(true);
		Assert.assertEquals(response.isCommitted(), true);

		response.setContentType("text/html");
		Assert.assertEquals(response.getContentType(), "text/html");
	}

	@Test
	public void test_headers() {
		MockHttpServletResponse response = new MockHttpServletResponse(servletContext);

		response.addHeaderValues("h1", "v1");
		response.addHeaderValues("h2", "v21", "v22");
		response.addHeaderValues("h3", 1, 2);

		Assert.assertEquals(response.getHeader("h1"), "v1");
		response.addHeaderValues("h1", "v11");
		Assert.assertTrue(response.getHeaders("h1").size() == 2);

		Assert.assertEquals(response.getHeader("h2"), "v21");
		Assert.assertEquals(response.getHeaders("h2").iterator().next(), "v21");

		Assert.assertEquals(response.getHeader("h3"), "1");
		Assert.assertEquals(response.getHeaders("h3").iterator().next(), "1");
	}

	@Test
	public void test_sendError() {
		MockHttpServletResponse response = new MockHttpServletResponse(servletContext);
		try {
			response.sendError(500);
		} catch (IOException ex) {
		}
		Assert.assertEquals(response.getStatus(), 500);
	}

	@Test
	public void test_cookies() {
		MockHttpServletResponse response = new MockHttpServletResponse(servletContext);

		response.addCookie("cookie1", "v1", 0);
		Assert.assertNotNull(response.getCookie("cookie1"));
		Assert.assertEquals(response.getCookie("cookie1").getValue(), "v1");
	}

	@Test
	public void test_writeContent() {
		MockHttpServletResponse response = new MockHttpServletResponse(servletContext);
		try {
			response.getWriter().write("This is a test");
			response.flushBuffer();

			Assert.assertEquals(response.getContent().toString(), "This is a test");
		} catch (IOException ex) {
		}
	}
}
