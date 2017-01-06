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

import com.appslandia.common.utils.AssertUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public abstract class CheckInputTag extends InputTagBase {

	protected Object submitValue;

	protected String labelKey;

	@Override
	protected String getTagName() {
		return "input";
	}

	protected abstract String getType();

	protected abstract boolean isChecked();

	@Override
	protected void initTag() throws JspException, IOException {
		AssertUtils.assertNotNull(this.submitValue, "submitValue is required.");
		super.initTag();
	}

	@Override
	protected boolean writeHiddenTag() {
		return this.readonly && isChecked();
	}

	@Override
	protected void writeAttributes(JspWriter out) throws JspException, IOException {
		HtmlUtils.attribute(out, "id", this.id);
		HtmlUtils.attribute(out, "type", this.getType());
		HtmlUtils.attribute(out, "name", this.name);

		HtmlUtils.attribute(out, "value", this.formatValue(this.submitValue));
		HtmlUtils.checked(out, this.isChecked());

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
	protected void writeTag(JspWriter out) throws JspException, IOException {
		super.writeTag(out);

		// labelKey?
		if (this.labelKey != null) {
			out.write(" ");
			HtmlEscaper.writeEscapedXml(this.getActionContext().getMessage(this.labelKey), out);
		}
	}

	@Override
	public void setDisabled(boolean disabled) {
		throw new UnsupportedOperationException();
	}

	@Attribute(required = true, rtexprvalue = false)
	public void setSubmitValue(Object submitValue) {
		this.submitValue = submitValue;
	}

	@Attribute(required = false, rtexprvalue = false)
	public void setLabelKey(String labelKey) {
		this.labelKey = labelKey;
	}
}
