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

import javax.servlet.jsp.JspWriter;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class HtmlEscaper {

	private static final int HIGHEST_SPECIAL = '>';
	private static char[][] specialCharactersRepresentation = new char[HIGHEST_SPECIAL + 1][];

	static {
		specialCharactersRepresentation['&'] = "&amp;".toCharArray();
		specialCharactersRepresentation['<'] = "&lt;".toCharArray();
		specialCharactersRepresentation['>'] = "&gt;".toCharArray();
		specialCharactersRepresentation['"'] = "&quot;".toCharArray();
		specialCharactersRepresentation['\''] = "&apos;".toCharArray();
	}

	public static void writeEscapedXml(char[] srcChars, int length, JspWriter w) throws IOException {
		int start = 0;

		for (int i = 0; i < length; i++) {
			char c = srcChars[i];
			if (c <= HIGHEST_SPECIAL) {
				char[] escaped = specialCharactersRepresentation[c];
				if (escaped != null) {
					// add un_escaped portion
					if (start < i) {
						w.write(srcChars, start, i - start);
					}
					// add escaped
					w.write(escaped);
					start = i + 1;
				}
			}
		}
		// add rest of un_escaped portion
		if (start < length) {
			w.write(srcChars, start, length - start);
		}
	}

	public static void writeEscapedXml(String text, JspWriter w) throws IOException {
		writeEscapedXml(text.toCharArray(), text.length(), w);
	}
}
