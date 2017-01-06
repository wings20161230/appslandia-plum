package com.appslandia.plum.jsp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.appslandia.common.base.StandardConsts;
import com.appslandia.plum.base.ActionContextTestBase;
import com.appslandia.plum.base.ActionParser;
import com.appslandia.plum.base.GET;
import com.appslandia.plum.base.Path;
import com.appslandia.plum.base.ServletUtils;
import com.appslandia.plum.mocks.MockHttpServletRequest;
import com.appslandia.plum.mocks.MockHttpServletResponse;
import com.appslandia.plum.mocks.MockJspContext;
import com.appslandia.plum.mocks.MockWebContext;
import com.appslandia.plum.web.ConstDescProvider;

public class ConstDescTagTest extends ActionContextTestBase {

	MockWebContext mockWebContext;
	ConstDescProvider constDescProvider;

	MockHttpServletRequest request;
	MockHttpServletResponse response;

	ConstDescTag tag;

	@Before
	public void initialize() {
		mockWebContext = new MockWebContext();
		mockWebContext.register(TestController.class);
		constDescProvider = mockWebContext.getObject(ConstDescProvider.class);

		mockWebContext.getMockServletContext().setAttribute(ServletUtils.CONTEXT_ATTRIBUTE_ACTION_PARSER, mockWebContext.getObject(ActionParser.class));
		mockWebContext.getMockServletContext().setAttribute(ServletUtils.CONTEXT_ATTRIBUTE_CONST_DESC_PROVIDER, constDescProvider);

		request = mockWebContext.createMockHttpServletRequest();
		request.setRequestURI("/testController/index.html");
		response = mockWebContext.createMockHttpServletResponse();

		tag = new ConstDescTag();
		tag.setJspContext(new MockJspContext(request, response));
	}

	@Test
	public void test() {
		initActionContext(request, mockWebContext);

		constDescProvider.parseDescKeys(StandardConsts.class);

		tag.setConstGroup("activeConsts");
		tag.setValue(StandardConsts.ACTIVE);

		try {
			tag.doTag();

			String html = tag.getPageContext().getOut().toString();
			Assert.assertEquals(html, "?activeConsts.active");

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_notConfigured() {
		initActionContext(request, mockWebContext);

		tag.setConstGroup("activeConsts");
		tag.setValue(StandardConsts.INACTIVE);

		try {
			tag.doTag();

			String html = tag.getPageContext().getOut().toString();
			Assert.assertEquals(html, "?" + StandardConsts.INACTIVE);

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
