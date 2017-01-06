package com.appslandia.plum.base;

import java.sql.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.appslandia.common.formatters.Formatter;
import com.appslandia.common.formatters.FormatterId;
import com.appslandia.common.validators.BitMask;
import com.appslandia.common.validators.MaxLength;
import com.appslandia.plum.mocks.MockHttpServletRequest;
import com.appslandia.plum.mocks.MockHttpServletResponse;
import com.appslandia.plum.mocks.MockWebContext;

public class ModelBinderTest {

	MockWebContext mockWebContext;

	MockHttpServletRequest request;
	MockHttpServletResponse response;
	ModelState modelState;

	@Before
	public void initialize() {
		mockWebContext = new MockWebContext();
		mockWebContext.register(TestController.class);

		request = mockWebContext.createMockHttpServletRequest();
		response = mockWebContext.createMockHttpServletResponse();
		modelState = ServletUtils.getModelState(request);
	}

	@Test
	public void test_testUserModel() {
		request.setRequestURI("/testController/testUserModel.html");

		try {
			mockWebContext.execute(request, response);
			UserModel userModel = (UserModel) request.getAttribute("__model");

			Assert.assertNotNull(userModel);

			Assert.assertEquals(userModel.userId, 0);
			Assert.assertNull(userModel.name);
			Assert.assertNull(userModel.dob);
			Assert.assertEquals(userModel.permissions, 0);

			Assert.assertTrue(modelState.containsKey("name"));

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_testUserModel_userId() {
		request.setRequestURI("/testController/testUserModel.html");

		try {
			request.addParameter("userId", "100");
			mockWebContext.execute(request, response);

			UserModel userModel = (UserModel) request.getAttribute("__model");

			Assert.assertEquals(userModel.userId, 100);
			Assert.assertFalse(modelState.containsKey("userId"));

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		// Test another
		modelState.clear();
		request.clearParameters();
		try {
			request.addParameter("userId", "100_xyz");
			mockWebContext.execute(request, response);

			UserModel userModel = (UserModel) request.getAttribute("__model");

			Assert.assertEquals(userModel.userId, 0);
			Assert.assertTrue(modelState.containsKey("userId"));

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_testUserModel_name() {
		request.setRequestURI("/testController/testUserModel.html");

		try {
			request.addParameter("name", "testName");
			mockWebContext.execute(request, response);

			UserModel userModel = (UserModel) request.getAttribute("__model");

			Assert.assertEquals(userModel.name, "testName");
			Assert.assertFalse(modelState.containsKey("name"));

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		// Test another
		modelState.clear();
		request.clearParameters();
		try {
			request.addParameter("name", "testVeryLongName");
			mockWebContext.execute(request, response);

			UserModel userModel = (UserModel) request.getAttribute("__model");

			Assert.assertEquals(userModel.name, "testVeryLongName");
			Assert.assertTrue(modelState.containsKey("name"));

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_testUserModel_dob() {
		request.setRequestURI("/testController/testUserModel.html");

		try {
			request.addParameter("dob", "10/10/2010");
			mockWebContext.execute(request, response);

			UserModel userModel = (UserModel) request.getAttribute("__model");

			Assert.assertNotNull(userModel.dob);
			Assert.assertFalse(modelState.containsKey("dob"));

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		// Test another
		modelState.clear();
		request.clearParameters();
		try {
			request.addParameter("dob", "10//2010");
			mockWebContext.execute(request, response);

			UserModel userModel = (UserModel) request.getAttribute("__model");

			Assert.assertNull(userModel.dob);
			Assert.assertTrue(modelState.containsKey("dob"));

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_testUserModel_permissions() {
		request.setRequestURI("/testController/testUserModel.html");

		try {
			// 1, 4
			request.addParameter("permissions", "1", "4");
			mockWebContext.execute(request, response);

			UserModel userModel = (UserModel) request.getAttribute("__model");

			// 5=1+4
			Assert.assertEquals(userModel.permissions, 5);
			Assert.assertFalse(modelState.containsKey("permissions"));

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		// Test another
		modelState.clear();
		request.clearParameters();
		try {
			request.addParameter("permissions", "1", "2", "4x");
			mockWebContext.execute(request, response);

			UserModel userModel = (UserModel) request.getAttribute("__model");

			// 3=1+2
			Assert.assertEquals(userModel.permissions, 3);
			Assert.assertTrue(modelState.containsKey("permissions"));

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		// Test another
		modelState.clear();
		request.clearParameters();
		try {
			request.addParameter("permissions", "1", "2", "8");
			mockWebContext.execute(request, response);

			UserModel userModel = (UserModel) request.getAttribute("__model");

			Assert.assertEquals(userModel.permissions, 11);
			Assert.assertTrue(modelState.containsKey("permissions"));

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_testUserModel_manager() {
		request.setRequestURI("/testController/testUserModel.html");
		try {
			request.addParameter("userId", "1");
			request.addParameter("name", "user1");

			request.addParameter("manager.userId", "100");
			request.addParameter("manager.name", "manager");

			mockWebContext.execute(request, response);

			UserModel userModel = (UserModel) request.getAttribute("__model");
			Assert.assertNotNull(userModel.manager);
			Assert.assertEquals(userModel.manager.userId, 100);
			Assert.assertEquals(userModel.manager.name, "manager");

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		// Test another
		modelState.clear();
		request.clearParameters();

		try {
			request.addParameter("userId", "1");
			request.addParameter("name", "user1");
			request.addParameter("manager.userId", "100");

			mockWebContext.execute(request, response);

			UserModel userModel = (UserModel) request.getAttribute("__model");
			Assert.assertNotNull(userModel.manager);
			Assert.assertEquals(userModel.manager.userId, 100);

			Assert.assertTrue(modelState.containsKey("manager.name"));

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_testListModel() {
		request.setRequestURI("/testController/testUserList.html");

		try {
			mockWebContext.execute(request, response);
			UserList userList = (UserList) request.getAttribute("__model");

			Assert.assertNull(userList.users);
			Assert.assertTrue(modelState.containsKey("users"));

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_testListModel_users() {
		request.setRequestURI("/testController/testUserList.html");

		request.addParameter("users[1].userId", "1");
		request.addParameter("users[1].name", "user1");

		request.addParameter("users[2].userId", "2");
		request.addParameter("users[2].name", "user2");

		try {
			mockWebContext.execute(request, response);
			UserList userList = (UserList) request.getAttribute("__model");

			Assert.assertNotNull(userList.users);
			Assert.assertTrue(modelState.containsKey("users") == false);

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Path("testController")
	public static class TestController {

		@GET
		public void testUserModel(@FormModel UserModel model, ModelState modelState, HttpServletRequest request) throws Exception {
			request.setAttribute("__model", model);
		}

		@GET
		public void testUserList(@FormModel UserList model, ModelState modelState, HttpServletRequest request) throws Exception {
			request.setAttribute("__model", model);
		}
	}

	public static class UserModel {

		protected int userId;

		@NotNull
		@MaxLength(10)
		protected String name;

		@FormatterId(Formatter.SQL_DATE)
		protected Date dob;

		// 1|2|4
		@BitMask(3)
		protected int permissions;

		@Valid
		protected UserModel manager;

		public int getUserId() {
			return userId;
		}

		public void setUserId(int userId) {
			this.userId = userId;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Date getDob() {
			return dob;
		}

		public void setDob(Date dob) {
			this.dob = dob;
		}

		public int getPermissions() {
			return permissions;
		}

		public void setPermissions(int permissions) {
			this.permissions = permissions;
		}

		public UserModel getManager() {
			return manager;
		}

		public void setManager(UserModel manager) {
			this.manager = manager;
		}
	}

	public static class UserList {

		@NotNull
		protected List<UserModel> users;

		public List<UserModel> getUsers() {
			return users;
		}

		public void setUsers(List<UserModel> users) {
			this.users = users;
		}
	}
}
