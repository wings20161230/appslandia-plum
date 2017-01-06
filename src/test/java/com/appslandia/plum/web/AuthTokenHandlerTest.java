package com.appslandia.plum.web;

import javax.servlet.http.Cookie;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.appslandia.plum.mocks.MockHttpServletRequest;
import com.appslandia.plum.mocks.MockHttpServletResponse;
import com.appslandia.plum.mocks.MockWebContext;

public class AuthTokenHandlerTest {

	MockWebContext mockWebContext;
	AuthTokenHandler authTokenHandler;

	MockHttpServletRequest request;
	MockHttpServletResponse response;

	@Before
	public void initialize() {
		mockWebContext = new MockWebContext();
		authTokenHandler = mockWebContext.getObject(AuthTokenHandler.class);

		request = mockWebContext.createMockHttpServletRequest();
		response = mockWebContext.createMockHttpServletResponse();
	}

	private Cookie initToken() throws Exception {
		int maxAge = authTokenHandler.getTokenAge();
		AuthToken authToken = new AuthToken().setSeries("series").setToken("token").setExpires(System.currentTimeMillis() + maxAge * 1000L);

		MockHttpServletResponse resp = mockWebContext.createMockHttpServletResponse();
		authTokenHandler.saveToken(resp, authToken, maxAge);
		return resp.getCookie(authTokenHandler.getTokenName());
	}

	@Test
	public void test_saveToken() {
		try {
			int maxAge = authTokenHandler.getTokenAge();
			AuthToken authToken = new AuthToken().setSeries("series").setToken("token").setExpires(System.currentTimeMillis() + maxAge * 1000L);
			authTokenHandler.saveToken(response, authToken, authTokenHandler.getTokenAge());

			Cookie savedCookie = response.getCookie(authTokenHandler.getTokenName());
			Assert.assertNotNull(savedCookie);

			Assert.assertEquals(savedCookie.getDomain(), authTokenHandler.getSessionConfig().getCookieDomain());
			Assert.assertEquals(savedCookie.getPath(), authTokenHandler.getSessionConfig().getCookiePath());
			Assert.assertEquals(savedCookie.getMaxAge(), maxAge);

			Assert.assertEquals(savedCookie.getSecure(), authTokenHandler.getSessionConfig().isCookieSecure());
			Assert.assertEquals(savedCookie.isHttpOnly(), authTokenHandler.getSessionConfig().isCookieHttpOnly());

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_parseToken() {
		try {
			request.addCookie(initToken());
			AuthToken parsedAuthToken = authTokenHandler.parseToken(request, response);

			Assert.assertNotNull(parsedAuthToken);
			Assert.assertTrue(parsedAuthToken != AuthToken.EMPTY);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_parseToken_noCookie() {
		try {
			AuthToken parsedAuthToken = authTokenHandler.parseToken(request, response);

			Assert.assertNotNull(parsedAuthToken);
			Assert.assertTrue(parsedAuthToken == AuthToken.EMPTY);

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_removeToken() {
		try {
			authTokenHandler.removeToken(response);
			Cookie deletedCookie = response.getCookie(authTokenHandler.getTokenName());

			Assert.assertNotNull(deletedCookie);
			Assert.assertTrue(deletedCookie.getMaxAge() == 0);
			Assert.assertTrue("".equals(deletedCookie.getValue()));

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}
}
