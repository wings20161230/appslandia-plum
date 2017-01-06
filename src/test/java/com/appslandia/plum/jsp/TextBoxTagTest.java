package com.appslandia.plum.jsp;

import java.util.Date;

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

public class TextBoxTagTest {

	MockServletContext servletContext;

	TextBoxTag tag;
	TestModel model;
	ModelState modelState;

	@Before
	public void initialize() {
		servletContext = new MockServletContext(new MockSessionCookieConfig());
		ExpressionEvaluator.setInstance(new ExpressionEvaluatorImpl());

		MockHttpServletRequest request = new MockHttpServletRequest(servletContext);
		MockHttpServletResponse response = new MockHttpServletResponse(servletContext);

		tag = new TextBoxTag();
		tag.setJspContext(new MockJspContext(request, response));

		model = new TestModel();
		request.setAttribute(ServletUtils.REQUEST_ATTRIBUTE_MODEL, model);
		modelState = ServletUtils.getModelState(request);
	}

	@Test
	public void test() {
		tag.setProperty("__model.userName");
		tag.setType("text");

		tag.setAutocomplete(false);
		tag.setAutofocus(true);
		tag.setCssClass("class1");
		tag.setCssStyle("prop1=value1");
		tag.setDatatag("tag1");
		tag.setDisabled(true);
		tag.setForm("form1");
		tag.setList("list1");
		tag.setHidden(true);
		tag.setMaxlength("100");
		tag.setPattern("pattern");
		tag.setPlaceholder("placeholder");
		tag.setReadonly(true);
		tag.setRequired(true);
		tag.setTitle("title1");

		try {
			tag.doTag();
			String html = tag.getPageContext().getOut().toString();

			Assert.assertTrue(html.contains("type=\"text\""));
			Assert.assertTrue(html.contains("id=\"userName\""));
			Assert.assertTrue(html.contains("name=\"userName\""));

			Assert.assertTrue(html.contains("autocomplete=\"on\"") == false);
			Assert.assertTrue(html.contains("autofocus=\"autofocus\""));
			Assert.assertTrue(html.contains("class=\"class1\""));
			Assert.assertTrue(html.contains("style=\"prop1=value1\""));
			Assert.assertTrue(html.contains("data-tag=\"tag1\""));

			Assert.assertTrue(html.contains("disabled=\"disabled\""));
			Assert.assertTrue(html.contains("form=\"form1\""));
			Assert.assertTrue(html.contains("list=\"list1\""));
			Assert.assertTrue(html.contains("hidden=\"hidden\""));
			Assert.assertTrue(html.contains("maxlength=\"100\""));

			Assert.assertTrue(html.contains("pattern=\"pattern\""));
			Assert.assertTrue(html.contains("placeholder=\"placeholder\""));
			Assert.assertTrue(html.contains("readonly=\"readonly\""));
			Assert.assertTrue(html.contains("required=\"required\""));
			Assert.assertTrue(html.contains("title=\"title1\""));

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_noValue() {
		tag.setProperty("__model.userName");
		tag.setType("text");

		try {
			tag.doTag();
			String html = tag.getPageContext().getOut().toString();

			Assert.assertTrue(html.contains("value=\"\""));

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_value() {
		tag.setProperty("__model.userName");
		tag.setType("text");
		model.setUserName("testUser");

		try {
			tag.doTag();
			String html = tag.getPageContext().getOut().toString();
			Assert.assertTrue(html.contains("value=\"testUser\""));

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_escaped() {
		tag.setProperty("__model.userName");
		tag.setType("text");
		model.setUserName("> testUser");

		try {
			tag.doTag();
			String html = tag.getPageContext().getOut().toString();
			Assert.assertTrue(html.contains("value=\"&gt; testUser\""));

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_readonly() {
		tag.setProperty("__model.userName");
		tag.setType("text");
		tag.setReadonly(true);
		model.setUserName("testUser");

		try {
			tag.doTag();
			String html = tag.getPageContext().getOut().toString();

			Assert.assertTrue(html.contains("readonly=\"readonly\""));
			Assert.assertTrue(html.contains("type=\"hidden\""));
			Assert.assertTrue(html.contains("value=\"testUser\""));

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_disabled() {
		tag.setProperty("__model.userName");
		tag.setType("text");
		tag.setDisabled(true);
		model.setUserName("testUser");

		try {
			tag.doTag();
			String html = tag.getPageContext().getOut().toString();

			Assert.assertTrue(html.contains("disabled=\"disabled\""));
			Assert.assertFalse(html.contains("type=\"hidden\""));
			Assert.assertTrue(html.contains("value=\"testUser\""));

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	public static class TestModel {

		private Integer userId;
		private String userName;
		private Date dob;
		private boolean isActive;

		public Integer getUserId() {
			return userId;
		}

		public void setUserId(Integer userId) {
			this.userId = userId;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public Date getDob() {
			return dob;
		}

		public void setDob(Date dob) {
			this.dob = dob;
		}

		public boolean isActive() {
			return isActive;
		}

		public void setActive(boolean isActive) {
			this.isActive = isActive;
		}
	}
}
