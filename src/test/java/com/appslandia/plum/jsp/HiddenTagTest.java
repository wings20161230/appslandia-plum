package com.appslandia.plum.jsp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.appslandia.plum.base.ModelState;
import com.appslandia.plum.base.ServletUtils;
import com.appslandia.plum.mocks.MockHttpServletRequest;
import com.appslandia.plum.mocks.MockHttpServletResponse;
import com.appslandia.plum.mocks.MockJspContext;
import com.appslandia.plum.mocks.MockServletContext;
import com.appslandia.plum.mocks.MockSessionCookieConfig;

public class HiddenTagTest {

	MockServletContext servletContext;
	MockJspContext jspContext;

	HiddenTag tag;
	TestModel model;
	ModelState modelState;

	@Before
	public void initialize() {
		servletContext = new MockServletContext(new MockSessionCookieConfig());
		ExpressionEvaluator.setInstance(new ExpressionEvaluatorImpl());

		MockHttpServletRequest request = new MockHttpServletRequest(servletContext);
		MockHttpServletResponse response = new MockHttpServletResponse(servletContext);

		tag = new HiddenTag();

		jspContext = new MockJspContext(request, response);
		tag.setJspContext(jspContext);

		model = new TestModel();
		request.setAttribute(ServletUtils.REQUEST_ATTRIBUTE_MODEL, model);
		modelState = ServletUtils.getModelState(request);
	}

	@Test
	public void test_noValue() {
		tag.setProperty("__model.userId");
		try {
			tag.doTag();
			String html = tag.getPageContext().getOut().toString();

			Assert.assertTrue(html.contains("type=\"hidden\""));
			Assert.assertTrue(html.contains("id=\"userId\""));
			Assert.assertTrue(html.contains("name=\"userId\""));

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_value() {
		tag.setProperty("__model.userId");
		model.setUserId(100);

		try {
			tag.doTag();
			String html = tag.getPageContext().getOut().toString();

			Assert.assertTrue(html.contains("value=\"100\""));
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_invalidConversion() {
		tag.setProperty("__model.userId");

		jspContext.getMockRequest().addParameter("userId", "invalid");
		modelState.addFieldError("userId", "userId must be integer.");

		try {
			tag.doTag();
			String html = tag.getPageContext().getOut().toString();

			Assert.assertTrue(html.contains("value=\"invalid\""));
			Assert.assertTrue(html.contains("field-error"));

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	public static class TestModel {

		private Integer userId;

		public Integer getUserId() {
			return userId;
		}

		public void setUserId(Integer userId) {
			this.userId = userId;
		}
	}
}
