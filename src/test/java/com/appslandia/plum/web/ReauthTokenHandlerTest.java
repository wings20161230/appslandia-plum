package com.appslandia.plum.web;

import javax.servlet.http.Cookie;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.appslandia.plum.mocks.MockHttpServletRequest;
import com.appslandia.plum.mocks.MockHttpServletResponse;
import com.appslandia.plum.mocks.MockWebContext;

public class ReauthTokenHandlerTest {

	MockWebContext mockWebContext;
	ReauthTokenHandler reauthTokenHandler;

	MockHttpServletRequest request;
	MockHttpServletResponse response;

	@Before
	public void initialize() {
		mockWebContext = new MockWebContext();
		reauthTokenHandler = mockWebContext.getObject(ReauthTokenHandler.class);

		request = mockWebContext.createMockHttpServletRequest();
		response = mockWebContext.createMockHttpServletResponse();
	}

	private Cookie initToken() throws Exception {
		int maxAge = reauthTokenHandler.getTokenAge();
		ReauthToken reauthToken = new ReauthToken().setSeries("series").setToken("token").setExpires(System.currentTimeMillis() + maxAge * 1000L);

		MockHttpServletResponse resp = mockWebContext.createMockHttpServletResponse();
		reauthTokenHandler.saveToken(resp, reauthToken, maxAge);
		return resp.getCookie(reauthTokenHandler.getTokenName());
	}

	@Test
	public void test_saveToken() {
		try {
			int maxAge = reauthTokenHandler.getTokenAge();
			ReauthToken reauthToken = new ReauthToken().setSeries("series").setToken("token").setExpires(System.currentTimeMillis() + maxAge * 1000L);
			reauthTokenHandler.saveToken(response, reauthToken, reauthTokenHandler.getTokenAge());

			Cookie savedCookie = response.getCookie(reauthTokenHandler.getTokenName());
			Assert.assertNotNull(savedCookie);

			Assert.assertEquals(savedCookie.getDomain(), reauthTokenHandler.getSessionConfig().getCookieDomain());
			Assert.assertEquals(savedCookie.getPath(), reauthTokenHandler.getSessionConfig().getCookiePath());
			Assert.assertEquals(savedCookie.getMaxAge(), maxAge);

			Assert.assertEquals(savedCookie.getSecure(), reauthTokenHandler.getSessionConfig().isCookieSecure());
			Assert.assertEquals(savedCookie.isHttpOnly(), reauthTokenHandler.getSessionConfig().isCookieHttpOnly());

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_parseToken() {
		try {
			request.addCookie(initToken());
			ReauthToken parsedReauthToken = reauthTokenHandler.parseToken(request, response);

			Assert.assertNotNull(parsedReauthToken);
			Assert.assertTrue(parsedReauthToken != ReauthToken.EMPTY);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_parseToken_noCookie() {
		try {
			ReauthToken parsedReauthToken = reauthTokenHandler.parseToken(request, response);

			Assert.assertNotNull(parsedReauthToken);
			Assert.assertTrue(parsedReauthToken == ReauthToken.EMPTY);

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_removeToken() {
		try {
			reauthTokenHandler.removeToken(response);
			Cookie deletedCookie = response.getCookie(reauthTokenHandler.getTokenName());

			Assert.assertNotNull(deletedCookie);
			Assert.assertTrue(deletedCookie.getMaxAge() == 0);
			Assert.assertTrue("".equals(deletedCookie.getValue()));

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}
}
