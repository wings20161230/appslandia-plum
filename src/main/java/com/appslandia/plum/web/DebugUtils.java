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

package com.appslandia.plum.web;

import java.lang.reflect.Method;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.appslandia.common.base.TextBuilder;
import com.appslandia.common.base.ToStringBuilder;
import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.ReflectionUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class DebugUtils {

	public static String requestAttrs(HttpServletRequest request, String attrName, int toStringLevel) {
		return buildAttrInfo(request, attrName, toStringLevel);
	}

	public static String sessionAttrs(HttpServletRequest request, String attrName, int toStringLevel) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return "No session";
		}
		return buildAttrInfo(session, attrName, toStringLevel);
	}

	public static String contextAttrs(HttpServletRequest request, String attrName, int toStringLevel) {
		return buildAttrInfo(request.getServletContext(), attrName, toStringLevel);
	}

	@SuppressWarnings("unchecked")
	private static String buildAttrInfo(Object attrsObj, String attrName, int toStringLevel) {
		if (attrName != null) {

			Method getAttributeNames = ReflectionUtils.findMethod(attrsObj.getClass(), "getAttributeNames");
			AssertUtils.assertNotNull(getAttributeNames);
			Enumeration<String> attrNames = null;
			try {
				attrNames = (Enumeration<String>) getAttributeNames.invoke(attrsObj);
			} catch (Exception ignore) {
			}

			TextBuilder tb = new TextBuilder();
			while (attrNames.hasMoreElements()) {
				String attr = attrNames.nextElement();
				if (attr.contains(attrName)) {

					Method getAttribute = ReflectionUtils.findMethod(attrsObj.getClass(), "getAttribute");
					AssertUtils.assertNotNull(getAttribute);
					Object value = null;
					try {
						value = getAttribute.invoke(attrsObj, attr);
					} catch (Exception ignore) {
					}

					if (value == null) {
						tb.append(attr).append(": ").append(null).appendln();
					} else {
						tb.append(attr).append(": ").append(new ToStringBuilder(toStringLevel).toString(value)).appendln();
					}
				}
			}
			if (tb.length() > 0) {
				return attrName + ": " + tb.toString();
			}
			return attrName + ": No matches.";
		}
		return new ToStringBuilder(toStringLevel).toStringAttributes(attrsObj);
	}
}
