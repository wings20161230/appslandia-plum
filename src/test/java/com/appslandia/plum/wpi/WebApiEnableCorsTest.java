package com.appslandia.plum.wpi;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.appslandia.plum.base.CorsPolicy;
import com.appslandia.plum.base.CorsPolicyHandler;
import com.appslandia.plum.base.EnableCors;
import com.appslandia.plum.base.EnableHttps;
import com.appslandia.plum.base.GET;
import com.appslandia.plum.base.POST;
import com.appslandia.plum.base.Path;
import com.appslandia.plum.base.ServletUtils;
import com.appslandia.plum.mocks.MockAppConfig;
import com.appslandia.plum.mocks.MockCorsPolicyProvider;
import com.appslandia.plum.mocks.MockHttpServletRequest;
import com.appslandia.plum.mocks.MockHttpServletResponse;
import com.appslandia.plum.mocks.MockWebContext;

public class WebApiEnableCorsTest {

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

	private void initTestPolicy() {
		MockCorsPolicyProvider corsPolicyProvider = mockWebContext.getObject(MockCorsPolicyProvider.class);
		corsPolicyProvider.addCorsPolicy("testPolicy", new CorsPolicy().setAllowDomains(new String[] { "localhost" }));
	}

	private void initDefaultPorts() {
		MockAppConfig appConfig = mockWebContext.getObject(MockAppConfig.class);
		appConfig.put(ServletUtils.CONFIG_HTTP_PORT, "80");
		appConfig.put(ServletUtils.CONFIG_HTTPS_PORT, "443");
	}

	@Test
	public void test_testAction_OPTIONS_production() {
		appConfig.put(ServletUtils.CONFIG_PRODUCTION, String.valueOf(true));

		request.setRequestURI("/testController/testAction.api");
		request.setMethod("OPTIONS");

		try {
			mockWebContext.execute(request, response);

			Assert.assertEquals(response.getStatus(), 405);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_enableCorsAction_matched() {
		initTestPolicy();

		request.setRequestURI("/testController/enableCorsAction.api");
		request.setMethod("OPTIONS");

		// http
		request.setHeader("origin", "http://localhost:8080");
		try {
			mockWebContext.execute(request, response);

			Assert.assertEquals(response.getStatus(), 200);
			Assert.assertNull(response.getHeader(CorsPolicyHandler.HEADER_CORS_ORIGIN_UNMATCHED));
			Assert.assertEquals(response.getHeader(CorsPolicyHandler.HEADER_CORS_ALLOW_ORIGIN), "http://localhost:8080");

			Assert.assertTrue(response.getHeader(CorsPolicyHandler.HEADER_CORS_ALLOW_METHODS).contains("GET"));
			Assert.assertTrue(response.getHeader(CorsPolicyHandler.HEADER_CORS_ALLOW_METHODS).contains("HEAD"));
			Assert.assertTrue(response.getHeader(CorsPolicyHandler.HEADER_CORS_ALLOW_METHODS).contains("POST"));
			Assert.assertTrue(response.getHeader(CorsPolicyHandler.HEADER_CORS_ALLOW_METHODS).contains("OPTIONS"));

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		// HTTPS
		request.setHeader("origin", "https://localhost:8181");
		try {
			mockWebContext.execute(request, response);

			Assert.assertEquals(response.getStatus(), 200);
			Assert.assertNull(response.getHeader(CorsPolicyHandler.HEADER_CORS_ORIGIN_UNMATCHED));
			Assert.assertEquals(response.getHeader(CorsPolicyHandler.HEADER_CORS_ALLOW_ORIGIN), "https://localhost:8181");

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_enableCorsAction_unmatched() {
		initTestPolicy();

		request.setRequestURI("/testController/enableCorsAction.api");
		request.setMethod("OPTIONS");

		// Unmatch host
		request.setHeader("origin", "http://invalidHost:8080");
		try {
			mockWebContext.execute(request, response);

			Assert.assertEquals(response.getStatus(), 200);
			Assert.assertEquals(response.getHeader(CorsPolicyHandler.HEADER_CORS_ORIGIN_UNMATCHED), "true");
			Assert.assertEquals(response.getHeader(CorsPolicyHandler.HEADER_CORS_ALLOW_ORIGIN), "http://localhost:8080");

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		// Unmatch host
		request.setHeader("origin", "https://invalidHost:8181");
		try {
			mockWebContext.execute(request, response);

			Assert.assertEquals(response.getStatus(), 200);
			Assert.assertEquals(response.getHeader(CorsPolicyHandler.HEADER_CORS_ORIGIN_UNMATCHED), "true");
			Assert.assertEquals(response.getHeader(CorsPolicyHandler.HEADER_CORS_ALLOW_ORIGIN), "https://localhost:8181");

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		// Unmatch port
		request.setHeader("origin", "http://localhost:8081");
		try {
			mockWebContext.execute(request, response);

			Assert.assertEquals(response.getStatus(), 200);
			Assert.assertEquals(response.getHeader(CorsPolicyHandler.HEADER_CORS_ORIGIN_UNMATCHED), "true");
			Assert.assertEquals(response.getHeader(CorsPolicyHandler.HEADER_CORS_ALLOW_ORIGIN), "http://localhost:8080");

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		// Unmatch port
		request.setHeader("origin", "https://localhost:8182");
		try {
			mockWebContext.execute(request, response);

			Assert.assertEquals(response.getStatus(), 200);
			Assert.assertEquals(response.getHeader(CorsPolicyHandler.HEADER_CORS_ORIGIN_UNMATCHED), "true");
			Assert.assertEquals(response.getHeader(CorsPolicyHandler.HEADER_CORS_ALLOW_ORIGIN), "https://localhost:8181");

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_enableCorsHttpsAction_matched() {
		initTestPolicy();

		request.setRequestURI("/testController/enableCorsHttpsAction.api");
		request.setMethod("OPTIONS");
		request.setHeader("origin", "https://localhost:8181");

		try {
			mockWebContext.execute(request, response);

			Assert.assertEquals(response.getStatus(), 200);
			Assert.assertNull(response.getHeader(CorsPolicyHandler.HEADER_CORS_ORIGIN_UNMATCHED));
			Assert.assertEquals(response.getHeader(CorsPolicyHandler.HEADER_CORS_ALLOW_ORIGIN), "https://localhost:8181");

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_enableCorsHttpsAction_unmatched() {
		initTestPolicy();

		request.setRequestURI("/testController/enableCorsHttpsAction.api");
		request.setMethod("OPTIONS");

		// Unmatched scheme
		request.setHeader("origin", "http://localhost:8080");
		try {
			mockWebContext.execute(request, response);

			Assert.assertEquals(response.getStatus(), 200);
			Assert.assertEquals(response.getHeader(CorsPolicyHandler.HEADER_CORS_ORIGIN_UNMATCHED), "true");
			Assert.assertEquals(response.getHeader(CorsPolicyHandler.HEADER_CORS_ALLOW_ORIGIN), "https://localhost:8181");

		} catch (Exception ex) {
			ex.printStackTrace();
			Assert.fail(ex.getMessage());
		}

		// Unmatched host
		request.setHeader("origin", "https://invalidHost:8181");
		try {
			mockWebContext.execute(request, response);

			Assert.assertEquals(response.getStatus(), 200);
			Assert.assertEquals(response.getHeader(CorsPolicyHandler.HEADER_CORS_ORIGIN_UNMATCHED), "true");
			Assert.assertEquals(response.getHeader(CorsPolicyHandler.HEADER_CORS_ALLOW_ORIGIN), "https://localhost:8181");

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		// Unmatched port
		request.setHeader("origin", "https://localhost:8182");
		try {
			mockWebContext.execute(request, response);

			Assert.assertEquals(response.getStatus(), 200);
			Assert.assertEquals(response.getHeader(CorsPolicyHandler.HEADER_CORS_ORIGIN_UNMATCHED), "true");
			Assert.assertEquals(response.getHeader(CorsPolicyHandler.HEADER_CORS_ALLOW_ORIGIN), "https://localhost:8181");

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_enableCorsAction_defaultPort_matched() {
		initTestPolicy();
		initDefaultPorts();

		request.setRequestURI("/testController/enableCorsAction.api");
		request.setMethod("OPTIONS");

		// http
		request.setHeader("origin", "http://localhost");
		try {
			mockWebContext.execute(request, response);

			Assert.assertEquals(response.getStatus(), 200);
			Assert.assertEquals(response.getHeader(CorsPolicyHandler.HEADER_CORS_ALLOW_ORIGIN), "http://localhost");

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_enableCorsAction_defaultPort_unmatched() {
		initTestPolicy();
		initDefaultPorts();

		request.setRequestURI("/testController/enableCorsAction.api");
		request.setMethod("OPTIONS");

		// Unmatched host - HTTP
		request.setHeader("origin", "http://invalidHost");
		try {
			mockWebContext.execute(request, response);

			Assert.assertEquals(response.getStatus(), 200);
			Assert.assertEquals(response.getHeader(CorsPolicyHandler.HEADER_CORS_ORIGIN_UNMATCHED), "true");
			Assert.assertEquals(response.getHeader(CorsPolicyHandler.HEADER_CORS_ALLOW_ORIGIN), "http://localhost");

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		// Unmatched host - HTTPS
		request.setHeader("origin", "https://invalidHost");
		try {
			mockWebContext.execute(request, response);

			Assert.assertEquals(response.getStatus(), 200);
			Assert.assertEquals(response.getHeader(CorsPolicyHandler.HEADER_CORS_ORIGIN_UNMATCHED), "true");
			Assert.assertEquals(response.getHeader(CorsPolicyHandler.HEADER_CORS_ALLOW_ORIGIN), "https://localhost");

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
		@POST
		@EnableCors("testPolicy")
		public void enableCorsAction(HttpServletRequest request, HttpServletResponse response) throws Exception {
		}

		@GET
		@EnableHttps
		@EnableCors("testPolicy")
		public void enableCorsHttpsAction(HttpServletRequest request, HttpServletResponse response) throws Exception {
		}
	}
}
