package com.appslandia.plum.wpi;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.appslandia.plum.base.EnableHttps;
import com.appslandia.plum.base.GET;
import com.appslandia.plum.base.POST;
import com.appslandia.plum.base.Path;
import com.appslandia.plum.mocks.MockHttpServletRequest;
import com.appslandia.plum.mocks.MockHttpServletResponse;
import com.appslandia.plum.mocks.MockWebContext;

public class WebApiEnableHttpsTest {

	MockWebContext mockWebContext;

	MockHttpServletRequest request;
	MockHttpServletResponse response;

	@Before
	public void initialize() {
		mockWebContext = new MockWebContext().useWebApiVersion();
		mockWebContext.register(TestController.class);

		request = mockWebContext.createMockHttpServletRequest();
		response = mockWebContext.createMockHttpServletResponse();
	}

	@Test
	public void test_testSecuredAction_http() {
		request.setMethod("GET");
		request.setScheme("http");
		request.setRequestURI("/testController/testSecuredAction.api");

		try {
			mockWebContext.execute(request, response);

			Assert.assertEquals(response.getStatus(), 302);

			String location = response.getHeader("Location");
			Assert.assertNotNull(location);
			Assert.assertTrue(location.contains("/testController/testSecuredAction.api"));

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_testSecuredAction_https() {
		request.setMethod("GET");
		request.setScheme("https");
		request.setRequestURI("/testController/testSecuredAction.api");

		try {
			mockWebContext.execute(request, response);

			Assert.assertEquals(response.getStatus(), 200);

			String location = response.getHeader("Location");
			Assert.assertNull(location);

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_testSecuredAction_http_POST() {
		request.setMethod("POST");
		request.setScheme("http");
		request.setRequestURI("/testController/testSecuredAction.api");

		try {
			mockWebContext.execute(request, response);

			Assert.assertEquals(response.getStatus(), 400);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Path("testController")
	@WebApiVersion
	public static class TestController {

		@GET
		@POST
		@EnableHttps
		public void testSecuredAction(HttpServletRequest request, HttpServletResponse response) throws Exception {
		}
	}
}
