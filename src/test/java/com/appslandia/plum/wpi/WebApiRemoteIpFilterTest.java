package com.appslandia.plum.wpi;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.appslandia.plum.base.GET;
import com.appslandia.plum.base.Path;
import com.appslandia.plum.base.RemoteIpFilter;
import com.appslandia.plum.mocks.MockHttpServletRequest;
import com.appslandia.plum.mocks.MockHttpServletResponse;
import com.appslandia.plum.mocks.MockWebContext;

public class WebApiRemoteIpFilterTest {

	MockWebContext mockWebContext;
	RemoteIpFilter remoteIpFilter;

	MockHttpServletRequest request;
	MockHttpServletResponse response;

	@Before
	public void initialize() {
		mockWebContext = new MockWebContext().useWebApiVersion();
		mockWebContext.register(TestController.class);
		remoteIpFilter = mockWebContext.getObject(RemoteIpFilter.class);

		request = mockWebContext.createMockHttpServletRequest();
		response = mockWebContext.createMockHttpServletResponse();
	}

	@Test
	public void test_testAction_setDenies_403() {
		request.setRequestURI("/testController/testAction.api");
		remoteIpFilter.setDenies("192\\.168\\.10\\.\\d+");
		request.setRemoteAddr("192.168.10.1");

		try {
			mockWebContext.execute(request, response);

			Assert.assertEquals(response.getStatus(), 403);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

	}

	@Test
	public void test_testAction_setDenies_200() {
		request.setRequestURI("/testController/testAction.api");
		remoteIpFilter.setDenies("192\\.168\\.10\\.\\d+");
		request.setRemoteAddr("192.168.11.1");

		try {
			mockWebContext.execute(request, response);

			Assert.assertEquals(response.getStatus(), 200);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_testAction_setAllows_403() {
		request.setRequestURI("/testController/testAction.api");
		remoteIpFilter.setAllows("192\\.168\\.10\\.\\d+");
		request.setRemoteAddr("192.168.11.1");

		try {
			mockWebContext.execute(request, response);

			Assert.assertEquals(response.getStatus(), 403);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_testAction_setAllows_200() {
		request.setRequestURI("/testController/testAction.api");
		remoteIpFilter.setAllows("192\\.168\\.10\\.\\d+");
		request.setRemoteAddr("192.168.10.1");

		try {
			mockWebContext.execute(request, response);

			Assert.assertEquals(response.getStatus(), 200);
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
	}
}
