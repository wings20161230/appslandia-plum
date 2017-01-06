package com.appslandia.plum.wpi;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.appslandia.plum.base.DisableGzip;
import com.appslandia.plum.base.GET;
import com.appslandia.plum.base.Path;
import com.appslandia.plum.mocks.MockHttpServletRequest;
import com.appslandia.plum.mocks.MockHttpServletResponse;
import com.appslandia.plum.mocks.MockWebContext;

public class WebApiGzipTest {

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
	public void test_testAction_gzip() {
		request.setRequestURI("/testController/testAction.api");
		request.setAcceptEncoding("gzip");

		try {
			mockWebContext.execute(request, response);

			Assert.assertEquals(response.getStatus(), 200);
			Assert.assertEquals(response.getHeader("Content-Encoding"), "gzip");

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_testAction_noGzip() {
		request.setRequestURI("/testController/testAction.api");

		try {
			mockWebContext.execute(request, response);

			Assert.assertEquals(response.getStatus(), 200);
			Assert.assertNull(response.getHeader("Content-Encoding"));

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_disableGzipAction() {
		request.setRequestURI("/testController/disableGzipAction.api");
		request.setAcceptEncoding("gzip");

		try {
			mockWebContext.execute(request, response);

			Assert.assertEquals(response.getStatus(), 200);
			Assert.assertNotEquals(response.getHeader("Content-Encoding"), "gzip");
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

		@GET
		@DisableGzip
		public void disableGzipAction(HttpServletRequest request, HttpServletResponse response) throws Exception {
		}
	}
}
