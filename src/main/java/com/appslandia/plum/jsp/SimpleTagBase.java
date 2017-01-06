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

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.appslandia.common.base.StringWriter;
import com.appslandia.common.utils.StringUtils;
import com.appslandia.plum.base.ActionContext;
import com.appslandia.plum.base.ActionParser;
import com.appslandia.plum.base.ModelState;
import com.appslandia.plum.base.ServletUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public abstract class SimpleTagBase extends SimpleTagSupport {

	protected String evalJspBody() throws JspException, IOException {
		if (getJspBody() != null) {
			StringWriter out = new StringWriter();
			getJspBody().invoke(out);
			return out.toString();
		}
		return StringUtils.EMPTY_STRING;
	}

	public ServletContext getServletContext() {
		return ((PageContext) getJspContext()).getServletContext();
	}

	public PageContext getPageContext() {
		return (PageContext) getJspContext();
	}

	protected HttpServletRequest getRequest() {
		return (HttpServletRequest) ((PageContext) getJspContext()).getRequest();
	}

	protected HttpServletResponse getResponse() {
		return (HttpServletResponse) ((PageContext) getJspContext()).getResponse();
	}

	protected ActionContext getActionContext() {
		return (ActionContext) ((PageContext) getJspContext()).getRequest().getAttribute(ServletUtils.REQUEST_ATTRIBUTE_ACTION_CONTEXT);
	}

	protected ActionParser getActionParser() {
		return (ActionParser) ((PageContext) getJspContext()).getServletContext().getAttribute(ServletUtils.CONTEXT_ATTRIBUTE_ACTION_PARSER);
	}

	protected ModelState getModelState() {
		return ServletUtils.getModelState((HttpServletRequest) ((PageContext) getJspContext()).getRequest());
	}
}
