package com.appslandia.plum.jsp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.appslandia.plum.base.ActionContextTestBase;
import com.appslandia.plum.base.ActionParser;
import com.appslandia.plum.base.GET;
import com.appslandia.plum.base.Path;
import com.appslandia.plum.base.PathParams;
import com.appslandia.plum.base.ServletUtils;
import com.appslandia.plum.mocks.MockHttpServletRequest;
import com.appslandia.plum.mocks.MockHttpServletResponse;
import com.appslandia.plum.mocks.MockJspContext;
import com.appslandia.plum.mocks.MockWebContext;

public class ActionImageTagTest extends ActionContextTestBase {

	MockWebContext mockWebContext;
	MockHttpServletRequest request;
	MockHttpServletResponse response;

	ActionImageTag tag;

	@Before
	public void initialize() {
		mockWebContext = new MockWebContext();
		mockWebContext.register(TestController.class);
		mockWebContext.getMockServletContext().setAttribute(ServletUtils.CONTEXT_ATTRIBUTE_ACTION_PARSER, mockWebContext.getObject(ActionParser.class));

		request = mockWebContext.createMockHttpServletRequest();
		request.setRequestURI("/testController/index.html");
		response = mockWebContext.createMockHttpServletResponse();

		tag = new ActionImageTag();
		tag.setJspContext(new MockJspContext(request, response));
	}

	@Test
	public void test_actionNoParams() {
		initActionContext(request, mockWebContext);

		tag.setAction("actionNoParams");
		tag.setController("testController");
		tag.setAlt("testAlt");
		tag.setWidth("300");
		tag.setHeight("50");

		try {
			tag.doTag();
			String html = tag.getPageContext().getOut().toString();

			Assert.assertTrue(html.contains("/testController/actionNoParams.html"));
			Assert.assertTrue(html.contains("src="));
			Assert.assertTrue(html.contains("alt=\"testAlt\""));
			Assert.assertTrue(html.contains("width=\"300\""));
			Assert.assertTrue(html.contains("height=\"50\""));

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_actionPathParams() {
		initActionContext(request, mockWebContext);

		tag.setAction("actionPathParams");
		tag.setController("testController");

		try {
			evalParamTag(tag, "p1", "param1");
			evalParamTag(tag, "p2", "param2");

			tag.doTag();
			String html = tag.getPageContext().getOut().toString();

			Assert.assertTrue(html.contains("/testController/actionPathParams/param1.html"));
			Assert.assertTrue(html.contains("?p2=param2"));

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	private void evalParamTag(ParamSupport parent, String name, Object value) {
		parent.putParam(name, value);
	}

	@Path("testController")
	private static class TestController {

		@GET
		public void index() {
		}

		@GET
		public void actionNoParams() {
		}

		@GET
		@PathParams("/{p1}")
		public void actionPathParams() {
		}
	}
}
