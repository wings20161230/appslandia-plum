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
public abstract class InputTagBase extends ValueBaseTag {

	protected boolean disabled;
	protected boolean readonly;
	protected boolean autofocus;

	protected boolean writeHiddenTag() {
		return this.readonly;
	}

	@Override
	protected void writeTag(JspWriter out) throws JspException, IOException {
		super.writeTag(out);

		// Write hidden?
		if (writeHiddenTag()) {
			out.write("<input name=\"");
			out.write(this.name);

			out.write("\" value=\"");
			if (this.value != null) {
				HtmlEscaper.writeEscapedXml((String) this.value, out);
			}
			out.write("\" type=\"hidden\" />");
		}
	}

	@Attribute(required = false, rtexprvalue = true)
	public void setAutofocus(boolean autofocus) {
		this.autofocus = autofocus;
	}

	@Attribute(required = false, rtexprvalue = true)
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	@Attribute(required = false, rtexprvalue = true)
	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}
}
