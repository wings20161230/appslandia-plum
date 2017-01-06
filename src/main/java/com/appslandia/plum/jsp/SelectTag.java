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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import com.appslandia.common.base.SelectItem;
import com.appslandia.common.utils.StringUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@Tag(name = "select", bodyContent = "scriptless")
public class SelectTag extends InputTagBase {

	protected List<SelectItem> options;
	protected String var;
	protected String size;
	protected boolean multiple;

	protected String optDisabledExpr;
	protected Map<String, Object> _options;

	@Override
	protected String getTagName() {
		return "select";
	}

	@Override
	protected boolean hasBody() {
		return true;
	}

	@Override
	protected void initTag() throws JspException, IOException {
		super.initTag();

		// Invoke body
		if (getJspBody() != null) {
			getJspBody().invoke(null);
		}

		// Defaults
		if (this.var == null) {
			this.var = "opt";
		}
	}

	public void putOption(String value, Object name) {
		if (this._options == null) {
			this._options = new LinkedHashMap<>();
		}
		this._options.put(value, name);
	}

	@Override
	protected void writeAttributes(JspWriter out) throws JspException, IOException {
		HtmlUtils.attribute(out, "id", this.id);
		HtmlUtils.attribute(out, "name", this.name);

		HtmlUtils.multiple(out, this.multiple);
		HtmlUtils.attribute(out, "size", this.size);

		HtmlUtils.hidden(out, this.hidden);
		HtmlUtils.disabled(out, this.readonly);
		HtmlUtils.autofocus(out, this.autofocus);
		HtmlUtils.required(out, this.required);

		HtmlUtils.attribute(out, "form", this.form);
		HtmlUtils.attribute(out, "data-tag", this.datatag);

		HtmlUtils.attribute(out, "class", this.cssClass);
		HtmlUtils.attribute(out, "style", this.cssStyle);
		HtmlUtils.escapeAttribute(out, "title", this.title);
	}

	@Override
	protected void writeBody(JspWriter out) throws JspException, IOException {
		// curValue
		String curValue = (String) this.value;

		// Declared Options
		if (this._options != null) {
			for (Entry<String, Object> opt : this._options.entrySet()) {

				// <option />
				out.write("<option");
				HtmlUtils.attribute(out, "value", (opt.getKey() == null) ? StringUtils.EMPTY_STRING : opt.getKey());
				HtmlUtils.selected(out, Objects.equals(opt.getKey(), curValue));
				out.write('>');

				if (opt.getValue() != null) {
					HtmlEscaper.writeEscapedXml(opt.getValue().toString(), out);
				}
				out.write("</option>");
			}
		}

		// Options
		if (this.options != null) {
			for (SelectItem opt : this.options) {
				this.getPageContext().setAttribute(this.var, opt);

				// optDisabled
				boolean optDisabled = false;
				if (this.optDisabledExpr != null) {
					optDisabled = Boolean.TRUE.equals(ExpressionEvaluator.getInstance().getValue(this.getPageContext(), this.optDisabledExpr));
				}

				// <option />
				out.write("<option");
				HtmlUtils.attribute(out, "value", (opt.getValue() == null) ? StringUtils.EMPTY_STRING : opt.getValue().toString());
				HtmlUtils.selected(out, Objects.equals(opt.getValue() != null ? opt.getValue().toString() : null, curValue));
				HtmlUtils.disabled(out, optDisabled);
				out.write('>');

				if (opt.getName() != null) {
					HtmlEscaper.writeEscapedXml(opt.getName(), out);
				}
				out.write("</option>");
			}
			this.getPageContext().setAttribute(this.var, null);
		}
	}

	@Override
	public void setDisabled(boolean disabled) {
		throw new UnsupportedOperationException();
	}

	@Attribute(required = false, rtexprvalue = true)
	public void setOptions(List<SelectItem> options) {
		this.options = options;
	}

	@Attribute(required = false, rtexprvalue = false)
	public void setVar(String var) {
		this.var = var;
	}

	@Attribute(required = false, rtexprvalue = false)
	public void setSize(String size) {
		this.size = size;
	}

	@Attribute(required = false, rtexprvalue = false)
	public void setMultiple(boolean multiple) {
		this.multiple = multiple;
	}

	@Attribute(required = false, rtexprvalue = false)
	public void setOptDisabledExpr(String optDisabledExpr) {
		this.optDisabledExpr = optDisabledExpr;
	}
}
