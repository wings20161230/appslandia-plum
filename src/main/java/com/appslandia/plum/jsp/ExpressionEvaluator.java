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

import javax.el.ExpressionFactory;
import javax.servlet.jsp.JspFactory;
import javax.servlet.jsp.PageContext;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public abstract class ExpressionEvaluator {

	private static volatile ExpressionEvaluator __instance;
	private static final Object mutex = new Object();

	public static ExpressionEvaluator getInstance() {
		ExpressionEvaluator obj = __instance;
		if (obj == null) {
			synchronized (mutex) {
				if ((obj = __instance) == null) {
					__instance = obj = new ExpressionEvaluatorImpl();
				}
			}
		}
		return obj;
	}

	public static void setInstance(ExpressionEvaluator instance) {
		ExpressionEvaluator.__instance = instance;
	}

	public abstract Object getValue(PageContext pc, String property);

	private static class ExpressionEvaluatorImpl extends ExpressionEvaluator {

		private static final JspFactory jspFactory = JspFactory.getDefaultFactory();

		@SuppressWarnings("el-syntax")
		@Override
		public Object getValue(PageContext pc, String property) {
			String expr = "${" + property + "}";
			return getExpressionFactory(pc).createValueExpression(pc.getELContext(), expr, Object.class).getValue(pc.getELContext());
		}

		protected ExpressionFactory getExpressionFactory(PageContext pc) {
			return jspFactory.getJspApplicationContext(pc.getServletContext()).getExpressionFactory();
		}
	}
}
