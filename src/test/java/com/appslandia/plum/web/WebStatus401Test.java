package com.appslandia.plum.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.appslandia.plum.base.Authorize;
import com.appslandia.plum.base.GET;
import com.appslandia.plum.base.Path;
import com.appslandia.plum.mocks.MockHttpServletRequest;
import com.appslandia.plum.mocks.MockHttpServletResponse;
import com.appslandia.plum.mocks.MockUser;
import com.appslandia.plum.mocks.MockUserInfoManager;
import com.appslandia.plum.mocks.MockWebContext;

public class WebStatus401Test {

	MockWebContext mockWebContext;
	MockUserInfoManager mockUserInfoManager;

	MockHttpServletRequest request;
	MockHttpServletResponse response;

	@Before
	public void initialize() {
		mockWebContext = new MockWebContext();
		mockWebContext.register(TestController.class);

		mockUserInfoManager = mockWebContext.getObject(MockUserInfoManager.class);
		mockUserInfoManager.addMockUser(new MockUser().setUserName("user").setPassword("user").setUserRoles("user"));

		request = mockWebContext.createMockHttpServletRequest();
		response = mockWebContext.createMockHttpServletResponse();
	}

	@Test
	public void test_testAction_unAuthenticated() {
		request.setRequestURI("/testController/testAction.html");
		try {
			mockWebContext.execute(request, response);

			Assert.assertEquals(response.getStatus(), 302);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_testAction_authenticated() {
		request.setRequestURI("/testController/testAction.html");
		request.login("user");

		try {
			mockWebContext.execute(request, response);

			Assert.assertEquals(response.getStatus(), 200);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Path("testController")
	public static class TestController {

		@GET
		@Authorize
		public void testAction(HttpServletRequest request, HttpServletResponse response) throws Exception {
		}
	}
}
