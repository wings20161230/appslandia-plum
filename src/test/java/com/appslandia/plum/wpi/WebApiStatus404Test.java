package com.appslandia.plum.wpi;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.appslandia.plum.base.GET;
import com.appslandia.plum.base.NotFoundException;
import com.appslandia.plum.base.Path;
import com.appslandia.plum.base.PathParams;
import com.appslandia.plum.base.ServletUtils;
import com.appslandia.plum.mocks.MockHttpServletRequest;
import com.appslandia.plum.mocks.MockHttpServletResponse;
import com.appslandia.plum.mocks.MockWebContext;

public class WebApiStatus404Test {

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
	public void test_notFound() {
		request.setMethod("GET");
		request.setRequestURI("/testController/notAction.api");

		try {
			mockWebContext.execute(request, response);

			Assert.assertEquals(response.getStatus(), 404);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_actionNoPathParams_invalidPathParamsAdded() {
		request.setMethod("GET");
		request.setRequestURI("/testController/actionNoPathParams/v1.api");

		try {
			mockWebContext.execute(request, response);

			Assert.assertEquals(response.getStatus(), 404);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_actionWithPathParams_pathParamsMissed() {
		request.setMethod("GET");
		request.setRequestURI("/testController/actionWithPathParams/v1.api");

		try {
			mockWebContext.execute(request, response);

			Assert.assertEquals(response.getStatus(), 404);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_actionWithPathParams_invalidPathParamsFormat() {
		request.setMethod("GET");
		request.setRequestURI("/testController/actionWithPathParams/v1/v2.api");

		try {
			mockWebContext.execute(request, response);

			Assert.assertEquals(response.getStatus(), 404);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_actionWithPathParams_pathParamsProvided() {
		request.setMethod("GET");
		request.setRequestURI("/testController/actionWithPathParams/v1/v2-v3.api");

		try {
			mockWebContext.execute(request, response);

			HttpServletRequest requestWrapper = (HttpServletRequest) request.getAttribute(ServletUtils.REQUEST_ATTRIBUTE_REQUEST_WRAPPER);

			Assert.assertEquals(requestWrapper.getParameter("p1"), "v1");
			Assert.assertEquals(requestWrapper.getParameter("p2"), "v2");
			Assert.assertEquals(requestWrapper.getParameter("p3"), "v3");

			Assert.assertEquals(response.getStatus(), 200);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_actionNotFoundException() {
		request.setMethod("GET");
		request.setRequestURI("/testController/actionNotFoundException.api");

		try {
			mockWebContext.execute(request, response);

			Assert.assertEquals(response.getStatus(), 404);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Path("testController")
	@WebApiVersion
	public static class TestController {

		@GET
		public void actionNoPathParams(HttpServletRequest request, HttpServletResponse response) throws Exception {
		}

		@GET
		@PathParams("/{p1}/{p2}-{p3}")
		public void actionWithPathParams(HttpServletRequest request, HttpServletResponse response) throws Exception {
		}

		@GET
		public void actionNotFoundException(HttpServletRequest request, HttpServletResponse response) throws Exception {
			throw new NotFoundException();
		}
	}
}
