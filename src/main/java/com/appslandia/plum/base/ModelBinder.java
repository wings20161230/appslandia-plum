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

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Validator;

import com.appslandia.common.base.NotBinding;
import com.appslandia.common.formatters.Formatter;
import com.appslandia.common.formatters.FormatterId;
import com.appslandia.common.formatters.FormatterProvider;
import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.ReflectionUtils;
import com.appslandia.common.utils.StringUtils;
import com.appslandia.common.utils.TypeUtils;
import com.appslandia.common.validators.BitMask;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public abstract class ModelBinder {

	public abstract FormatterProvider getFormatterProvider();

	public abstract Validator getValidator();

	private String getErrorMessage(MessageBundle messageBundle, String errorMsgKey, String fieldName) {
		return messageBundle.getMessage(errorMsgKey, new Object[] { messageBundle.get(fieldName, fieldName) });
	}

	private Formatter findFormatter(Class<?> targetType, Field field) throws IllegalArgumentException {
		FormatterId formatterId = field.getDeclaredAnnotation(FormatterId.class);
		if (formatterId != null) {
			return getFormatterProvider().getFormatter(formatterId.value());
		} else {
			return getFormatterProvider().getFormatter(targetType);
		}
	}

	public ModelState bindModel(HttpServletRequest request, Object model) throws Exception {
		ActionContext actionContext = (ActionContext) request.getAttribute(ServletUtils.REQUEST_ATTRIBUTE_ACTION_CONTEXT);
		ModelState modelState = ServletUtils.getModelState(request);

		Queue<BinderNode> queue = new LinkedList<>();
		queue.add(new BinderNode(model, null));

		while (queue.isEmpty() == false) {
			BinderNode node = queue.poll();
			for (PropertyDescriptor propDesc : Introspector.getBeanInfo(node.model.getClass()).getPropertyDescriptors()) {

				// Getter/Setter
				if ((propDesc.getWriteMethod() == null) || (propDesc.getReadMethod() == null)) {
					continue;
				}

				// Field
				Field field = ReflectionUtils.findField(node.model.getClass(), propDesc.getName());
				if ((field == null) || (field.getDeclaredAnnotation(NotBinding.class) != null)) {
					continue;
				}

				// propPath
				String propPath = StringUtils.isNullOrEmpty(node.path) ? field.getName() : (node.path + "." + field.getName());

				// propPath is parameter?
				if (request.getParameterMap().containsKey(propPath)) {

					// String?
					if (field.getType() == String.class) {
						propDesc.getWriteMethod().invoke(node.model, StringUtils.trimToNull(request.getParameter(propPath)));
						continue;
					}

					// Array || @BitMask
					if (field.getType().isArray() || field.getDeclaredAnnotation(BitMask.class) != null) {

						// bitMaskParam
						boolean bitMaskParam = field.getDeclaredAnnotation(BitMask.class) != null;

						Class<?> elementType = null;
						if (bitMaskParam) {
							elementType = field.getType();
							AssertUtils.assertTrue(Number.class.isAssignableFrom(TypeUtils.wrap(elementType)));
						} else {
							elementType = field.getType().getComponentType();
						}

						// parseArray
						ModelBinderUtils.ParseResult parseResult = new ModelBinderUtils.ParseResult();
						Object parsedArray = ModelBinderUtils.parseArray(request.getParameterValues(propPath), elementType, parseResult, findFormatter(elementType, field), actionContext.getFormatProvider());

						// bitMaskParam?
						if (bitMaskParam) {
							propDesc.getWriteMethod().invoke(node.model, ModelBinderUtils.toBitMaskValue(parsedArray));
						} else {
							propDesc.getWriteMethod().invoke(node.model, parsedArray);
						}

						if (parseResult.isError()) {
							modelState.setParseError(propPath, getErrorMessage(actionContext.getMessageBundle(), parseResult.getMsgKey(), field.getName()));
						}
						continue;
					}

					// Formatter?
					ModelBinderUtils.ParseResult parseResult = new ModelBinderUtils.ParseResult();
					Object parsedValue = ModelBinderUtils.parse(StringUtils.trimToNull(request.getParameter(propPath)), field.getType(), parseResult, findFormatter(field.getType(), field), actionContext.getFormatProvider());
					propDesc.getWriteMethod().invoke(node.model, parsedValue);

					if (parseResult.isError()) {
						modelState.setParseError(propPath, getErrorMessage(actionContext.getMessageBundle(), parseResult.getMsgKey(), field.getName()));
					}
				}
				// propPath is NOT parameter
				// List?
				else if (List.class.isAssignableFrom(field.getType())) {

					// Sub index
					int countSubIndexProps = countSubIndexProps(request, propPath);
					if (countSubIndexProps == 0) {
						continue;
					}

					Class<?> elementType = ReflectionUtils.getListArgType((ParameterizedType) field.getGenericType());
					List<Object> subModel = new ArrayList<>(countSubIndexProps);
					int idx = 0;
					int count = 0;

					while (true) {
						String subIndexProp = propPath + "[" + idx + "]";
						if (hasSubProperties(request, subIndexProp)) {

							Object elementModel = elementType.newInstance();
							subModel.add(elementModel);
							queue.add(new BinderNode(elementModel, subIndexProp));

							if (++count == countSubIndexProps) {
								break;
							}
						}
						idx++;
					}
					propDesc.getWriteMethod().invoke(node.model, subModel);

				}
				// Collection/Map -> Skip
				else if (Collection.class.isAssignableFrom(field.getType()) || Map.class.isAssignableFrom(field.getType())) {
					continue;
				}
				// Sub dot?
				else if (hasSubProperties(request, propPath)) {
					Object subModel = propDesc.getReadMethod().invoke(node.model);
					if (subModel == null) {

						subModel = field.getType().newInstance();
						propDesc.getWriteMethod().invoke(node.model, subModel);
					}
					queue.add(new BinderNode(subModel, propPath));
				}
			} // Iteration of properties
		}

		// Validate Model
		validateModel(model, modelState, actionContext);
		return modelState;
	}

	public void validateModel(Object model, ModelState modelState, ActionContext actionContext) {
		Set<?> violations = getValidator().validate(model);
		StringBuilder fieldPath = null;
		for (Object violation : violations) {
			ConstraintViolation<?> error = (ConstraintViolation<?>) violation;
			if (fieldPath == null) {
				fieldPath = new StringBuilder();
			} else {
				fieldPath.setLength(0);
			}
			Path.Node leafNode = getLeafNode(error.getPropertyPath(), fieldPath);
			modelState.addFieldError(fieldPath.toString(), getErrorMessage(actionContext.getMessageBundle(), getErrorMsgKey(error), leafNode.getName()));
		}
	}

	public String getErrorMsgKey(ConstraintViolation<?> error) {
		return error.getConstraintDescriptor().getAnnotation().annotationType().getName() + ".message";
	}

	private Path.Node getLeafNode(Path path, StringBuilder fieldPath) {
		Iterator<Path.Node> iter = path.iterator();
		Path.Node node = null;
		while (iter.hasNext()) {
			node = (Path.Node) iter.next();
			if (node.getIndex() != null) {
				fieldPath.append('[').append(node.getIndex()).append(']');
			}
			if (fieldPath.length() != 0) {
				fieldPath.append('.');
			}
			fieldPath.append(node.getName());
		}
		return node;
	}

	private boolean hasSubProperties(HttpServletRequest request, String propPath) {
		String prefix = propPath + ".";
		for (String name : request.getParameterMap().keySet()) {
			if (name.startsWith(prefix) && (name.length() > prefix.length())) {
				return true;
			}
		}
		return false;
	}

	// [index].property_expr
	private static final Pattern __subIndexPropPattern = Pattern.compile("\\[\\d+\\]\\..+");

	private int countSubIndexProps(HttpServletRequest request, String propPath) {
		Set<String> indexes = null;
		for (String name : request.getParameterMap().keySet()) {
			if (name.startsWith(propPath)) {
				String subIndexProp = name.substring(propPath.length());
				if (__subIndexPropPattern.matcher(subIndexProp).matches()) {
					if (indexes == null) {
						indexes = new HashSet<>();
					}
					indexes.add(subIndexProp.substring(1, subIndexProp.indexOf(']')));
				}
			}
		}
		return indexes != null ? indexes.size() : (0);
	}

	private static class BinderNode {
		final Object model;
		final String path;

		public BinderNode(Object model, String path) {
			this.model = model;
			this.path = path;
		}
	}
}
