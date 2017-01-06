package com.appslandia.plum.base;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.appslandia.plum.mocks.MockHttpServletRequest;
import com.appslandia.plum.mocks.MockWebContext;

public class ActionParserUrlTest extends ActionContextTestBase {

	MockWebContext mockWebContext;
	ActionParser actionParser;
	MockHttpServletRequest request;

	@Before
	public void initialize() {
		mockWebContext = new MockWebContext();
		mockWebContext.register(TestController.class);

		actionParser = mockWebContext.getObject(ActionParser.class);
		request = mockWebContext.createMockHttpServletRequest();
	}

	@Test
	public void test_actionNoPathParams() {
		request.setRequestURI("/testController/actionNoPathParams.html");
		try {
			initActionContext(request, mockWebContext);

			String url = actionParser.toActionUrl(request, null, "testController", "actionNoPathParams", null);
			Assert.assertTrue(url.lastIndexOf("/testController/actionNoPathParams.html") > 0);

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_actionPathParams() {
		request.setRequestURI("/testController/actionPathParams/p1/p2.html");
		try {
			initActionContext(request, mockWebContext);

			Map<String, Object> params = new HashMap<>();
			params.put("p1", "p1");
			params.put("p2", "p2");

			String url = actionParser.toActionUrl(request, null, "testController", "actionPathParams", params);
			Assert.assertTrue(url.lastIndexOf("/testController/actionPathParams/p1/p2.html") > 0);

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_actionPathParams_lang() {
		request.setRequestURI("/en/testController/actionPathParams/p1/p2.html");
		try {
			initActionContext(request, mockWebContext);

			Map<String, Object> params = new HashMap<>();
			params.put("p1", "p1");
			params.put("p2", "p2");

			String url = actionParser.toActionUrl(request, null, "testController", "actionPathParams", params);
			Assert.assertTrue(url.lastIndexOf("/en/testController/actionPathParams/p1/p2.html") > 0);

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_actionPathParams_pathParamsMissed() {
		request.setRequestURI("/testController/actionPathParams/p1/p2.html");
		try {
			initActionContext(request, mockWebContext);

			Map<String, Object> params = new HashMap<>();
			params.put("p1", "p1");

			actionParser.toActionUrl(request, null, "testController", "actionPathParams", params);
			Assert.fail();

		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}
	}

	@Test
	public void test_actionInlinePathParams() {
		request.setRequestURI("/testController/actionInlinePathParams/p1-p2.html");
		try {
			initActionContext(request, mockWebContext);

			Map<String, Object> params = new HashMap<>();
			params.put("p1", "p1");
			params.put("p2", "p2");

			String url = actionParser.toActionUrl(request, null, "testController", "actionInlinePathParams", params);
			Assert.assertTrue(url.lastIndexOf("/testController/actionInlinePathParams/p1-p2.html") > 0);

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_actionInlinePathParams_pathParamsMissed() {
		request.setRequestURI("/testController/actionInlinePathParams/p1-p2.html");
		try {
			initActionContext(request, mockWebContext);

			Map<String, Object> params = new HashMap<>();
			params.put("p1", "p1");

			actionParser.toActionUrl(request, null, "testController", "actionInlinePathParams", params);
			Assert.fail();

		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof IllegalArgumentException);
		}
	}

	@Path("testController")
	private static class TestController {

		@GET
		public void actionNoPathParams() {
		}

		@GET
		@PathParams("/{p1}/{p2}")
		public void actionPathParams() {
		}

		@GET
		@PathParams("/{p1}-{p2}")
		public void actionInlinePathParams() {
		}
	}
}
