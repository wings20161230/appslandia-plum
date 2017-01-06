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

import com.appslandia.plum.base.ServletUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@Tag(name = "captchaWords", dynamicAttributes = false)
public class CaptchaWordsTag extends UITagBase {

	protected String form;
	protected String maxlength;
	protected boolean required;

	protected boolean autofocus;
	protected String placeholder;

	@Override
	protected String getTagName() {
		return "input";
	}

	@Override
	protected void initTag() throws JspException, IOException {
		// cssClass
		if (this.getModelState().isFieldValid(ServletUtils.PARAM_CAPTCHA_WORDS) == false) {
			if (this.cssClass == null) {
				this.cssClass = "field-error";
			} else {
				this.cssClass = this.cssClass + " field-error";
			}
		}
	}

	@Override
	protected void writeAttributes(JspWriter out) throws JspException, IOException {
		HtmlUtils.attribute(out, "id", ServletUtils.PARAM_CAPTCHA_WORDS);
		HtmlUtils.attribute(out, "type", "text");
		HtmlUtils.attribute(out, "name", ServletUtils.PARAM_CAPTCHA_WORDS);

		HtmlUtils.attribute(out, "maxlength", this.maxlength);
		HtmlUtils.autocomplete(out, false);

		HtmlUtils.autofocus(out, this.autofocus);
		HtmlUtils.required(out, this.required);
		HtmlUtils.attribute(out, "form", this.form);

		HtmlUtils.attribute(out, "class", this.cssClass);
		HtmlUtils.attribute(out, "style", this.cssStyle);
		HtmlUtils.escapeAttribute(out, "placeholder", this.placeholder);
		HtmlUtils.escapeAttribute(out, "title", this.title);
	}

	@Attribute(required = false, rtexprvalue = false)
	public void setForm(String form) {
		this.form = form;
	}

	@Attribute(required = false, rtexprvalue = false)
	public void setMaxlength(String maxlength) {
		this.maxlength = maxlength;
	}

	@Attribute(required = false, rtexprvalue = false)
	public void setRequired(boolean required) {
		this.required = required;
	}

	@Attribute(required = false, rtexprvalue = true)
	public void setAutofocus(boolean autofocus) {
		this.autofocus = autofocus;
	}

	@Attribute(required = false, rtexprvalue = true)
	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}
}
