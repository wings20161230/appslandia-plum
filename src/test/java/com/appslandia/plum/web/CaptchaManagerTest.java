package com.appslandia.plum.web;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.appslandia.plum.base.ServletUtils;
import com.appslandia.plum.mocks.MockCaptchaManager;
import com.appslandia.plum.mocks.MockHttpServletRequest;
import com.appslandia.plum.mocks.MockHttpSession;
import com.appslandia.plum.mocks.MockWebContext;

public class CaptchaManagerTest {

	MockWebContext mockWebContext;
	CaptchaManager captchaManager;

	MockHttpSession session;
	MockHttpServletRequest request;

	@Before
	public void initialize() {
		mockWebContext = new MockWebContext();
		captchaManager = mockWebContext.getObject(CaptchaManager.class);

		session = mockWebContext.createMockHttpSession();
		request = mockWebContext.createMockHttpServletRequest(session);
	}

	private void initCaptcha() throws Exception {
		captchaManager.initCaptcha(mockWebContext.createMockHttpServletRequest(session));
	}

	@Test
	public void test_initCaptcha() {
		try {
			captchaManager.initCaptcha(request);

			// captchaId
			String captchaId = (String) request.getAttribute(ServletUtils.REQUEST_ATTRIBUTE_CAPTCHA_ID);
			Assert.assertNotNull(captchaId);

			// getCaptchaWords
			String captchaWords = captchaManager.getCaptchaWords(request, captchaId);
			Assert.assertNotNull(captchaWords);

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_validateCaptcha() {
		try {
			initCaptcha();

			request.addParameter(ServletUtils.PARAM_CAPTCHA_ID, MockCaptchaManager.MOCK_CAPTCHA_ID);
			request.addParameter(ServletUtils.PARAM_CAPTCHA_WORDS, MockCaptchaManager.MOCK_CAPTCHA_WORDS);

			boolean captchaVerified = captchaManager.isCaptchaValid(request);

			Assert.assertTrue(captchaVerified);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_validateCaptcha_invalid() {
		try {
			initCaptcha();

			request.addParameter(ServletUtils.PARAM_CAPTCHA_ID, MockCaptchaManager.MOCK_CAPTCHA_ID);
			request.addParameter(ServletUtils.PARAM_CAPTCHA_WORDS, "invalid");

			boolean captchaVerified = captchaManager.isCaptchaValid(request);
			Assert.assertFalse(captchaVerified);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_validateCaptcha_noSession() {
		try {
			initCaptcha();

			MockHttpServletRequest noSessionRequest = mockWebContext.createMockHttpServletRequest();
			noSessionRequest.addParameter(ServletUtils.PARAM_CAPTCHA_ID, MockCaptchaManager.MOCK_CAPTCHA_ID);
			noSessionRequest.addParameter(ServletUtils.PARAM_CAPTCHA_WORDS, MockCaptchaManager.MOCK_CAPTCHA_WORDS);

			boolean captchaVerified = captchaManager.isCaptchaValid(noSessionRequest);
			Assert.assertFalse(captchaVerified);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}
}
