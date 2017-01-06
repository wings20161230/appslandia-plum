package com.appslandia.plum.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

import com.appslandia.common.base.FormatProviderImpl;
import com.appslandia.common.base.Language;
import com.appslandia.plum.mocks.MockHttpServletRequest;
import com.appslandia.plum.mocks.MockWebContext;

public abstract class ActionContextTestBase {

	protected ActionContext initActionContext(MockHttpServletRequest request, MockWebContext mockWebContext) {

		ActionParser actionParser = mockWebContext.getObject(ActionParser.class);
		LanguageProvider languageProvider = mockWebContext.getObject(LanguageProvider.class);

		// actionContext
		ActionContext actionContext = new ActionContext();

		// pathItems
		List<String> pathItems = ActionPathUtils.parsePathItems(request.getServletPath(), actionParser.getActionExtension());

		// Language
		String languageId = pathItems.get(0);
		Language language = languageProvider.getLanguage(languageId);

		if (language == null) {
			languageId = null;
			language = languageProvider.getDefault();
		} else {
			// Remove language
			pathItems.remove(0);
			actionContext.setLanguageId(languageId);
		}

		actionContext.setLanguage(language);
		actionContext.setMessageBundle(new MessageBundle());
		actionContext.setFormatProvider(new FormatProviderImpl(actionContext.getLanguage()));

		// parseActionContext
		Map<String, String> pathParamMap = new HashMap<>();
		boolean parsed = actionParser.parseActionContext(pathItems, actionContext, pathParamMap);
		Assert.assertTrue(parsed);

		request.setAttribute(ServletUtils.REQUEST_ATTRIBUTE_ACTION_CONTEXT, actionContext);
		return actionContext;
	}
}
