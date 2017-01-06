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

import java.lang.reflect.Array;

import com.appslandia.common.base.FormatProvider;
import com.appslandia.common.formatters.Formatter;
import com.appslandia.common.formatters.FormatterException;
import com.appslandia.common.utils.StringUtils;
import com.appslandia.common.utils.TypeDefaults;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class ModelBinderUtils {

	public static Object parseArray(String[] paramValues, Class<?> elementType, ParseResult parseResult, Formatter formatter, FormatProvider formatProvider) {
		if (paramValues == null) {
			return null;
		}
		Object parsedArray = Array.newInstance(elementType, paramValues.length);
		for (int idx = 0; idx < paramValues.length; idx++) {
			Object obj = null;
			try {
				obj = formatter.parse(StringUtils.trimToNull(paramValues[idx]), formatProvider);
				if ((obj == null) && (elementType.isPrimitive())) {

					obj = TypeDefaults.defaultValue(elementType);
					parseResult.setError(ServletUtils.ERROR_MSGKEY_LIST_FAILED);
				}
			} catch (FormatterException ex) {
				obj = TypeDefaults.defaultValue(elementType);
				parseResult.setError(ServletUtils.ERROR_MSGKEY_LIST_FAILED);
			}
			Array.set(parsedArray, idx, obj);
		}
		return parsedArray;
	}

	public static Object parse(String paramValue, Class<?> targetType, ParseResult parseResult, Formatter formatter, FormatProvider formatProvider) {
		Object result = null;
		try {
			result = formatter.parse(paramValue, formatProvider);
			if ((result == null) && (targetType.isPrimitive())) {

				result = TypeDefaults.defaultValue(targetType);
				parseResult.setError(ServletUtils.ERROR_MSGKEY_FIELD_REQUIRED);
			}
		} catch (FormatterException ex) {
			result = TypeDefaults.defaultValue(targetType);
			parseResult.setError(ex.getMsgKey());
		}
		return result;
	}

	public static int toBitMaskValue(Object numberArray) {
		if (numberArray == null) {
			return (0);
		}
		int len = Array.getLength(numberArray);
		int sum = 0;
		for (int i = 0; i < len; i++) {
			Number v = (Number) Array.get(numberArray, i);
			sum += (v != null) ? v.intValue() : (0);
		}
		return sum;
	}

	public static class ParseResult {
		private boolean isError;
		private String msgKey;

		public boolean isError() {
			return this.isError;
		}

		public ParseResult setError(String msgKey) {
			this.isError = true;
			this.msgKey = msgKey;
			return this;
		}

		public String getMsgKey() {
			return this.msgKey;
		}
	}
}
