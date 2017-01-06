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

public class SelectTagTest {

	MockServletContext servletContext;

	SelectTag tag;
	TestModel model;
	ModelState modelState;

	@Before
	public void initialize() {
		servletContext = new MockServletContext(new MockSessionCookieConfig());
		ExpressionEvaluator.setInstance(new ExpressionEvaluatorImpl());

		MockHttpServletRequest request = new MockHttpServletRequest(servletContext);
		MockHttpServletResponse response = new MockHttpServletResponse(servletContext);

		tag = new SelectTag();
		tag.setJspContext(new MockJspContext(request, response));

		model = new TestModel();
		request.setAttribute(ServletUtils.REQUEST_ATTRIBUTE_MODEL, model);
		modelState = ServletUtils.getModelState(request);
	}

	@Test
	public void test_optionTag_notSelected() {
		tag.setProperty("__model.userType");
		tag.putOption("1", "type1");
		tag.putOption("2", "type2");
		tag.putOption("3", "type3");

		try {
			tag.doTag();
			String html = tag.getPageContext().getOut().toString();
			Assert.assertFalse(html.contains("selected"));

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_optionTag_selected() {
		tag.setProperty("__model.userType");
		tag.putOption("1", "type1");
		tag.putOption("2", "type2");
		tag.putOption("3", "type3");
		model.setUserType(2);

		try {
			tag.doTag();
			String html = tag.getPageContext().getOut().toString();
			Assert.assertTrue(html.contains("<option value=\"2\" selected=\"selected\">type2</option>"));

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_optionTag_selected_readonly() {
		tag.setProperty("__model.userType");
		tag.putOption("1", "type1");
		tag.putOption("2", "type2");
		tag.putOption("3", "type3");
		tag.setReadonly(true);
		model.setUserType(2);

		try {
			tag.doTag();
			String html = tag.getPageContext().getOut().toString();
			Assert.assertTrue(html.contains("disabled=\"disabled\""));
			Assert.assertTrue(html.contains("<option value=\"2\" selected=\"selected\">type2</option>"));

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_optionTag_escaped() {
		tag.setProperty("__model.userType");
		tag.putOption("1", "> type1");

		try {
			tag.doTag();
			String html = tag.getPageContext().getOut().toString();
			Assert.assertTrue(html.contains("&gt; type1"));

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	public static class TestModel {

		private int userType;

		public int getUserType() {
			return this.userType;
		}

		public void setUserType(int userType) {
			this.userType = userType;
		}
	}
}
