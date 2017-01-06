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
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class HtmlUtils {

	public static void hidden(JspWriter out, boolean hidden) throws IOException {
		if (hidden) {
			out.write(" hidden=\"hidden\"");
		}
	}

	public static void autofocus(JspWriter out, boolean autofocus) throws IOException {
		if (autofocus) {
			out.write(" autofocus=\"autofocus\"");
		}
	}

	public static void disabled(JspWriter out, boolean disabled) throws IOException {
		if (disabled) {
			out.write(" disabled=\"disabled\"");
		}
	}

	public static void readonly(JspWriter out, boolean readonly) throws IOException {
		if (readonly) {
			out.write(" readonly=\"readonly\"");
		}
	}

	public static void required(JspWriter out, boolean required) throws IOException {
		if (required) {
			out.write(" required=\"required\"");
		}
	}

	public static void checked(JspWriter out, boolean checked) throws IOException {
		if (checked) {
			out.write(" checked=\"checked\"");
		}
	}

	public static void selected(JspWriter out, boolean selected) throws IOException {
		if (selected) {
			out.write(" selected=\"selected\"");
		}
	}

	public static void autocomplete(JspWriter out, boolean autocomplete) throws IOException {
		if (autocomplete == false) {
			out.write(" autocomplete=\"");
			out.write(Long.toString(System.currentTimeMillis()));
			out.write("\"");
		}
	}

	public static void wrap(JspWriter out, boolean hard) throws IOException {
		if (hard) {
			out.write(" wrap=\"hard\"");
		}
	}

	public static void multiple(JspWriter out, boolean multiple) throws IOException {
		if (multiple) {
			out.write(" multiple=\"multiple\"");
		}
	}

	public static void novalidate(JspWriter out, boolean novalidate) throws IOException {
		if (novalidate) {
			out.write(" novalidate=\"novalidate\"");
		}
	}

	public static void dynamicAttributes(JspWriter out, Map<String, Object> dynamicAttributes) throws JspException, IOException {
		for (Entry<String, Object> attr : dynamicAttributes.entrySet()) {
			if (attr.getValue() != null) {
				out.write(' ');
				out.write(attr.getKey());
				out.write("=\"");
				HtmlEscaper.writeEscapedXml(attr.getValue().toString(), out);
				out.write('"');
			}
		}
	}

	public static void attribute(JspWriter out, String name, String value) throws IOException {
		if (value != null) {
			out.write(' ');
			out.write(name);
			out.write("=\"");
			out.write(value);
			out.write('"');
		}
	}

	public static void escapeAttribute(JspWriter out, String name, String value) throws IOException {
		if (value != null) {
			out.write(' ');
			out.write(name);
			out.write("=\"");
			HtmlEscaper.writeEscapedXml(value, out);
			out.write('"');
		}
	}

	public static void escapeValueAttribute(JspWriter out, String name, String value) throws IOException {
		out.write(' ');
		out.write(name);
		out.write("=\"");
		if (value != null) {
			HtmlEscaper.writeEscapedXml(value, out);
		}
		out.write('"');
	}
}
