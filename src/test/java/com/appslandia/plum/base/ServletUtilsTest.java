package com.appslandia.plum.base;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.appslandia.plum.mocks.MockHttpServletRequest;
import com.appslandia.plum.mocks.MockHttpServletResponse;
import com.appslandia.plum.mocks.MockServletContext;
import com.appslandia.plum.mocks.MockServletUtils;
import com.appslandia.plum.mocks.MockSessionCookieConfig;

public class ServletUtilsTest {

	MockServletContext servletContext;

	@Before
	public void initialize() {
		servletContext = new MockServletContext(new MockSessionCookieConfig());
	}

	@Test
	public void test_getXForwardFor() {
		MockHttpServletRequest request = new MockHttpServletRequest(servletContext);

		request.setHeaderValues("X-Forwarded-For", "192.168.0.2");
		Assert.assertEquals(ServletUtils.getRemoteIp(request), "192.168.0.2");
	}

	@Test
	public void test_checkNotModified_lastModified() {
		MockHttpServletRequest request = new MockHttpServletRequest(servletContext);
		MockHttpServletResponse response = new MockHttpServletResponse(servletContext);

		long ifModifiedSince = MockServletUtils.noMilliseconds(System.currentTimeMillis());
		request.setHeaderValues("If-Modified-Since", ifModifiedSince);

		long lastModified = ifModifiedSince;
		Boolean notModified = ServletUtils.checkNotModified(request, response, lastModified);
		Assert.assertTrue(notModified);
		Assert.assertEquals(response.getDateHeader("Last-Modified"), lastModified);
		Assert.assertEquals(response.getStatus(), 304);
	}

	@Test
	public void test_checkNotModified_lastModified_modified() {
		MockHttpServletRequest request = new MockHttpServletRequest(servletContext);
		MockHttpServletResponse response = new MockHttpServletResponse(servletContext);

		long ifModifiedSince = MockServletUtils.noMilliseconds(System.currentTimeMillis());
		request.setHeaderValues("If-Modified-Since", ifModifiedSince);

		long lastModified = ifModifiedSince + 10000;
		Boolean notModified = ServletUtils.checkNotModified(request, response, lastModified);
		Assert.assertFalse(notModified);

		Assert.assertEquals(response.getDateHeader("Last-Modified"), lastModified);
		Assert.assertEquals(response.getStatus(), 200);
	}

	@Test
	public void test_checkNotModified_etag() {
		MockHttpServletRequest request = new MockHttpServletRequest(servletContext);
		MockHttpServletResponse response = new MockHttpServletResponse(servletContext);

		String ifNoneMatch = "etag1";
		request.setHeaderValues("If-None-Match", ifNoneMatch);

		String etag = "etag1";
		Boolean notModified = ServletUtils.checkNotModified(request, response, etag);
		Assert.assertTrue(notModified);
		Assert.assertEquals(response.getHeader("ETag"), etag);
		Assert.assertEquals(response.getStatus(), 304);
	}

	@Test
	public void test_checkNotModified_etag_modified() {
		MockHttpServletRequest request = new MockHttpServletRequest(servletContext);
		MockHttpServletResponse response = new MockHttpServletResponse(servletContext);

		String ifNoneMatch = "etag1";
		request.setHeaderValues("If-None-Match", ifNoneMatch);

		String etag = "etag2";
		Boolean notModified = ServletUtils.checkNotModified(request, response, etag);
		Assert.assertFalse(notModified);
		Assert.assertEquals(response.getHeader("ETag"), etag);
		Assert.assertEquals(response.getStatus(), 200);
	}
}
