package com.appslandia.plum.base;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.appslandia.plum.mocks.MockHttpServletRequest;
import com.appslandia.plum.mocks.MockServletContext;
import com.appslandia.plum.mocks.MockSessionCookieConfig;

public class RequestWrapperTest {

	MockServletContext servletContext;
	MockHttpServletRequest request;

	@Before
	public void initialize() {
		servletContext = new MockServletContext(new MockSessionCookieConfig());
		request = new MockHttpServletRequest(servletContext);
	}

	@Test
	public void test() {
		request.addParameter("p1", "v1");
		request.addParameter("p2", "v2");
		request.addParameter("p3", "v31", "v32");

		Map<String, String> pathParamMap = new HashMap<>();
		pathParamMap.put("p2", "v22");
		pathParamMap.put("p4", "v4");

		RequestWrapper requestWrapper = new RequestWrapper(request, pathParamMap);

		try {
			Assert.assertEquals(requestWrapper.getParameter("p1"), "v1");
			Assert.assertEquals(requestWrapper.getParameter("p4"), "v4");

			// p2 is path parameter
			// value orders
			Assert.assertEquals(requestWrapper.getParameterValues("p2")[0], "v22");
			Assert.assertEquals(requestWrapper.getParameterValues("p2")[1], "v2");

			Assert.assertEquals(requestWrapper.getParameterValues("p3")[0], "v31");
			Assert.assertEquals(requestWrapper.getParameterValues("p3")[1], "v32");

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

}
