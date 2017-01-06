package com.appslandia.plum.wpi;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.appslandia.plum.base.GET;
import com.appslandia.plum.base.POST;
import com.appslandia.plum.base.Path;
import com.appslandia.plum.base.ServletUtils;
import com.appslandia.plum.mocks.MockAppConfig;
import com.appslandia.plum.mocks.MockHttpServletRequest;
import com.appslandia.plum.mocks.MockHttpServletResponse;
import com.appslandia.plum.mocks.MockWebContext;

public class WebApiStatus405Test {

	MockWebContext mockWebContext;
	MockAppConfig appConfig;

	MockHttpServletRequest request;
	MockHttpServletResponse response;

	@Before
	public void initialize() {
		mockWebContext = new MockWebContext().useWebApiVersion();
		mockWebContext.register(TestController.class);
		appConfig = mockWebContext.getObject(MockAppConfig.class);

		request = mockWebContext.createMockHttpServletRequest();
		response = mockWebContext.createMockHttpServletResponse();
	}

	@Test
	public void test_testAction_MethodNotHandled() {
		request.setMethod("POST");
		request.setRequestURI("/testController/testAction.api");

		try {
			mockWebContext.execute(request, response);

			Assert.assertEquals(response.getStatus(), 405);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_testAction_OPTIONS_production() {
		appConfig.put(ServletUtils.CONFIG_PRODUCTION, String.valueOf(true));

		request.setMethod("OPTIONS");
		request.setRequestURI("/testController/testAction.api");

		try {
			mockWebContext.execute(request, response);

			Assert.assertEquals(response.getStatus(), 405);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_testAction_TRACE_production() {
		appConfig.put(ServletUtils.CONFIG_PRODUCTION, String.valueOf(true));

		request.setMethod("TRACE");
		request.setRequestURI("/testController/testAction.api");

		try {
			mockWebContext.execute(request, response);

			Assert.assertEquals(response.getStatus(), 405);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_testAction_HEAD() {
		request.setMethod("HEAD");
		request.setRequestURI("/testController/testAction.api");

		try {
			mockWebContext.execute(request, response);

			Assert.assertEquals(response.getStatus(), 200);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_testNotHeadAction_HEAD() {
		request.setMethod("HEAD");
		request.setRequestURI("/testController/testNotHeadAction.api");

		try {
			mockWebContext.execute(request, response);

			Assert.assertEquals(response.getStatus(), 405);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Path("testController")
	@WebApiVersion
	public static class TestController {

		@GET
		public void testAction(HttpServletRequest request, HttpServletResponse response) throws Exception {
		}

		@POST
		public void testNotHeadAction(HttpServletRequest request, HttpServletResponse response) throws Exception {
		}
	}
}
