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

public class TextAreaTagTest {

	MockServletContext servletContext;

	TextAreaTag tag;
	TestModel model;
	ModelState modelState;

	@Before
	public void initialize() {
		servletContext = new MockServletContext(new MockSessionCookieConfig());
		ExpressionEvaluator.setInstance(new ExpressionEvaluatorImpl());

		MockHttpServletRequest request = new MockHttpServletRequest(servletContext);
		MockHttpServletResponse response = new MockHttpServletResponse(servletContext);

		tag = new TextAreaTag();
		tag.setJspContext(new MockJspContext(request, response));

		model = new TestModel();
		request.setAttribute(ServletUtils.REQUEST_ATTRIBUTE_MODEL, model);
		modelState = ServletUtils.getModelState(request);
	}

	@Test
	public void test() {
		tag.setProperty("__model.notes");

		tag.setAutofocus(true);
		tag.setCssClass("class1");
		tag.setCssStyle("prop1=value1");
		tag.setDatatag("tag1");

		tag.setDisabled(true);
		tag.setForm("form1");
		tag.setHidden(true);
		tag.setMaxlength("100");

		tag.setRows("10");
		tag.setCols("50");

		tag.setPlaceholder("placeholder");
		tag.setReadonly(true);
		tag.setRequired(true);
		tag.setTitle("title1");

		try {
			tag.doTag();
			String html = tag.getPageContext().getOut().toString();

			Assert.assertTrue(html.contains("id=\"notes\""));
			Assert.assertTrue(html.contains("name=\"notes\""));

			Assert.assertTrue(html.contains("autofocus=\"autofocus\""));
			Assert.assertTrue(html.contains("class=\"class1\""));
			Assert.assertTrue(html.contains("style=\"prop1=value1\""));
			Assert.assertTrue(html.contains("data-tag=\"tag1\""));

			Assert.assertTrue(html.contains("disabled=\"disabled\""));
			Assert.assertTrue(html.contains("form=\"form1\""));
			Assert.assertTrue(html.contains("hidden=\"hidden\""));
			Assert.assertTrue(html.contains("maxlength=\"100\""));

			Assert.assertTrue(html.contains("rows=\"10\""));
			Assert.assertTrue(html.contains("cols=\"50\""));

			Assert.assertTrue(html.contains("placeholder=\"placeholder\""));
			Assert.assertTrue(html.contains("readonly=\"readonly\""));
			Assert.assertTrue(html.contains("required=\"required\""));
			Assert.assertTrue(html.contains("title=\"title1\""));

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_value() {
		tag.setProperty("__model.notes");
		model.setNotes("testNotes");

		try {
			tag.doTag();
			String html = tag.getPageContext().getOut().toString();
			Assert.assertTrue(html.contains("testNotes"));

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_escaped() {
		tag.setProperty("__model.notes");
		model.setNotes("> testNotes");

		try {
			tag.doTag();
			String html = tag.getPageContext().getOut().toString();
			Assert.assertTrue(html.contains("&gt; testNotes"));

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_readonly() {
		tag.setProperty("__model.notes");
		model.setNotes("testNotes");
		tag.setReadonly(true);

		try {
			tag.doTag();
			String html = tag.getPageContext().getOut().toString();

			Assert.assertTrue(html.contains("readonly=\"readonly\""));
			Assert.assertTrue(html.contains("type=\"hidden\""));
			Assert.assertTrue(html.contains("value=\"testNotes\""));

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_disabled() {
		tag.setProperty("__model.notes");
		model.setNotes("testNotes");
		tag.setDisabled(true);

		try {
			tag.doTag();
			String html = tag.getPageContext().getOut().toString();

			Assert.assertTrue(html.contains("disabled=\"disabled\""));
			Assert.assertFalse(html.contains("type=\"hidden\""));
			Assert.assertFalse(html.contains("value=\"testNotes\""));

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	public static class TestModel {
		private String notes;

		public String getNotes() {
			return this.notes;
		}

		public void setNotes(String notes) {
			this.notes = notes;
		}
	}
}
