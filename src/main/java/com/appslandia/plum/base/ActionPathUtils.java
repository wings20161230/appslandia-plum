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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.SplitUtils;
import com.appslandia.common.utils.URLEncoding;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class ActionPathUtils {

	// path: /{language}/{controller}/{action}(/{parameter})*.{extension}
	public static List<String> parsePathItems(String path, String extension) {
		int startIdx = 1;
		int endIdx;
		List<String> list = new ArrayList<>(8);
		while ((endIdx = path.indexOf(('/'), startIdx)) != -1) {
			list.add(path.substring(startIdx, endIdx));
			startIdx = endIdx + 1;
		}
		// page: {parameter}.{extension}
		String page = (startIdx == 1) ? path.substring(1) : path.substring(startIdx, path.length());
		list.add(page.substring(0, page.length() - extension.length()));
		return list;
	}

	// pathItem: {parameter}(-{parameter})+
	public static boolean parsePathParams(String pathItem, PathParam[] pathParams, Map<String, String> pathParamMap) {
		int paramIdx = 0;
		int startIdx = 0;
		while (true) {
			// case: paramIdx == pathParams.length - 1
			if (paramIdx == pathParams.length - 1) {
				if (startIdx < pathItem.length()) {
					String paramVal = pathItem.substring(startIdx).trim();
					if (paramVal.isEmpty()) {
						return false;
					}
					pathParamMap.put(pathParams[paramIdx].getParamName(), paramVal);
					return true;
				} else {
					return false;
				}
			} else {
				// case: paramIdx < pathParams.length - 1
				int endIdx = pathItem.indexOf(('-'), startIdx);
				if (endIdx >= 0) {
					String paramVal = pathItem.substring(startIdx, endIdx).trim();
					if (paramVal.isEmpty()) {
						return false;
					}
					pathParamMap.put(pathParams[paramIdx].getParamName(), paramVal);
					startIdx = endIdx + 1;
				} else {
					return false;
				}
			}
			paramIdx++;
		}
	}

	private static final PathParam[] EMPTY_PATH_PARAMS = {};
	private static final String PARAM_FORMAT = "\\{[a-z\\d._]+\\}";
	private static final Pattern PARAM_PATTERN = Pattern.compile(PARAM_FORMAT, Pattern.CASE_INSENSITIVE);
	private static final Pattern PATH_PARAMS_PATTERN = Pattern.compile(String.format("(/%s(-%s)*)+", PARAM_FORMAT, PARAM_FORMAT), Pattern.CASE_INSENSITIVE);

	// pathParams: (/{parameter}(-{parameter})*)+
	public static PathParam[] parsePathParams(String pathParams) {
		if (pathParams == null) {
			return EMPTY_PATH_PARAMS;
		}
		AssertUtils.assertTrue(PATH_PARAMS_PATTERN.matcher(pathParams).matches(), "pathParams is invalid (value=" + pathParams + ")");

		List<String> pathItems = SplitUtils.split(pathParams, '/');
		PathParam[] params = new PathParam[pathItems.size()];

		for (int i = 0; i < pathItems.size(); i++) {
			String pathItem = pathItems.get(i);

			if (PARAM_PATTERN.matcher(pathItem).matches()) {
				params[i] = parsePathParam(pathItem);
			} else {
				params[i] = new PathParam(parseInlinePathParams(pathItem));
			}
		}
		return params;
	}

	// pathItem: {parameter}
	private static PathParam parsePathParam(String pathItem) {
		return new PathParam(pathItem.substring(1, pathItem.length() - 1));
	}

	// pathItem: {parameter}(-{parameter})+
	private static PathParam[] parseInlinePathParams(String pathItem) {
		List<String> list = SplitUtils.split(pathItem, '-');
		PathParam[] pathParams = new PathParam[list.size()];
		for (int i = 0; i < list.size(); i++) {
			pathParams[i] = parsePathParam(list.get(i));
		}
		return pathParams;
	}

	private static boolean hasPathParam(String pathParam, PathParam[] pathParams) {
		for (PathParam param : pathParams) {
			if (param.hasPathParam(pathParam)) {
				return true;
			}
		}
		return false;
	}

	public static void addQueryParams(StringBuilder url, Map<String, Object> parameterMap, PathParam[] pathParams) {
		boolean isFirstParam = true;
		for (Map.Entry<String, Object> param : parameterMap.entrySet()) {
			if (ActionPathUtils.hasPathParam(param.getKey(), pathParams)) {
				continue;
			}
			if (isFirstParam) {
				isFirstParam = false;
			} else {
				url.append('&');
			}
			url.append(param.getKey()).append('=');
			if (param.getValue() != null) {
				url.append(URLEncoding.encode(param.getValue().toString()));
			}
		}
	}

	public static void addPathParams(StringBuilder url, Map<String, Object> parameterMap, PathParam[] pathParams) {
		for (PathParam pathParam : pathParams) {
			if (pathParam.getParamName() != null) {
				Object value = parameterMap.get(pathParam.getParamName());
				if (value == null) {
					throw new IllegalArgumentException("Path parameter is required (name=" + pathParam.getParamName() + ")");
				}
				url.append('/').append(value);
				continue;
			}
			// getName() is null
			boolean isFirstSub = true;
			for (PathParam subParam : pathParam.getPathParams()) {
				Object value = parameterMap.get(subParam.getParamName());
				if (value == null) {
					throw new IllegalArgumentException("Path parameter is required (name=" + subParam.getParamName() + ")");
				}
				if (isFirstSub) {
					url.append('/').append(value);
					isFirstSub = false;
				} else {
					url.append('-').append(value);
				}
			}
		}
	}
}
