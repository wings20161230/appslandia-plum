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

package com.appslandia.plum.jsp;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import com.appslandia.common.base.TextBuilder;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class FunctionTldGenerator {

	public static void main(String[] args) {
		TextBuilder sb = new TextBuilder();
		generateFunction(Functions.class, sb);

		System.out.println(sb);
	}

	protected static void generateFunction(Class<?> clazz, TextBuilder sb) {
		for (Method method : clazz.getMethods()) {
			if (Modifier.isPublic(method.getModifiers()) && Modifier.isStatic(method.getModifiers())) {

				Function function = method.getDeclaredAnnotation(Function.class);
				if (function != null) {
					String name = function.name().length() == 0 ? method.getName() : function.name();
					String desc = function.description().length() == 0 ? name : function.description();

					if (sb.length() == 0) {
						sb.appendtab().append("<function>");
						sb.appendln();
					} else {
						sb.appendln(2).appendtab().append("<function>");
						sb.appendln();
					}

					sb.appendtab(2).append("<description>" + desc + "</description>");
					sb.appendln();
					sb.appendtab(2).append("<name>" + name + "</name>");
					sb.appendln();
					sb.appendtab(2).append("<function-class>" + clazz.getName() + "</function-class>");
					sb.appendln();
					sb.appendtab(2).append("<function-signature>" + functionSignature(method) + "</function-signature>");
					sb.appendln();

					sb.appendtab().append("</function>");
				}
			}
		}
	}

	public static String functionSignature(Method method) {
		StringBuilder sb = new StringBuilder();
		sb.append(method.getReturnType().getName()).append(" ");
		sb.append(method.getName()).append('(');
		boolean first = true;
		for (Class<?> paramType : method.getParameterTypes()) {
			if (first) {
				sb.append(paramType.getName());
				first = false;
			} else {
				sb.append(", ").append(paramType.getName());
			}
		}
		sb.append(')');
		return sb.toString();
	}
}
