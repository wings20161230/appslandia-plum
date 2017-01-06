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

public class BitMaskCheckboxTagTest {

	MockServletContext servletContext;
	MockHttpServletRequest request;

	BitMaskCheckboxTag tag;
	TestModel model;
	ModelState modelState;

	@Before
	public void initialize() {
		servletContext = new MockServletContext(new MockSessionCookieConfig());
		ExpressionEvaluator.setInstance(new ExpressionEvaluatorImpl());

		MockHttpServletRequest request = new MockHttpServletRequest(servletContext);
		MockHttpServletResponse response = new MockHttpServletResponse(servletContext);

		tag = new BitMaskCheckboxTag();
		tag.setJspContext(new MockJspContext(request, response));

		model = new TestModel();
		request.setAttribute(ServletUtils.REQUEST_ATTRIBUTE_MODEL, model);
		modelState = ServletUtils.getModelState(request);
	}

	@Test
	public void test_notChecked() {
		model.setPermission(4);
		tag.setProperty("__model.permission");
		tag.setSubmitValue(1);

		try {
			tag.doTag();
			String html = tag.getPageContext().getOut().toString();

			Assert.assertTrue(html.contains("value=\"1\""));
			Assert.assertFalse(html.contains("checked=\"checked\""));

		} catch (Exception ex) {

			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_checked() {
		model.setPermission(1 + 4);
		tag.setProperty("__model.permission");
		tag.setSubmitValue(1);

		try {
			tag.doTag();
			String html = tag.getPageContext().getOut().toString();

			Assert.assertTrue(html.contains("value=\"1\""));
			Assert.assertTrue(html.contains("checked=\"checked\""));
		} catch (Exception ex) {

			Assert.fail(ex.getMessage());
		}
	}

	public static class TestModel {
		private int permission;

		public int getPermission() {
			return this.permission;
		}

		public void setPermission(int permission) {
			this.permission = permission;
		}
	}
}
