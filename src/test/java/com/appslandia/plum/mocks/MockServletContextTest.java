package com.appslandia.plum.mocks;

import org.junit.Assert;
import org.junit.Test;

public class MockServletContextTest {

	@Test
	public void test() {
		MockServletContext ctx = new MockServletContext(new MockSessionCookieConfig());

		Assert.assertNotNull(ctx.getSessionCookieConfig());
		Assert.assertEquals(ctx.getContextPath(), "/app");

		ctx.setAppDir("C:/webApps");
		Assert.assertEquals(ctx.getAppDir(), "C:/webApps");
	}

	@Test
	public void test_attributes() {
		MockServletContext ctx = new MockServletContext(new MockSessionCookieConfig());

		ctx.setAttribute("location", "location1");
		Assert.assertEquals(ctx.getAttribute("location"), "location1");

		ctx.removeAttribute("location");
		Assert.assertNull(ctx.getAttribute("location"));
	}

	@Test
	public void test_initParameters() {
		MockServletContext ctx = new MockServletContext(new MockSessionCookieConfig());

		ctx.setInitParameter("p1", "v1");
		ctx.setInitParameter("p2", "v2");

		Assert.assertEquals(ctx.getInitParameter("p1"), "v1");
	}
}
