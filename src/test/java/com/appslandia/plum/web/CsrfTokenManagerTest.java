package com.appslandia.plum.web;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.appslandia.plum.base.ServletUtils;
import com.appslandia.plum.mocks.MockCsrfTokenManager;
import com.appslandia.plum.mocks.MockHttpServletRequest;
import com.appslandia.plum.mocks.MockHttpSession;
import com.appslandia.plum.mocks.MockWebContext;

public class CsrfTokenManagerTest {

	MockWebContext mockWebContext;
	CsrfTokenManager csrfTokenManager;

	MockHttpSession session;
	MockHttpServletRequest request;

	@Before
	public void initialize() {
		mockWebContext = new MockWebContext();
		csrfTokenManager = mockWebContext.getObject(CsrfTokenManager.class);

		session = mockWebContext.createMockHttpSession();
		request = mockWebContext.createMockHttpServletRequest(session);
	}

	private void initCsrfToken() throws Exception {
		csrfTokenManager.initCsrfToken(mockWebContext.createMockHttpServletRequest(session));
	}

	@Test
	public void test_initCsrfToken() {
		try {
			csrfTokenManager.initCsrfToken(request);
			String csrfToken = (String) request.getAttribute(ServletUtils.REQUEST_ATTRIBUTE_CSRF_TOKEN);
			Assert.assertNotNull(csrfToken);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_validateCsrfToken_notShared() {
		try {
			initCsrfToken();
			request.addParameter(ServletUtils.PARAM_CSRF_TOKEN, MockCsrfTokenManager.MOCK_CSRF_TOKEN);

			boolean tokenVerified = csrfTokenManager.isCsrfTokenValid(request, false);
			Assert.assertTrue(tokenVerified);

			boolean anotherTokenVerified = csrfTokenManager.isCsrfTokenValid(request, false);
			Assert.assertFalse(anotherTokenVerified);

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_validateCsrfToken_invalid() {
		try {
			initCsrfToken();
			request.addParameter(ServletUtils.PARAM_CSRF_TOKEN, "invalid");

			boolean tokenVerified = csrfTokenManager.isCsrfTokenValid(request, false);
			Assert.assertFalse(tokenVerified);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_validateCsrfToken_sharedToken() {
		try {
			initCsrfToken();
			request.addParameter(ServletUtils.PARAM_CSRF_TOKEN, MockCsrfTokenManager.MOCK_CSRF_TOKEN);

			boolean tokenVerified = csrfTokenManager.isCsrfTokenValid(request, true);
			Assert.assertTrue(tokenVerified);

			boolean anotherTokenVerified = csrfTokenManager.isCsrfTokenValid(request, true);
			Assert.assertTrue(anotherTokenVerified);

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_validateCsrfToken_noSession() {
		try {
			initCsrfToken();

			MockHttpServletRequest noSessionRequest = mockWebContext.createMockHttpServletRequest();
			noSessionRequest.addParameter(ServletUtils.PARAM_CSRF_TOKEN, MockCsrfTokenManager.MOCK_CSRF_TOKEN);

			boolean tokenVerified = csrfTokenManager.isCsrfTokenValid(noSessionRequest, false);
			Assert.assertFalse(tokenVerified);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}
}
