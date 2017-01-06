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

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.SimpleTag;

import com.appslandia.common.utils.AssertUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@Tag(name = "option", dynamicAttributes = false)
public class OptionTag implements SimpleTag {
	protected JspTag parent;

	protected String value;
	protected Object name;

	@Override
	public void doTag() throws JspException, IOException {
		AssertUtils.assertNotNull(this.parent);
		((SelectTag) this.parent).putOption(this.value, this.name);
	}

	@Attribute(required = true, rtexprvalue = false)
	public void setValue(String value) {
		this.value = value;
	}

	@Attribute(required = false, rtexprvalue = true)
	public void setName(Object name) {
		this.name = name;
	}

	@Override
	public void setParent(JspTag parent) {
		this.parent = parent;
	}

	@Override
	public JspTag getParent() {
		return this.parent;
	}

	@Override
	public void setJspContext(JspContext pc) {
	}

	@Override
	public void setJspBody(JspFragment jspBody) {
	}
}
