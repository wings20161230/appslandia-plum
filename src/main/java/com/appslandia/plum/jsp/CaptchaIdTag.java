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
import com.appslandia.plum.base.ServletUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@Tag(name = "captchaId", dynamicAttributes = false)
public class CaptchaIdTag extends SimpleTagBase {

	@Override
	public void doTag() throws JspException, IOException {
		// captchaId
		String captchaId = (String) this.getRequest().getAttribute(ServletUtils.REQUEST_ATTRIBUTE_CAPTCHA_ID);
		AssertUtils.assertNotNull(captchaId);

		JspWriter out = this.getJspContext().getOut();

		out.write("<input id=\"");
		out.write(ServletUtils.PARAM_CAPTCHA_ID);

		out.write("\" name=\"");
		out.write(ServletUtils.PARAM_CAPTCHA_ID);

		out.write("\" value=\"");
		out.write(captchaId);

		out.write("\" type=\"hidden\" />");
	}
}
