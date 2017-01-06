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

public class CheckboxTagTest {

	MockServletContext servletContext;

	CheckboxTag tag;
	ModelState modelState;

	@Before
	public void initialize() {
		servletContext = new MockServletContext(new MockSessionCookieConfig());
		ExpressionEvaluator.setInstance(new ExpressionEvaluatorImpl());

		MockHttpServletRequest request = new MockHttpServletRequest(servletContext);
		MockHttpServletResponse response = new MockHttpServletResponse(servletContext);

		tag = new CheckboxTag();
		tag.setJspContext(new MockJspContext(request, response));
		modelState = ServletUtils.getModelState(request);
	}

	@Test
	public void test() {
		tag.setName("name1");
		tag.setSubmitValue("submitValue");

		tag.setAutofocus(true);
		tag.setCssClass("class1");
		tag.setCssStyle("prop1=value1");
		tag.setDatatag("tag1");
		tag.setReadonly(true);
		tag.setForm("form1");
		tag.setHidden(true);
		tag.setRequired(true);
		tag.setTitle("title1");

		try {
			tag.doTag();
			String html = tag.getPageContext().getOut().toString();

			Assert.assertTrue(html.contains("type=\"checkbox\""));
			Assert.assertTrue(html.contains("name=\"name1\""));
			Assert.assertTrue(html.contains("value=\"submitValue\""));

			Assert.assertTrue(html.contains("autofocus=\"autofocus\""));
			Assert.assertTrue(html.contains("class=\"class1\""));
			Assert.assertTrue(html.contains("style=\"prop1=value1\""));
			Assert.assertTrue(html.contains("data-tag=\"tag1\""));

			Assert.assertTrue(html.contains("disabled=\"disabled\""));
			Assert.assertTrue(html.contains("form=\"form1\""));
			Assert.assertTrue(html.contains("hidden=\"hidden\""));

			Assert.assertTrue(html.contains("required=\"required\""));
			Assert.assertTrue(html.contains("title=\"title1\""));

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_readonly_checked() {
		tag.setName("name1");
		tag.setReadonly(true);

		tag.setSubmitValue("submitValue");
		tag.setValue("submitValue");
		tag.setChecked(true);

		try {
			tag.doTag();
			String html = tag.getPageContext().getOut().toString();
			Assert.assertTrue(html.contains("type=\"checkbox\""));
			Assert.assertTrue(html.contains("value=\"submitValue\""));
			Assert.assertTrue(html.contains("disabled=\"disabled\""));
			Assert.assertTrue(html.contains("type=\"hidden\""));

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_readonly_notChecked() {
		tag.setName("name1");
		tag.setReadonly(true);

		tag.setSubmitValue("submitValue");
		tag.setValue("invalidValue");
		tag.setChecked(false);

		try {
			tag.doTag();
			String html = tag.getPageContext().getOut().toString();
			Assert.assertTrue(html.contains("type=\"checkbox\""));
			Assert.assertTrue(html.contains("value=\"submitValue\""));
			Assert.assertTrue(html.contains("disabled=\"disabled\""));

			Assert.assertFalse(html.contains("type=\"hidden\""));
			Assert.assertFalse(html.contains("value=\"invalidValue\""));

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}
}
