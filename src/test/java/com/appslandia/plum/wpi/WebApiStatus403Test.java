package com.appslandia.plum.wpi;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.appslandia.common.utils.CollectionUtils;
import com.appslandia.plum.base.AuthTypes;
import com.appslandia.plum.base.Authorize;
import com.appslandia.plum.base.ForbiddenException;
import com.appslandia.plum.base.GET;
import com.appslandia.plum.base.Path;
import com.appslandia.plum.base.ServletUtils;
import com.appslandia.plum.base.UserData;
import com.appslandia.plum.mocks.MockHttpServletRequest;
import com.appslandia.plum.mocks.MockHttpServletResponse;
import com.appslandia.plum.mocks.MockUserData;
import com.appslandia.plum.mocks.MockWebContext;

public class WebApiStatus403Test {

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
	public void test_testAction_unAuthenticated() {
		request.setRequestURI("/testController/testAction.api");

		try {
			mockWebContext.execute(request, response);

			Assert.assertEquals(response.getStatus(), 401);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_testAction_unAuthorized() {
		request.setRequestURI("/testController/testAction.api");

		UserData userData = new MockUserData(1, AuthTypes.APP, CollectionUtils.unmodifiableSet("user"));
		request.setAttribute(ServletUtils.ATTRIBUTE_USER_DATA, userData);

		try {
			mockWebContext.execute(request, response);

			Assert.assertEquals(response.getStatus(), 403);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_testAction_authorized() {
		request.setRequestURI("/testController/testAction.api");

		UserData userData = new MockUserData(1, AuthTypes.APP, CollectionUtils.unmodifiableSet("manager"));
		request.setAttribute(ServletUtils.ATTRIBUTE_USER_DATA, userData);

		try {
			mockWebContext.execute(request, response);

			Assert.assertEquals(response.getStatus(), 200);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_test403Exception_unAuthorized() {
		request.setRequestURI("/testController/test403Exception.api");

		UserData userData = new MockUserData(1, AuthTypes.APP, CollectionUtils.unmodifiableSet("user"));
		request.setAttribute(ServletUtils.ATTRIBUTE_USER_DATA, userData);

		try {
			mockWebContext.execute(request, response);

			Assert.assertEquals(response.getStatus(), 403);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Path("testController")
	@WebApiVersion
	public static class TestController {

		@GET
		@Authorize(userRoles = { "manager" })
		public void testAction(HttpServletRequest request, HttpServletResponse response) throws Exception {
		}

		@GET
		public void test403Exception(HttpServletRequest request, HttpServletResponse response) throws Exception {
			if (request.isUserInRole("manager") == false) {
				throw new ForbiddenException("manager required");
			}
		}
	}
}
