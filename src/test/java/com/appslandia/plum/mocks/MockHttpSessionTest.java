package com.appslandia.plum.mocks;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MockHttpSessionTest {

	MockServletContext servletContext;

	@Before
	public void initialize() {
		servletContext = new MockServletContext(new MockSessionCookieConfig());
	}

	@Test
	public void test() {
		MockHttpSession session = new MockHttpSession(servletContext);
		Assert.assertNotNull(session.getServletContext());

		session.setMaxInactiveInterval(1800);
		Assert.assertEquals(session.getMaxInactiveInterval(), 1800);
	}

	@Test
	public void test_attributes() {
		MockHttpSession session = new MockHttpSession(servletContext);

		session.setAttribute("attr1", "value1");
		session.setAttribute("attr2", "value2");

		Assert.assertEquals(session.getAttribute("attr1"), "value1");
		Assert.assertNull(session.getAttribute("attr3"));

		session.removeAttribute("attr2");
		Assert.assertNull(session.getAttribute("attr2"));
	}

	@Test
	public void test_invalidate() {
		MockHttpSession session = new MockHttpSession(servletContext);

		// isInvalidated
		Assert.assertFalse(session.isInvalidated());

		session.invalidate();
		Assert.assertTrue(session.isInvalidated());
	}

	@Test
	public void test_changeSessionId() {
		MockHttpSession session = new MockHttpSession(servletContext);
		String oldId = session.getId();

		session.changeSessionId();
		Assert.assertNotEquals(session.getId(), oldId);
	}
}
