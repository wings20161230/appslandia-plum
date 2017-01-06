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

import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.StringUtils;
import com.appslandia.plum.base.ServletUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public abstract class ValueBaseTag extends UITagBase {

	protected String form;
	protected String name;
	protected Object value;
	protected String property;

	protected boolean required;

	protected String getInvalidValue(String name) {
		return StringUtils.trimToNull(this.getRequest().getParameter(this.name));
	}

	@Override
	protected void initTag() throws JspException, IOException {
		boolean isValid = false;

		// property?
		if (this.property != null) {

			// __model.property.property
			AssertUtils.assertTrue(this.property.startsWith(ServletUtils.REQUEST_ATTRIBUTE_MODEL));

			this.name = this.property.substring(this.property.indexOf('.') + 1);
			this.id = TagUtils.parseId(this.name);

			if ((isValid = this.getModelState().isFieldValid(this.name))) {
				this.value = ExpressionEvaluator.getInstance().getValue(this.getPageContext(), this.property);
			} else {
				this.value = getInvalidValue(this.name);
			}
		} else {
			// Name/value
			AssertUtils.assertNotNull(this.name);

			// Invalid?
			if ((isValid = this.getModelState().isFieldValid(this.name)) == false) {
				this.value = getInvalidValue(this.name);
			}
		}

		// Format value
		if (this.value != null) {
			if (this.value.getClass() == String.class) {
				this.value = StringUtils.trimToNull((String) this.value);
			} else {
				this.value = formatValue(this.value);
			}
		}

		// cssClass
		if (isValid == false) {
			if (this.cssClass == null) {
				this.cssClass = "field-error";
			} else {
				this.cssClass = this.cssClass + " field-error";
			}
		}
	}

	protected String formatValue(Object value) {
		return String.valueOf(value);
	}

	@Attribute(required = false, rtexprvalue = false)
	public void setForm(String form) {
		this.form = form;
	}

	@Attribute(required = false, rtexprvalue = false)
	public void setName(String name) {
		this.name = name;
	}

	@Attribute(required = false, rtexprvalue = true)
	public void setValue(Object value) {
		this.value = value;
	}

	@Attribute(required = false, rtexprvalue = false)
	public void setProperty(String property) {
		this.property = property;
	}

	@Attribute(required = false, rtexprvalue = true)
	public void setRequired(boolean required) {
		this.required = required;
	}
}
