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

import com.appslandia.common.base.ParameterMap;
import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.StringUtils;
import com.appslandia.plum.base.ServletUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@Tag(name = "captchaImage", dynamicAttributes = false)
public class CaptchaImageTag extends UITagBase {

	protected String controller;

	protected String width;
	protected String height;
	protected String alt;

	protected String _src;

	@Override
	protected String getTagName() {
		return "img";
	}

	@Override
	protected void initTag() throws JspException, IOException {
		// Defaults
		if (this.alt == null) {
			this.alt = StringUtils.EMPTY_STRING;
		}

		String captchaId = (String) this.getRequest().getAttribute(ServletUtils.REQUEST_ATTRIBUTE_CAPTCHA_ID);
		AssertUtils.assertNotNull(captchaId);
		ParameterMap paramMap = new ParameterMap().of("captchaId", captchaId);

		this._src = this.getActionParser().toActionUrl(this.getRequest(), null, this.controller, ServletUtils.ACTION_INDEX, paramMap);
		this._src = this.getResponse().encodeURL(this._src);
	}

	@Override
	protected void writeAttributes(JspWriter out) throws JspException, IOException {
		HtmlUtils.attribute(out, "id", this.id);
		HtmlUtils.attribute(out, "src", this._src);

		HtmlUtils.attribute(out, "width", this.width);
		HtmlUtils.attribute(out, "height", this.height);
		HtmlUtils.escapeAttribute(out, "alt", this.alt);

		HtmlUtils.attribute(out, "class", this.cssClass);
		HtmlUtils.attribute(out, "style", this.cssStyle);
		HtmlUtils.escapeAttribute(out, "title", this.title);
	}

	@Attribute(required = true, rtexprvalue = false)
	public void setController(String controller) {
		this.controller = controller;
	}

	@Attribute(required = false, rtexprvalue = false)
	public void setWidth(String width) {
		this.width = width;
	}

	@Attribute(required = false, rtexprvalue = false)
	public void setHeight(String height) {
		this.height = height;
	}

	@Attribute(required = false, rtexprvalue = true)
	public void setAlt(String alt) {
		this.alt = alt;
	}
}
