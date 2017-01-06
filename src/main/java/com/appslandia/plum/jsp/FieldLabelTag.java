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
@Tag(name = "fieldLabel")
public class FieldLabelTag extends UITagBase {

	protected String fieldName;
	protected String labelKey;
	protected String form;

	protected String _for;

	@Override
	protected String getTagName() {
		return "label";
	}

	@Override
	protected void initTag() throws JspException, IOException {
		// _for
		this._for = TagUtils.parseId(this.fieldName);

		// id
		this.id = "label_" + this._for;
	}

	@Override
	protected void writeAttributes(JspWriter out) throws JspException, IOException {
		HtmlUtils.attribute(out, "id", this.id);
		HtmlUtils.attribute(out, "for", this._for);

		HtmlUtils.attribute(out, "form", this.form);
		HtmlUtils.hidden(out, this.hidden);

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
		HtmlEscaper.writeEscapedXml(this.getActionContext().getMessage(this.labelKey), out);
	}

	@Override
	public void setId(String id) {
	}

	@Attribute(required = true, rtexprvalue = false)
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	@Attribute(required = true, rtexprvalue = false)
	public void setLabelKey(String labelKey) {
		this.labelKey = labelKey;
	}

	@Attribute(required = false, rtexprvalue = false)
	public void setForm(String form) {
		this.form = form;
	}
}
