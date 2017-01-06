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
import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.DynamicAttributes;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public abstract class UITagBase extends SimpleTagBase implements DynamicAttributes {

	// Global attributes
	protected String id;
	protected boolean hidden;
	protected String datatag;

	protected String cssClass;
	protected String cssStyle;
	protected String title;

	protected Map<String, Object> dynamicAttributes;

	@Override
	public void doTag() throws JspException, IOException {
		this.initTag();
		this.writeTag(this.getJspContext().getOut());
	}

	protected abstract void writeAttributes(JspWriter out) throws JspException, IOException;

	protected void writeDynamicAttributes(JspWriter out) throws JspException, IOException {
		if (this.dynamicAttributes != null) {
			HtmlUtils.dynamicAttributes(out, this.dynamicAttributes);
		}
	}

	@Override
	public void setDynamicAttribute(String uri, String name, Object value) throws JspException {
		if (this.dynamicAttributes == null) {
			this.dynamicAttributes = new HashMap<>();
		}
		this.dynamicAttributes.put(name, value);
	}

	abstract protected void initTag() throws JspException, IOException;

	protected abstract String getTagName();

	protected boolean hasBody() {
		return false;
	}

	protected void writeBody(JspWriter out) throws JspException, IOException {
	}

	protected void writeTag(JspWriter out) throws JspException, IOException {
		out.write('<');
		out.write(this.getTagName());

		this.writeAttributes(out);
		this.writeDynamicAttributes(out);

		if (this.hasBody()) {
			out.write('>');
			this.writeBody(out);
			out.write("</");
			out.write(this.getTagName());
			out.write('>');

		} else {
			out.write(" />");
		}
	}

	@Attribute(required = false, rtexprvalue = false)
	public void setId(String id) {
		this.id = id;
	}

	@Attribute(required = false, rtexprvalue = true)
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	@Attribute(required = false, rtexprvalue = true)
	public void setDatatag(Object datatag) {
		this.datatag = datatag != null ? datatag.toString() : (null);
	}

	@Attribute(required = false, rtexprvalue = true)
	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	@Attribute(required = false, rtexprvalue = true)
	public void setCssStyle(String cssStyle) {
		this.cssStyle = cssStyle;
	}

	@Attribute(required = false, rtexprvalue = true)
	public void setTitle(String title) {
		this.title = title;
	}
}
