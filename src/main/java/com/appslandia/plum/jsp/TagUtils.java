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

import javax.servlet.jsp.PageContext;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class TagUtils {

	public static final String REQUEST = "request";
	public static final String SESSION = "session";
	public static final String APPLICATION = "application";

	public static int getScope(String scope) {
		if (REQUEST.equalsIgnoreCase(scope)) {
			return PageContext.REQUEST_SCOPE;
		}
		if (SESSION.equalsIgnoreCase(scope)) {
			return PageContext.SESSION_SCOPE;
		}
		if (APPLICATION.equalsIgnoreCase(scope)) {
			return PageContext.APPLICATION_SCOPE;
		}
		return PageContext.PAGE_SCOPE;
	}

	public static String parseId(String fieldName) {
		int len = fieldName.length();
		int i = -1;
		char buf[] = new char[fieldName.length()];

		while (i < len - 1) {
			i++;
			char c = fieldName.charAt(i);
			buf[i] = ((c == '.') || (c == '[') || (c == ']')) ? ('_') : c;
		}
		return new String(buf);
	}
}
