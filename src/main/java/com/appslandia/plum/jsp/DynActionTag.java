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
import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.DynamicAttributes;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@Tag(name = "dynAction")
public class DynActionTag extends SimpleTagBase implements DynamicAttributes {

	protected String var;
	protected int scope = PageContext.PAGE_SCOPE;

	protected String language;
	protected String controller;
	protected String action;

	protected Map<String, Object> _params;

	@Override
	public void setDynamicAttribute(String uri, String localName, Object value) throws JspException {
		if (this._params == null) {
			this._params = new HashMap<>();
		}
		this._params.put(localName, value);
	}

	@Override
	public void doTag() throws JspException, IOException {
		// Defaults
		if (this.controller == null) {
			this.controller = getActionContext().getController();
		}

		// URL
		String _url = this.getActionParser().toActionUrl(this.getRequest(), this.language, this.controller, this.action, this._params);

		// encodeUrl
		_url = this.getResponse().encodeURL(_url);

		// Handle url
		if (this.var != null) {
			this.getPageContext().setAttribute(this.var, _url, this.scope);
		} else {
			this.getJspContext().getOut().write(_url);
		}
	}

	@Attribute(required = false, rtexprvalue = false)
	public void setVar(String var) {
		this.var = var;
	}

	@Attribute(required = false, rtexprvalue = false)
	public void setScope(String scope) {
		this.scope = TagUtils.getScope(scope);
	}

	@Attribute(required = false, rtexprvalue = false)
	public void setLanguage(String language) {
		this.language = language;
	}

	@Attribute(required = false, rtexprvalue = false)
	public void setController(String controller) {
		this.controller = controller;
	}

	@Attribute(required = true, rtexprvalue = false)
	public void setAction(String action) {
		this.action = action;
	}
}
