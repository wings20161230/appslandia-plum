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

public class FieldErrorTagTest {

	MockServletContext servletContext;

	FieldErrorTag tag;
	ModelState modelState;

	@Before
	public void initialize() {
		servletContext = new MockServletContext(new MockSessionCookieConfig());

		MockHttpServletRequest request = new MockHttpServletRequest(servletContext);
		MockHttpServletResponse response = new MockHttpServletResponse(servletContext);

		tag = new FieldErrorTag();
		tag.setJspContext(new MockJspContext(request, response));
		modelState = ServletUtils.getModelState(request);
	}

	@Test
	public void test() {
		tag.setFieldName("userName");
		modelState.addFieldError("userName", "userName is required.");

		try {
			tag.doTag();
			String html = tag.getPageContext().getOut().toString();
			Assert.assertTrue(html.contains("class=\"field-error-msg\""));
			Assert.assertTrue(html.contains("userName is required."));

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_moreCss() {
		tag.setFieldName("userName");
		tag.setCssClass("custClass");
		modelState.addFieldError("userName", "userName is required.");

		try {
			tag.doTag();
			String html = tag.getPageContext().getOut().toString();
			Assert.assertTrue(html.contains("class=\"custClass field-error-msg\""));
			Assert.assertTrue(html.contains("userName is required."));

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_valid() {
		tag.setFieldName("userName");

		try {
			tag.doTag();
			String html = tag.getPageContext().getOut().toString();
			Assert.assertTrue(html.contains("style=\"display:none\""));
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}
}
