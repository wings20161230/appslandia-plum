package com.appslandia.plum.web;

import javax.servlet.http.Cookie;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.appslandia.plum.mocks.MockHttpServletRequest;
import com.appslandia.plum.mocks.MockHttpServletResponse;
import com.appslandia.plum.mocks.MockWebContext;

public class PrefCookieHandlerTest {

	MockWebContext mockWebContext;
	PrefCookieHandler prefCookieHandler;

	MockHttpServletRequest request;
	MockHttpServletResponse response;

	@Before
	public void initialize() {
		mockWebContext = new MockWebContext();
		prefCookieHandler = mockWebContext.getObject(PrefCookieHandler.class);

		request = mockWebContext.createMockHttpServletRequest();
		response = mockWebContext.createMockHttpServletResponse();
	}

	private Cookie initPrefCookie() throws Exception {
		MockHttpServletResponse resp = mockWebContext.createMockHttpServletResponse();
		prefCookieHandler.savePrefCookie(resp, new PrefCookie().setLanguage("en").setDisplayName("user"));
		return resp.getCookie(prefCookieHandler.getCookieName());
	}

	@Test
	public void test_savePrefCookie() {
		try {
			MockHttpServletResponse response = mockWebContext.createMockHttpServletResponse();
			prefCookieHandler.savePrefCookie(response, new PrefCookie().setLanguage("en").setDisplayName("user"));

			Cookie savedCookie = response.getCookie(prefCookieHandler.getCookieName());
			Assert.assertNotNull(savedCookie);

			Assert.assertEquals(savedCookie.getDomain(), prefCookieHandler.getSessionConfig().getCookieDomain());
			Assert.assertEquals(savedCookie.getPath(), prefCookieHandler.getSessionConfig().getCookiePath());
			Assert.assertEquals(savedCookie.getMaxAge(), prefCookieHandler.getCookieMaxAge());

			Assert.assertEquals(savedCookie.getSecure(), prefCookieHandler.getSessionConfig().isCookieSecure());
			Assert.assertEquals(savedCookie.isHttpOnly(), prefCookieHandler.getSessionConfig().isCookieHttpOnly());

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_parsePrefCookie() {
		try {
			request.addCookie(initPrefCookie());
			PrefCookie parsedPrefCookie = prefCookieHandler.parsePrefCookie(request, response);

			Assert.assertNotNull(parsedPrefCookie);
			Assert.assertTrue(parsedPrefCookie != PrefCookie.EMPTY);
			Assert.assertEquals(parsedPrefCookie.getLanguage(), "en");
			Assert.assertEquals(parsedPrefCookie.getDisplayName(), "user");
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_parsePrefCookie_noCookie() {
		try {
			// request.addCookie(initPrefCookie());
			PrefCookie parsedPrefCookie = prefCookieHandler.parsePrefCookie(request, response);

			Assert.assertNotNull(parsedPrefCookie);
			Assert.assertTrue(parsedPrefCookie == PrefCookie.EMPTY);

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_removePrefCookie() {
		try {
			prefCookieHandler.removePrefCookie(response);
			Cookie deletedCookie = response.getCookie(prefCookieHandler.getCookieName());

			Assert.assertNotNull(deletedCookie);
			Assert.assertTrue(deletedCookie.getMaxAge() == 0);
			Assert.assertTrue("".equals(deletedCookie.getValue()));

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}
}
