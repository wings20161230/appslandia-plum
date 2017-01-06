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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import com.appslandia.common.utils.SplitUtils;
import com.appslandia.common.utils.StringUtils;
import com.appslandia.plum.base.ModelState;
import com.appslandia.plum.base.ModelState.FieldErrors;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@Tag(name = "formErrors", bodyContent = "scriptless")
public class FormErrorsTag extends UITagBase {

	protected String contextId;
	protected String titleKey;
	protected boolean excludePropertyErrors;

	protected String _fieldOrders;

	@Override
	protected String getTagName() {
		return "div";
	}

	@Override
	protected boolean hasBody() {
		return true;
	}

	public void setFieldOrders(String fieldOrders) {
		this._fieldOrders = fieldOrders;
	}

	@Override
	protected void initTag() throws JspException, IOException {
		// cssClass
		if (this.cssClass == null) {
			this.cssClass = "form-errors";
		} else {
			this.cssClass = this.cssClass + " form-errors";
		}

		// cssStyle
		if (this.getModelState().isModelValid()) {
			if (this.cssStyle == null) {
				this.cssStyle = "display:none";
			} else {
				this.cssStyle = this.cssStyle + ";display:none";
			}
		}

		// Invoke body
		if (getJspBody() != null) {
			getJspBody().invoke(null);
		}
	}

	@Override
	protected void writeAttributes(JspWriter out) throws JspException, IOException {
		HtmlUtils.attribute(out, "class", this.cssClass);
		HtmlUtils.attribute(out, "style", this.cssStyle);
	}

	@Override
	protected void writeBody(JspWriter out) throws JspException, IOException {
		if (this.getModelState().isModelValid()) {
			return;
		}
		// Title
		if (this.titleKey != null) {
			out.write("<span>");
			HtmlEscaper.writeEscapedXml(this.getActionContext().getMessage(this.titleKey), out);
			out.write("</span>");
		}

		// Errors
		out.write("<ul>");

		// Sort Fields
		List<Entry<String, FieldErrors>> fields = new ArrayList<>(this.getModelState().entrySet());
		if (StringUtils.isNullOrEmpty(this._fieldOrders) == false) {
			Collections.sort(fields, buildFieldComparator(this._fieldOrders));
		}

		if (this.excludePropertyErrors) {
			for (Entry<String, FieldErrors> entry : fields) {
				if (ModelState.MODEL_FIELD.equals(entry.getKey())) {
					this.writeMessages(out, entry.getValue());
				}
			}
		} else {
			for (Entry<String, FieldErrors> entry : fields) {
				this.writeMessages(out, entry.getValue());
			}
		}
		out.write("</ul>");
	}

	protected void writeMessages(JspWriter out, FieldErrors fieldErrors) throws IOException {
		for (String error : fieldErrors.getErrors()) {
			out.write("<li>");
			HtmlEscaper.writeEscapedXml(error, out);
			out.write("</li>");
		}
	}

	protected static Comparator<Map.Entry<String, FieldErrors>> buildFieldComparator(String fieldOrders) {

		// Positions
		final Map<String, Integer> posMap = new HashMap<>();
		int pos = 0;
		for (String fieldName : SplitUtils.splitByComma(fieldOrders)) {
			posMap.put(fieldName, ++pos);
		}

		return new Comparator<Map.Entry<String, FieldErrors>>() {

			@Override
			public int compare(Entry<String, FieldErrors> f1, Entry<String, FieldErrors> f2) {
				Integer p1 = posMap.get(f1.getKey());
				Integer p2 = posMap.get(f2.getKey());
				if (p1 == null) {
					p1 = Integer.MAX_VALUE;
				}
				if (p2 == null) {
					p2 = Integer.MAX_VALUE;
				}
				return p1.compareTo(p2);
			}
		};
	}

	@Attribute(required = false, rtexprvalue = false)
	public void setContextId(String contextId) {
		this.contextId = contextId;
	}

	@Attribute(required = false, rtexprvalue = false)
	public void setTitleKey(String titleKey) {
		this.titleKey = titleKey;
	}

	@Attribute(required = false, rtexprvalue = false)
	public void setExcludePropertyErrors(boolean excludePropertyErrors) {
		this.excludePropertyErrors = excludePropertyErrors;
	}
}
