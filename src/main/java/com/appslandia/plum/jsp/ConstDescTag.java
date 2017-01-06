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

import com.appslandia.plum.base.ServletUtils;
import com.appslandia.plum.web.ConstDescProvider;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@Tag(name = "constDesc", dynamicAttributes = false)
public class ConstDescTag extends SimpleTagBase {

	protected String constGroup;
	protected Object value;

	protected ConstDescProvider getConstDescProvider() {
		return (ConstDescProvider) getServletContext().getAttribute(ServletUtils.CONTEXT_ATTRIBUTE_CONST_DESC_PROVIDER);
	}

	@Override
	public void doTag() throws JspException, IOException {
		if (this.value != null) {
			String descKey = this.getConstDescProvider().getDescKey(this.constGroup, this.value);
			if (descKey != null) {
				HtmlEscaper.writeEscapedXml(this.getActionContext().getMessage(descKey), this.getJspContext().getOut());
			} else {
				HtmlEscaper.writeEscapedXml(buildMissingDesc(), this.getJspContext().getOut());
			}
		}
	}

	protected String buildMissingDesc() {
		return "?" + this.value;
	}

	@Attribute(required = true, rtexprvalue = false)
	public void setConstGroup(String constGroup) {
		this.constGroup = constGroup;
	}

	@Attribute(required = true, rtexprvalue = true)
	public void setValue(Object value) {
		this.value = value;
	}
}
