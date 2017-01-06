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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@Tag(name = "textArea")
public class TextAreaTag extends TextEditTag {

	protected String cols;
	protected String rows;
	protected boolean hardWrap;

	@Override
	protected String getTagName() {
		return "textarea";
	}

	@Override
	protected boolean hasBody() {
		return true;
	}

	@Override
	protected void writeAttributes(JspWriter out) throws JspException, IOException {
		HtmlUtils.attribute(out, "id", this.id);
		HtmlUtils.attribute(out, "name", this.name);

		HtmlUtils.attribute(out, "maxlength", this.maxlength);
		HtmlUtils.attribute(out, "rows", this.rows);
		HtmlUtils.attribute(out, "cols", this.cols);
		HtmlUtils.wrap(out, this.hardWrap);

		HtmlUtils.hidden(out, this.hidden);
		HtmlUtils.readonly(out, this.readonly);
		HtmlUtils.disabled(out, this.disabled);
		HtmlUtils.autofocus(out, this.autofocus);
		HtmlUtils.required(out, this.required);

		HtmlUtils.attribute(out, "form", this.form);
		HtmlUtils.attribute(out, "data-tag", this.datatag);

		HtmlUtils.attribute(out, "class", this.cssClass);
		HtmlUtils.attribute(out, "style", this.cssStyle);
		HtmlUtils.escapeAttribute(out, "placeholder", this.placeholder);
		HtmlUtils.escapeAttribute(out, "title", this.title);
	}

	@Override
	protected void writeBody(JspWriter out) throws JspException, IOException {
		if (this.value != null) {
			HtmlEscaper.writeEscapedXml((String) this.value, out);
		}
	}

	@Attribute(required = false, rtexprvalue = false)
	public void setRows(String rows) {
		this.rows = rows;
	}

	@Attribute(required = false, rtexprvalue = false)
	public void setCols(String cols) {
		this.cols = cols;
	}

	@Attribute(required = false, rtexprvalue = false)
	public void setHardWrap(boolean hardWrap) {
		this.hardWrap = hardWrap;
	}
}
