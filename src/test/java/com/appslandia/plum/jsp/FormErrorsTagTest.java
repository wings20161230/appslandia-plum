package com.appslandia.plum.jsp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.appslandia.plum.base.ModelState;
import com.appslandia.plum.base.ServletUtils;
import com.appslandia.plum.mocks.MockHttpServletRequest;
import com.appslandia.plum.mocks.MockHttpServletResponse;
import com.appslandia.plum.mocks.MockJspContext;
import com.appslandia.plum.mocks.MockJspFragment;
import com.appslandia.plum.mocks.MockWebContext;

public class FormErrorsTagTest {

	MockWebContext mockWebContext;
	MockHttpServletRequest request;
	MockHttpServletResponse response;

	FormErrorsTag tag;
	ModelState modelState;

	@Before
	public void initialize() {
		mockWebContext = new MockWebContext();

		request = mockWebContext.createMockHttpServletRequest();
		response = mockWebContext.createMockHttpServletResponse();

		tag = new FormErrorsTag();
		tag.setJspContext(new MockJspContext(request, response));
		modelState = ServletUtils.getModelState(request);
	}

	@Test
	public void test() {
		try {
			tag.doTag();
			String html = tag.getPageContext().getOut().toString();

			Assert.assertTrue(html.contains("class=\"form-errors\""));
			Assert.assertTrue(html.contains("style=\"display:none\""));
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_hasErrors() {
		modelState.addFieldError("userName", "userName is required.");
		modelState.addModelError("faleModelError");

		try {
			tag.doTag();
			String html = tag.getPageContext().getOut().toString();
			Assert.assertTrue(html.contains("class=\"form-errors\""));

			Assert.assertTrue(html.contains("<li>faleModelError</li>"));
			Assert.assertTrue(html.contains("<li>userName is required.</li>"));

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_excludePropertyErrors() {
		modelState.addFieldError("userName", "userName is required.");
		modelState.addModelError("faleModelError");

		tag.setExcludePropertyErrors(true);

		try {
			tag.doTag();
			String html = tag.getPageContext().getOut().toString();
			Assert.assertTrue(html.contains("class=\"form-errors\""));

			Assert.assertTrue(html.contains("<li>faleModelError</li>"));
			Assert.assertTrue(html.contains("<li>userName is required.</li>") == false);

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_fieldOrders() {
		modelState.addFieldError("userName", "userName is required.");
		modelState.addFieldError("password", "password is required.");

		FieldOrdersTag fieldOrders = new FieldOrdersTag();
		fieldOrders.setJspContext(tag.getPageContext());
		fieldOrders.setJspBody(new MockJspFragment(fieldOrders.getPageContext(), "userName, password"));

		tag.setContextId("loginForm");
		fieldOrders.setParent(tag);

		try {
			fieldOrders.doTag();
			tag.doTag();
			String html = tag.getPageContext().getOut().toString();

			Assert.assertTrue(html.contains("class=\"form-errors\""));

			int idx1 = html.indexOf("<li>userName is required.</li>");
			int idx2 = html.indexOf("<li>password is required.</li>");

			Assert.assertTrue(idx1 > 0);
			Assert.assertTrue(idx2 > 0);

			Assert.assertTrue(idx1 < idx2);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}
}
