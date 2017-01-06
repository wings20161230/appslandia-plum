package com.appslandia.plum.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.appslandia.plum.mocks.MockHttpServletRequest;
import com.appslandia.plum.mocks.MockWebContext;

public class ActionParserTest {

	MockWebContext mockWebContext;
	ActionParser actionParser;
	MockHttpServletRequest request;

	@Before
	public void initialize() {
		mockWebContext = new MockWebContext();
		mockWebContext.register(TestController.class);
		actionParser = mockWebContext.getObject(ActionParser.class);

		request = mockWebContext.createMockHttpServletRequest();
	}

	@Test
	public void test_actionNoPathParams() {
		request.setRequestURI("/testController/actionNoPathParams.html");

		try {
			// pathItems
			List<String> pathItems = ActionPathUtils.parsePathItems(request.getServletPath(), actionParser.getActionExtension());

			// actionContext
			ActionContext actionContext = new ActionContext();
			Map<String, String> pathParamMap = new HashMap<>();
			boolean parsed = actionParser.parseActionContext(pathItems, actionContext, pathParamMap);

			Assert.assertTrue(parsed);
			Assert.assertEquals(actionContext.getAction(), "actionNoPathParams");
			Assert.assertEquals(actionContext.getController(), "testController");
			Assert.assertEquals(pathParamMap.size(), 0);

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_indexAction() {
		request.setRequestURI("/testController.html");

		try {
			// pathItems
			List<String> pathItems = ActionPathUtils.parsePathItems(request.getServletPath(), actionParser.getActionExtension());

			// actionContext
			ActionContext actionContext = new ActionContext();
			Map<String, String> pathParamMap = new HashMap<>();
			boolean parsed = actionParser.parseActionContext(pathItems, actionContext, pathParamMap);

			Assert.assertTrue(parsed);
			Assert.assertEquals(actionContext.getAction(), "index");
			Assert.assertEquals(actionContext.getController(), "testController");
			Assert.assertEquals(pathParamMap.size(), 0);

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_defaultController() {
		request.setRequestURI("/index.html");

		try {
			// pathItems
			List<String> pathItems = ActionPathUtils.parsePathItems(request.getServletPath(), actionParser.getActionExtension());

			// actionContext
			ActionContext actionContext = new ActionContext();
			Map<String, String> pathParamMap = new HashMap<>();
			boolean parsed = actionParser.parseActionContext(pathItems, actionContext, pathParamMap);

			Assert.assertTrue(parsed);
			Assert.assertEquals(actionContext.getAction(), "index");
			Assert.assertEquals(actionContext.getController(), "testController");
			Assert.assertEquals(pathParamMap.size(), 0);

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_actionNoPathParams_invalidPathParamsAdded() {
		request.setRequestURI("/testController/actionNoPathParams/p1.html");

		try {
			// pathItems
			List<String> pathItems = ActionPathUtils.parsePathItems(request.getServletPath(), actionParser.getActionExtension());

			// actionContext
			ActionContext actionContext = new ActionContext();
			Map<String, String> pathParamMap = new HashMap<>();
			boolean parsed = actionParser.parseActionContext(pathItems, actionContext, pathParamMap);

			Assert.assertFalse(parsed);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_actionPathParams_pathParamsProvided() {
		request.setRequestURI("/testController/actionPathParams/p1/p2.html");

		try {
			// pathItems
			List<String> pathItems = ActionPathUtils.parsePathItems(request.getServletPath(), actionParser.getActionExtension());

			// actionContext
			ActionContext actionContext = new ActionContext();
			Map<String, String> pathParamMap = new HashMap<>();
			boolean parsed = actionParser.parseActionContext(pathItems, actionContext, pathParamMap);

			Assert.assertTrue(parsed);
			Assert.assertEquals(actionContext.getAction(), "actionPathParams");
			Assert.assertEquals(actionContext.getController(), "testController");
			Assert.assertEquals(pathParamMap.size(), 2);

			Assert.assertEquals(pathParamMap.get("p1"), "p1");
			Assert.assertEquals(pathParamMap.get("p2"), "p2");

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_actionPathParams_pathParamsMissed() {
		request.setRequestURI("/testController/actionPathParams/p1.html");

		try {
			// pathItems
			List<String> pathItems = ActionPathUtils.parsePathItems(request.getServletPath(), actionParser.getActionExtension());

			// actionContext
			ActionContext actionContext = new ActionContext();
			Map<String, String> pathParamMap = new HashMap<>();
			boolean parsed = actionParser.parseActionContext(pathItems, actionContext, pathParamMap);

			Assert.assertFalse(parsed);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_actionInlinePathParams_pathParamsProvided() {
		request.setRequestURI("/testController/actionInlinePathParams/p1-p2.html");

		try {
			// pathItems
			List<String> pathItems = ActionPathUtils.parsePathItems(request.getServletPath(), actionParser.getActionExtension());

			// actionContext
			ActionContext actionContext = new ActionContext();
			Map<String, String> pathParamMap = new HashMap<>();
			boolean parsed = actionParser.parseActionContext(pathItems, actionContext, pathParamMap);

			Assert.assertTrue(parsed);
			Assert.assertEquals(actionContext.getAction(), "actionInlinePathParams");
			Assert.assertEquals(actionContext.getController(), "testController");
			Assert.assertEquals(pathParamMap.size(), 2);

			Assert.assertEquals(pathParamMap.get("p1"), "p1");
			Assert.assertEquals(pathParamMap.get("p2"), "p2");

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_actionInlinePathParams_pathParamsMissed() {
		request.setRequestURI("/testController/actionInlinePathParams/p1.html");

		try {
			// pathItems
			List<String> pathItems = ActionPathUtils.parsePathItems(request.getServletPath(), actionParser.getActionExtension());

			// actionContext
			ActionContext actionContext = new ActionContext();
			Map<String, String> pathParamMap = new HashMap<>();
			boolean parsed = actionParser.parseActionContext(pathItems, actionContext, pathParamMap);

			Assert.assertFalse(parsed);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_actionInlinePathParams_invalidPathParamsAdded() {
		request.setRequestURI("/testController/actionInlinePathParams/p1-p2/p3.html");

		try {
			// pathItems
			List<String> pathItems = ActionPathUtils.parsePathItems(request.getServletPath(), actionParser.getActionExtension());

			// actionContext
			ActionContext actionContext = new ActionContext();
			Map<String, String> pathParamMap = new HashMap<>();
			boolean parsed = actionParser.parseActionContext(pathItems, actionContext, pathParamMap);

			Assert.assertFalse(parsed);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Path("testController")
	@Default
	private static class TestController {

		@GET
		public void index() {
		}

		@GET
		public void actionNoPathParams() {
		}

		@GET
		@PathParams("/{p1}/{p2}")
		public void actionPathParams() {
		}

		@GET
		@PathParams("/{p1}-{p2}")
		public void actionInlinePathParams() {
		}
	}
}
