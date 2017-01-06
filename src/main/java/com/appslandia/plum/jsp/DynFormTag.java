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
import java.nio.charset.StandardCharsets;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import com.appslandia.common.utils.ContentTypes;
import com.appslandia.plum.base.HttpMethod;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@Tag(name = "dynForm", bodyContent = "scriptless")
public class DynFormTag extends UITagBase {

	protected String name;
	protected String controller;
	protected String action;

	protected String method = HttpMethod.POST;
	protected String acceptCharset = StandardCharsets.UTF_8.name();
	protected String enctype = ContentTypes.APP_FORM_URLENCODED;

	protected boolean autocomplete;
	protected boolean novalidate;

	protected String _action;
	protected String _body;

	@Override
	protected String getTagName() {
		return "form";
	}

	@Override
	protected void initTag() throws JspException, IOException {
		// Evaluate body
		this._body = evalJspBody();

		// Defaults
		if (this.controller == null) {
			this.controller = getActionContext().getController();
		}

		// Action
		this._action = this.getActionParser().toActionUrl(this.getRequest(), null, controller, this.action, this.dynamicAttributes);

		// encodeUrl
		this._action = this.getResponse().encodeURL(this._action);
	}

	@Override
	protected void writeDynamicAttributes(JspWriter out) throws JspException, IOException {
	}

	@Override
	protected void writeAttributes(JspWriter out) throws JspException, IOException {
		HtmlUtils.attribute(out, "id", this.id);
		HtmlUtils.attribute(out, "name", this.name);
		HtmlUtils.attribute(out, "action", this._action);

		HtmlUtils.attribute(out, "method", this.method);
		HtmlUtils.attribute(out, "accept-charset", this.acceptCharset);
		HtmlUtils.attribute(out, "enctype", this.enctype);

		HtmlUtils.autocomplete(out, this.autocomplete);
		HtmlUtils.novalidate(out, this.novalidate);

		HtmlUtils.hidden(out, this.hidden);
		HtmlUtils.attribute(out, "data-tag", this.datatag);

		HtmlUtils.attribute(out, "class", this.cssClass);
		HtmlUtils.attribute(out, "style", this.cssStyle);
		HtmlUtils.escapeAttribute(out, "title", this.title);
	}

	@Override
	protected boolean hasBody() {
		return true;
	}

	@Override
	protected void writeBody(JspWriter out) throws JspException, IOException {
		out.write(this._body);
	}

	@Attribute(required = false, rtexprvalue = false)
	public void setName(String name) {
		this.name = name;
	}

	@Attribute(required = false, rtexprvalue = false)
	public void setController(String controller) {
		this.controller = controller;
	}

	@Attribute(required = true, rtexprvalue = false)
	public void setAction(String action) {
		this.action = action;
	}

	@Attribute(required = false, rtexprvalue = false)
	public void setMethod(String method) {
		this.method = method;
	}

	@Attribute(required = false, rtexprvalue = false)
	public void setAcceptCharset(String acceptCharset) {
		this.acceptCharset = acceptCharset;
	}

	@Attribute(required = false, rtexprvalue = false)
	public void setEnctype(String enctype) {
		this.enctype = enctype;
	}

	@Attribute(required = false, rtexprvalue = false)
	public void setAutocomplete(boolean autocomplete) {
		this.autocomplete = autocomplete;
	}

	@Attribute(required = false, rtexprvalue = false)
	public void setNovalidate(boolean novalidate) {
		this.novalidate = novalidate;
	}
}
