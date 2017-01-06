package com.appslandia.plum.jsp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.appslandia.plum.base.ActionContextTestBase;
import com.appslandia.plum.base.ActionParser;
import com.appslandia.plum.base.GET;
import com.appslandia.plum.base.Path;
import com.appslandia.plum.base.ServletUtils;
import com.appslandia.plum.mocks.MockHttpServletRequest;
import com.appslandia.plum.mocks.MockHttpServletResponse;
import com.appslandia.plum.mocks.MockJspContext;
import com.appslandia.plum.mocks.MockWebContext;

public class FieldLabelTagTest extends ActionContextTestBase {

	MockWebContext mockWebContext;
	MockHttpServletRequest request;
	MockHttpServletResponse response;

	FieldLabelTag tag;

	@Before
	public void initialize() {
		mockWebContext = new MockWebContext();
		mockWebContext.register(TestController.class);
		mockWebContext.getMockServletContext().setAttribute(ServletUtils.CONTEXT_ATTRIBUTE_ACTION_PARSER, mockWebContext.getObject(ActionParser.class));

		request = mockWebContext.createMockHttpServletRequest();
		request.setRequestURI("/testController/index.html");
		response = mockWebContext.createMockHttpServletResponse();

		tag = new FieldLabelTag();
		tag.setJspContext(new MockJspContext(request, response));
	}

	@Test
	public void test() {
		initActionContext(request, mockWebContext);

		tag.setFieldName("userName");
		tag.setForm("testForm");
		tag.setLabelKey("testLabel");

		try {
			tag.doTag();
			String html = tag.getPageContext().getOut().toString();

			Assert.assertTrue(html.contains("id=\"label_userName\""));
			Assert.assertTrue(html.contains("for=\"userName\""));
			Assert.assertTrue(html.contains("form=\"testForm\""));

			Assert.assertTrue(html.contains("?testLabel"));

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Path("testController")
	private static class TestController {

		@GET
		public void index() {
		}
	}
}
