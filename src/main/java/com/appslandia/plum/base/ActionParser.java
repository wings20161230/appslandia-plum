// The MIT License (MIT)
// Copyright © 2015 AppsLandia. All rights reserved.

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

package com.appslandia.plum.base;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public abstract class ActionParser {

	public abstract ControllerDescProvider getControllerDescProvider();

	public abstract AppConfig getAppConfig();

	public abstract LanguageProvider getLanguageProvider();

	public abstract String getActionExtension();

	public boolean parseActionContext(List<String> pathItems, ActionContext actionContext, Map<String, String> pathParamMap) {
		ControllerDesc controllerDesc;
		ActionDesc actionDesc;

		if (pathItems.size() == 0) {

			// {language}.{extension}
			controllerDesc = this.getControllerDescProvider().getDefault();
			actionDesc = controllerDesc.getActionDesc(ServletUtils.ACTION_INDEX);

		} else if (pathItems.size() == 1) {
			// /{controller}.{extension}
			String controller = pathItems.get(0);

			if (controller.equals(ServletUtils.ACTION_INDEX)) {
				controllerDesc = this.getControllerDescProvider().getDefault();
			} else {
				controllerDesc = this.getControllerDescProvider().getControllerDesc(controller);
				if (controllerDesc == null) {
					return false;
				}
			}

			actionDesc = controllerDesc.getActionDesc(ServletUtils.ACTION_INDEX);
			if ((actionDesc == null) || actionDesc.isChildAction()) {
				return false;
			}
			if (actionDesc.getPathParams().length > 0) {
				return false;
			}
		} else {
			// /{controller}(/{parameter})+.{extension} -> index action
			// /{controller}/{action}(/{parameter})+.{extension}
			String controller = pathItems.get(0);
			String action = pathItems.get(1);

			controllerDesc = this.getControllerDescProvider().getControllerDesc(controller);
			if (controllerDesc == null) {
				return false;
			}
			actionDesc = controllerDesc.getActionDesc(action);
			boolean actionFound = (actionDesc != null) && (actionDesc.isChildAction() == false);

			if (actionFound == false) {
				action = ServletUtils.ACTION_INDEX;
				actionDesc = controllerDesc.getActionDesc(action);

				if ((actionDesc == null) || actionDesc.isChildAction()) {
					return false;
				}
			}

			// Remove controller/action
			pathItems.remove(0);
			if (actionFound) {
				pathItems.remove(0);
			}
			PathParam[] pathParams = actionDesc.getPathParams();
			if (pathItems.size() != pathParams.length) {
				return false;
			}

			// pathParamMap
			for (int i = 0; i < pathParams.length; i++) {
				PathParam pathParm = pathParams[i];
				if (pathParm.getParamName() != null) {
					String paramVal = pathItems.get(i).trim();
					if (paramVal.isEmpty()) {
						return false;
					}
					pathParamMap.put(pathParm.getParamName(), paramVal);
				} else {
					if (ActionPathUtils.parsePathParams(pathItems.get(i), pathParm.getPathParams(), pathParamMap) == false) {
						return false;
					}
				}
			}
		}

		actionContext.setControllerDesc(controllerDesc);
		actionContext.setActionDesc(actionDesc);
		return true;
	}

	public String toActionUrl(HttpServletRequest request, String language, String controller, String action, Map<String, Object> parameterMap) throws IllegalArgumentException {
		return toActionUrl(request, language, controller, action, parameterMap, false);
	}

	public String toActionUrl(HttpServletRequest request, String language, String controller, String action, Map<String, Object> parameterMap, boolean absoluteUrl) throws IllegalArgumentException {
		// controllerDesc
		ControllerDesc controllerDesc = this.getControllerDescProvider().getControllerDesc(controller);
		if (controllerDesc == null) {
			throw new IllegalArgumentException("Controller is not found (controller=" + controller + ")");
		}
		// actionDesc
		ActionDesc actionDesc = controllerDesc.getActionDesc(action);
		if ((actionDesc == null) || actionDesc.isChildAction()) {
			throw new IllegalArgumentException("Action is not found (controller=" + controller + ", action=" + action + ")");
		}

		// URL
		StringBuilder url = new StringBuilder();
		ActionContext actionContext = (ActionContext) request.getAttribute(ServletUtils.REQUEST_ATTRIBUTE_ACTION_CONTEXT);

		// Scheme|Server|Port
		if (absoluteUrl) {
			if (actionContext.getActionDesc().isEnableHttps() || request.isSecure()) {
				url.append("https://").append(request.getServerName());
				if (this.getAppConfig().getHttpsPort() != 443) {
					url.append(':').append(this.getAppConfig().getHttpsPort());
				}
			} else {
				url.append("http://").append(request.getServerName());
				if (this.getAppConfig().getHttpPort() != 80) {
					url.append(':').append(this.getAppConfig().getHttpPort());
				}
			}
		}

		// Context path
		url.append(request.getServletContext().getContextPath());

		// Language
		if (language == null) {
			language = actionContext.getLanguageId();
			if (language != null) {
				url.append('/').append(language);
			}
		} else {
			if (this.getLanguageProvider().getLanguage(language) == null) {
				throw new IllegalArgumentException("Language is not supported (language=" + language + ")");
			}
			url.append('/').append(language);
		}
		// Controller
		url.append('/').append(controller);

		// Action
		if ((ServletUtils.ACTION_INDEX.equals(action) == false) || (actionDesc.getPathParams().length == 0)) {
			url.append('/').append(action);
		}

		// Path parameters
		if (parameterMap != null) {
			ActionPathUtils.addPathParams(url, parameterMap, actionDesc.getPathParams());
		} else if (actionDesc.getPathParams().length > 0) {
			throw new IllegalArgumentException("Path parameters are required.");
		}

		// Extension
		url.append(this.getActionExtension());

		// Query String
		if ((parameterMap != null) && (parameterMap.size() > actionDesc.getPathParams().length)) {
			url.append('?');
			ActionPathUtils.addQueryParams(url, parameterMap, actionDesc.getPathParams());
		}
		return url.toString();
	}
}
