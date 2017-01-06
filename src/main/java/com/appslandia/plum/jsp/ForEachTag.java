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
import java.util.List;

import javax.servlet.jsp.JspException;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@Tag(name = "forEach", bodyContent = "scriptless", dynamicAttributes = false)
public class ForEachTag extends SimpleTagBase {

	protected List<Object> items;
	protected String var;

	@Override
	public void doTag() throws JspException, IOException {
		if (this.getJspBody() == null) {
			return;
		}
		for (Object item : this.items) {
			this.getPageContext().setAttribute(this.var, item);
			this.getJspBody().invoke(null);
		}
		this.getPageContext().setAttribute(this.var, null);
	}

	@Attribute(required = true, rtexprvalue = true)
	public void setItems(List<Object> items) {
		this.items = items;
	}

	@Attribute(required = true, rtexprvalue = false)
	public void setVar(String var) {
		this.var = var;
	}
}
