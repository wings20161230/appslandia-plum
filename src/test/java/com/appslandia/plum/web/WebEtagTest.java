package com.appslandia.plum.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.appslandia.plum.base.EnableEtag;
import com.appslandia.plum.base.GET;
import com.appslandia.plum.base.Path;
import com.appslandia.plum.mocks.MockHttpServletRequest;
import com.appslandia.plum.mocks.MockHttpServletResponse;
import com.appslandia.plum.mocks.MockWebContext;
import com.appslandia.plum.results.ContentResult;

public class WebEtagTest {

	MockWebContext mockWebContext;

	MockHttpServletRequest request;
	MockHttpServletResponse response;

	@Before
	public void initialize() {
		mockWebContext = new MockWebContext();
		mockWebContext.register(TestController.class);

		request = mockWebContext.createMockHttpServletRequest();
		response = mockWebContext.createMockHttpServletResponse();
	}

	@Test
	public void test_testAction() {
		request.setRequestURI("/testController/testAction.html");
		request.setAcceptEncoding("gzip");

		String etag = null;
		try {
			mockWebContext.execute(request, response);

			Assert.assertEquals(response.getStatus(), 200);
			Assert.assertEquals(response.getHeader("Content-Encoding"), "gzip");
			Assert.assertNotNull(response.getHeader("Etag"));

			etag = response.getHeader("Etag");
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		// Later requests
		MockHttpServletRequest laterRequest = mockWebContext.createMockHttpServletRequest();
		MockHttpServletResponse laterResponse = mockWebContext.createMockHttpServletResponse();

		laterRequest.setRequestURI("/testController/testAction.html");
		laterRequest.setAcceptEncoding("gzip");
		laterRequest.setHeader("If-None-Match", etag);

		try {
			mockWebContext.execute(laterRequest, laterResponse);

			Assert.assertEquals(laterResponse.getStatus(), HttpServletResponse.SC_NOT_MODIFIED);
			Assert.assertNotNull(laterResponse.getHeader("Etag"));

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Path("testController")
	public static class TestController {

		@GET
		@EnableEtag
		public ContentResult testAction(HttpServletRequest request, HttpServletResponse response) throws Exception {
			String content = "<html><head><title>testAction</title></head><body><h2>testAction</h2></body></html>";
			return new ContentResult(content, "text/html");
		}
	}
}
