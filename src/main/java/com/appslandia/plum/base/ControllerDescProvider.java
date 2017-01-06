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

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.appslandia.common.base.CaseInsensitiveMap;
import com.appslandia.common.formatters.FormatterId;
import com.appslandia.common.objects.AnnotationUtils;
import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.StringUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public abstract class ControllerDescProvider {

	private ControllerDesc defaultController;
	final Map<String, ControllerDesc> controllerDescMap = new CaseInsensitiveMap<ControllerDesc>();

	public ControllerDesc getDefault() {
		return this.defaultController;
	}

	public ControllerDesc getControllerDesc(String controller) {
		return this.controllerDescMap.get(controller);
	}

	public abstract boolean isEnableHttps();

	public void parseControllerDesc(Class<?> controllerClass) {
		// ControllerDesc
		ControllerDesc controllerDesc = new ControllerDesc();
		controllerDesc.setControllerClass(controllerClass);
		controllerDesc.setQualifiers(AnnotationUtils.parseQualifiers(controllerClass));

		// @Path
		Path path = controllerClass.getDeclaredAnnotation(Path.class);
		AssertUtils.assertNotNull(path, "@Path is required (controller=" + controllerClass + ")");
		controllerDesc.setController(path.value());

		// actionDescMap
		Map<String, ActionDesc> actionDescMap = controllerDesc.getActionDescMap();

		// Action methods
		for (Method actionMethod : controllerClass.getMethods()) {
			if (actionMethod.getDeclaringClass() == Object.class) {
				continue;
			}
			if ((Modifier.isPublic(actionMethod.getModifiers()) == false) || Modifier.isStatic(actionMethod.getModifiers())) {
				continue;
			}

			// Action Verbs
			String[] actionVerbs = parseActionVerbs(actionMethod);

			// @ChildAction
			ChildAction childAction = actionMethod.getDeclaredAnnotation(ChildAction.class);

			if ((childAction == null) && (actionVerbs.length == 0)) {
				continue;
			}

			// ActionDesc
			ActionDesc actionDesc = new ActionDesc();
			actionDesc.setMethod(actionMethod);
			actionDesc.setChildAction(childAction != null);

			// @Path
			path = actionMethod.getDeclaredAnnotation(Path.class);
			actionDesc.setAction((path == null) ? actionMethod.getName() : path.value());

			// @PathParams
			PathParams pathParams = actionMethod.getDeclaredAnnotation(PathParams.class);
			try {
				String value = (pathParams != null) ? pathParams.value() : null;
				actionDesc.setPathParams(ActionPathUtils.parsePathParams(value));
			} catch (Exception ex) {
				throw new IllegalArgumentException(ex.getMessage() + " (action=" + actionMethod + ")", ex);
			}
			actionDesc.setParamDescs(parseParamDescs(actionMethod, actionDesc.getPathParams()));

			// @OutputConfig
			OutputConfig outputConfig = actionMethod.getDeclaredAnnotation(OutputConfig.class);
			actionDesc.setOutputConfig(outputConfig != null ? outputConfig : controllerClass.getDeclaredAnnotation(OutputConfig.class));

			// Accessible?
			if (actionDesc.isChildAction() == false) {

				// Action Verbs
				actionDesc.setActionVerbs(actionVerbs);

				// @Authorize
				Authorize authorize = actionMethod.getDeclaredAnnotation(Authorize.class);
				if (authorize == null) {
					authorize = controllerClass.getDeclaredAnnotation(Authorize.class);
				}
				actionDesc.setAuthorize(authorize);

				// @EnableHeaders
				EnableHeaders enableHeaders = actionMethod.getDeclaredAnnotation(EnableHeaders.class);
				if (enableHeaders == null) {
					enableHeaders = controllerClass.getDeclaredAnnotation(EnableHeaders.class);
				}
				if (enableHeaders != null) {
					actionDesc.setHeaderPolicys(enableHeaders.value());
				}

				// @EnableHttps
				EnableHttps enableHttps = actionMethod.getDeclaredAnnotation(EnableHttps.class);
				if (enableHttps == null) {
					enableHttps = controllerClass.getDeclaredAnnotation(EnableHttps.class);
				}
				if (enableHttps != null) {
					actionDesc.setEnableHttps(true);
				} else {
					actionDesc.setEnableHttps(isEnableHttps());
				}

				// @EnableCsrf
				EnableCsrf enableCsrf = actionMethod.getDeclaredAnnotation(EnableCsrf.class);
				actionDesc.setEnableCsrf(enableCsrf);

				// @EnableCaptcha
				EnableCaptcha enableCaptcha = actionMethod.getDeclaredAnnotation(EnableCaptcha.class);
				actionDesc.setEnableCaptcha(enableCaptcha);

				// @EnableEtag
				EnableEtag enableEtag = actionMethod.getDeclaredAnnotation(EnableEtag.class);
				actionDesc.setEnableEtag(enableEtag);

				// @EnableCors
				EnableCors enableCors = actionMethod.getDeclaredAnnotation(EnableCors.class);
				if (enableCors == null) {
					enableCors = controllerClass.getDeclaredAnnotation(EnableCors.class);
				}
				if (enableCors != null) {
					actionDesc.setCorsPolicy(enableCors.value());
				}

				// @DisableGzip
				DisableGzip disableGzip = actionMethod.getDeclaredAnnotation(DisableGzip.class);
				actionDesc.setDisableGzip(disableGzip != null ? disableGzip : controllerClass.getDeclaredAnnotation(DisableGzip.class));

				// @Async
				Async async = actionMethod.getDeclaredAnnotation(Async.class);
				actionDesc.setAsync(async != null ? async : controllerClass.getDeclaredAnnotation(Async.class));
			}

			// Inaccessible?
			if (actionMethod.getDeclaredAnnotation(Inaccessible.class) != null) {
				continue;
			}

			// Duplicated?
			AssertUtils.assertFalse(actionDescMap.containsKey(actionDesc.getAction()), "Action is duplicated (action=" + actionMethod + ")");
			actionDescMap.put(actionDesc.getAction(), actionDesc);
		}

		// Inaccessible?
		if (controllerClass.getDeclaredAnnotation(Inaccessible.class) != null) {
			return;
		}

		// Duplicated?
		AssertUtils.assertFalse(this.controllerDescMap.containsKey(controllerDesc.getController()), "Controller is duplicated (controller=" + controllerClass + ")");

		// @Default
		if (controllerClass.getDeclaredAnnotation(Default.class) != null) {
			AssertUtils.assertNull(this.defaultController, "@Default is duplicated (controller=" + controllerClass + ")");
			AssertUtils.assertTrue(actionDescMap.containsKey(ServletUtils.ACTION_INDEX), "Action index is required (controller=" + controllerClass + ")");
			this.defaultController = controllerDesc;
		}
		this.controllerDescMap.put(controllerDesc.getController(), controllerDesc);
	}

	private String[] parseActionVerbs(Method actionMethod) {
		List<String> verbs = new ArrayList<>();
		if (actionMethod.getDeclaredAnnotation(GET.class) != null) {
			verbs.add(HttpMethod.GET);
			verbs.add(HttpMethod.HEAD);
		}
		if (actionMethod.getDeclaredAnnotation(POST.class) != null) {
			verbs.add(HttpMethod.POST);
		}
		if (actionMethod.getDeclaredAnnotation(PUT.class) != null) {
			verbs.add(HttpMethod.PUT);
		}
		if (actionMethod.getDeclaredAnnotation(DELETE.class) != null) {
			verbs.add(HttpMethod.DELETE);
		}
		if (actionMethod.getDeclaredAnnotation(PATCH.class) != null) {
			verbs.add(HttpMethod.PATCH);
		}
		return (verbs.isEmpty() == false) ? verbs.toArray(new String[verbs.size()]) : StringUtils.EMPTY_ARRAY;
	}

	private boolean hasPathParam(PathParam[] pathParams, String paramName) {
		for (PathParam pathParam : pathParams) {
			if (pathParam.hasPathParam(paramName)) {
				return true;
			}
		}
		return false;
	}

	private ParamDesc[] parseParamDescs(Method actionMethod, PathParam[] pathParams) {
		Parameter[] parameters = actionMethod.getParameters();
		ParamDesc[] paramDescs = new ParamDesc[parameters.length];

		for (int i = 0; i < paramDescs.length; i++) {
			ParamDesc paramDesc = new ParamDesc();
			paramDescs[i] = paramDesc;

			Parameter parameter = parameters[i];
			paramDesc.setParamType(parameter.getType());
			paramDesc.setParamName(parameter.getName());

			// HttpServletRequest | HttpServletResponse
			if ((parameter.getType() == HttpServletRequest.class) || (parameter.getType() == HttpServletResponse.class)) {
				continue;
			}

			// ActionContext | ModelState
			if ((parameter.getType() == ActionContext.class) || (parameter.getType() == ModelState.class)) {
				continue;
			}

			// @JsonModel
			JsonModel jsonModel = parameter.getDeclaredAnnotation(JsonModel.class);
			if (jsonModel != null) {
				paramDesc.setJsonModel(jsonModel);
				continue;
			}

			// @FormModel
			FormModel formModel = parameter.getDeclaredAnnotation(FormModel.class);
			if (formModel != null) {
				paramDesc.setFormModel(formModel);
				continue;
			}

			// @DescParam
			if (parameter.getDeclaredAnnotation(DescParam.class) != null) {
				AssertUtils.assertTrue(hasPathParam(pathParams, ServletUtils.PARAM_DESC), "__desc pathParam is not found (action=" + actionMethod + ")");

				paramDesc.setRequestParam(ServletUtils.PARAM_DESC);
				paramDesc.setPathParam(true);
				continue;
			}

			// @RequestParam
			RequestParam requestParam = parameter.getDeclaredAnnotation(RequestParam.class);
			paramDesc.setRequestParam(requestParam != null ? requestParam.value() : parameter.getName());
			paramDesc.setPathParam(hasPathParam(pathParams, paramDesc.getRequestParam()));

			// @FormatterId
			FormatterId formatterId = parameter.getDeclaredAnnotation(FormatterId.class);
			if (formatterId != null) {
				paramDesc.setFormatterId(formatterId.value());
			}
		}
		return paramDescs;
	}
}
