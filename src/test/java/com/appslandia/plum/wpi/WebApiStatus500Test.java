package com.appslandia.plum.wpi;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.appslandia.plum.base.GET;
import com.appslandia.plum.base.Path;
import com.appslandia.plum.mocks.MockHttpServletRequest;
import com.appslandia.plum.mocks.MockHttpServletResponse;
import com.appslandia.plum.mocks.MockWebContext;

public class WebApiStatus500Test {

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
	public void test_testActionException() {
		request.setRequestURI("/testController/testActionException.api");
		try {
			mockWebContext.execute(request, response);

			Assert.assertEquals(response.getStatus(), 500);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_testActionRuntimeException() {
		request.setRequestURI("/testController/testActionRuntimeException.api");
		try {
			mockWebContext.execute(request, response);

			Assert.assertEquals(response.getStatus(), 500);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Path("testController")
	@WebApiVersion
	public static class TestController {

		@GET
		public void testActionException(HttpServletRequest request, HttpServletResponse response) throws Exception {
			throw new Exception();
		}

		@GET
		public void testActionRuntimeException(HttpServletRequest request, HttpServletResponse response) throws Exception {
			throw new RuntimeException();
		}
	}
}
