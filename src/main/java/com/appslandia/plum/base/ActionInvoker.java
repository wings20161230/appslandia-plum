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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ElementKind;
import javax.validation.Path;

import com.appslandia.common.base.JsonProcessor;
import com.appslandia.common.formatters.Formatter;
import com.appslandia.common.formatters.FormatterProvider;
import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.ExceptionUtils;
import com.appslandia.common.utils.StringUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public abstract class ActionInvoker {

	public abstract FormatterProvider getFormatterProvider();

	public abstract ModelBinder getModelBinder();

	public abstract JsonProcessor getJsonProcessor();

	private Formatter findFormatter(ParamDesc paramDesc) throws IllegalArgumentException {
		if (paramDesc.getFormatterId() == null) {
			return getFormatterProvider().getFormatter(paramDesc.getParamType());
		} else {
			return getFormatterProvider().getFormatter(paramDesc.getFormatterId());
		}
	}

	private String getErrorMessage(MessageBundle messageBundle, String errorMsgKey, String fieldName) {
		return messageBundle.getMessage(errorMsgKey, new Object[] { messageBundle.get(fieldName, fieldName) });
	}

	public Object invoke(HttpServletRequest request, HttpServletResponse response, ActionContext actionContext, Object controller) throws Exception {
		Object[] actionArgs = new Object[actionContext.getActionDesc().getParamDescs().length];

		for (int index = 0; index < actionArgs.length; index++) {
			ParamDesc paramDesc = actionContext.getActionDesc().getParamDescs()[index];

			// HttpServletRequest
			if (paramDesc.getParamType() == HttpServletRequest.class) {
				actionArgs[index] = request;
				continue;
			}
			// HttpServletResponse
			if (paramDesc.getParamType() == HttpServletResponse.class) {
				actionArgs[index] = response;
				continue;
			}
			// ActionContext
			if (paramDesc.getParamType() == ActionContext.class) {
				actionArgs[index] = actionContext;
				continue;
			}
			// ModelState
			if (paramDesc.getParamType() == ModelState.class) {
				actionArgs[index] = ServletUtils.getModelState(request);
				continue;
			}

			// @JsonModel
			if (paramDesc.getJsonModel() != null) {
				Object model = null;
				// request.getReader()
				if (paramDesc.getJsonModel().value().isEmpty()) {
					model = getJsonProcessor().read(request.getReader(), paramDesc.getParamType());
				}
				// part.getInputStream()
				else {
					Part part = request.getPart(paramDesc.getJsonModel().value());
					AssertUtils.assertNotNull(part);
					try (BufferedReader reader = new BufferedReader(new InputStreamReader(part.getInputStream(), StandardCharsets.UTF_8))) {
						model = getJsonProcessor().read(reader, paramDesc.getParamType());
					}
				}
				getModelBinder().validateModel(model, ServletUtils.getModelState(request), actionContext);
				actionArgs[index] = model;
				continue;
			}
			// @FormModel
			if (paramDesc.getFormModel() != null) {
				Object model = paramDesc.getParamType().newInstance();
				getModelBinder().bindModel(request, model);
				actionArgs[index] = model;
				continue;
			}
			// String?
			if (paramDesc.getParamType() == String.class) {
				String convertedValue = StringUtils.trimToNull(request.getParameter(paramDesc.getRequestParam()));
				if (paramDesc.isPathParam() && (convertedValue == null)) {
					throw new NotFoundException(actionContext.getMessage(ServletUtils.ERROR_MSGKEY_NOT_FOUND, request.getRequestURI()));
				}
				actionArgs[index] = convertedValue;
				continue;
			}
			// Formatter?
			String paramValue = StringUtils.trimToNull(request.getParameter(paramDesc.getRequestParam()));
			ModelBinderUtils.ParseResult parseResult = new ModelBinderUtils.ParseResult();

			Object convertedValue = ModelBinderUtils.parse(paramValue, paramDesc.getParamType(), parseResult, findFormatter(paramDesc), actionContext.getFormatProvider());
			actionArgs[index] = convertedValue;

			if (parseResult.isError()) {
				if (paramDesc.isPathParam()) {
					throw new NotFoundException(actionContext.getMessage(ServletUtils.ERROR_MSGKEY_NOT_FOUND, request.getRequestURI()));
				}
				ServletUtils.getModelState(request).setParseError(paramDesc.getRequestParam(), getErrorMessage(actionContext.getMessageBundle(), parseResult.getMsgKey(), paramDesc.getRequestParam()));
			}
		}

		// Invoke actionMethod
		try {
			return actionContext.getActionDesc().getMethod().invoke(controller, actionArgs);

		} catch (Exception ex) {
			Exception he = ExceptionUtils.tryUnwrapInvocationTargetException(ex);
			if (he instanceof ConstraintViolationException) {
				handleConstraintViolationException((ConstraintViolationException) he, request, actionContext);
			}
			throw he;
		}
	}

	private void handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request, ActionContext actionContext) {
		for (Object violation : ex.getConstraintViolations()) {
			ConstraintViolation<?> error = (ConstraintViolation<?>) violation;
			Path.Node paramNode = getParameterNode(error.getPropertyPath());
			if (paramNode == null) {
				throw ex;
			}
			ParamDesc paramDesc = actionContext.getActionDesc().getParamDesc(paramNode.getName());
			if (paramDesc.isPathParam()) {
				throw new NotFoundException(actionContext.getMessage(ServletUtils.ERROR_MSGKEY_NOT_FOUND, request.getRequestURI()), ex);
			}
			throw new BadRequestException(actionContext.getMessage(ServletUtils.ERROR_MSGKEY_BAD_REQUEST, request.getRequestURI()), ex);
		}
		throw ex;
	}

	private Path.Node getParameterNode(Path path) {
		Iterator<Path.Node> iter = path.iterator();
		while (iter.hasNext()) {
			Path.Node node = iter.next();
			if (node.getKind() == ElementKind.PARAMETER) {
				return node;
			}
		}
		return null;
	}
}
