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

public class DynFormTagTest extends ActionContextTestBase {

	MockWebContext mockWebContext;
	MockHttpServletRequest request;
	MockHttpServletResponse response;

	DynFormTag tag;

	@Before
	public void initialize() {
		mockWebContext = new MockWebContext();
		mockWebContext.register(TestController.class);
		mockWebContext.getMockServletContext().setAttribute(ServletUtils.CONTEXT_ATTRIBUTE_ACTION_PARSER, mockWebContext.getObject(ActionParser.class));

		request = mockWebContext.createMockHttpServletRequest();
		request.setRequestURI("/testController/index.html");
		response = mockWebContext.createMockHttpServletResponse();

		tag = new DynFormTag();
		tag.setJspContext(new MockJspContext(request, response));
	}

	@Test
	public void test() {
		initActionContext(request, mockWebContext);

		tag.setId("form1");
		tag.setName("form1");

		tag.setAction("index");
		tag.setController("testController");

		tag.setNovalidate(true);

		try {
			tag.doTag();
			String html = tag.getPageContext().getOut().toString();

			Assert.assertTrue(html.contains("id=\"form1\""));
			Assert.assertTrue(html.contains("name=\"form1\""));

			Assert.assertTrue(html.contains("accept-charset=\"UTF-8\""));
			Assert.assertTrue(html.contains("enctype=\"application/x-www-form-urlencoded\""));

			Assert.assertTrue(html.contains("method=\"POST\""));
			Assert.assertTrue(html.contains("novalidate=\"novalidate\""));

			Assert.assertTrue(html.contains("/testController/index.html"));

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
