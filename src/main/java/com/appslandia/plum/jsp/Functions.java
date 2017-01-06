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

import com.appslandia.common.utils.StringUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class Functions {

	@Function
	public static String out(boolean b, Object ifTrue, Object ifFalse) {
		if (b) {
			if (ifTrue != null) {
				return ifTrue.toString();
			}
		} else {
			if (ifFalse != null) {
				return ifFalse.toString();
			}
		}
		return StringUtils.EMPTY_STRING;
	}

	@Function
	public static String hidden(boolean b) {
		return b ? " hidden=\"hidden\"" : StringUtils.EMPTY_STRING;
	}

	@Function
	public static String disabled(boolean b) {
		return b ? " disabled=\"disabled\"" : StringUtils.EMPTY_STRING;
	}

	@Function
	public static String readonly(boolean b) {
		return b ? " readonly=\"readonly\"" : StringUtils.EMPTY_STRING;
	}

	@Function
	public static String required(boolean b) {
		return b ? " required=\"required\"" : StringUtils.EMPTY_STRING;
	}

	@Function
	public static String checked(boolean b) {
		return b ? " checked=\"checked\"" : StringUtils.EMPTY_STRING;
	}

	@Function
	public static String selected(boolean b) {
		return b ? " selected=\"selected\"" : StringUtils.EMPTY_STRING;
	}

	@Function
	public static String sublast(String str, int len) {
		if (str != null) {
			return str.length() > len ? str.substring(str.length() - len) : str;
		}
		return StringUtils.EMPTY_STRING;
	}

	@Function
	public static String subfirst(String str, int len) {
		if (str != null) {
			return str.length() > len ? str.substring(0, len) : str;
		}
		return StringUtils.EMPTY_STRING;
	}

	@Function
	public static String trunc(String str, int len) {
		if (str != null) {
			return str.length() > len ? str.substring(0, len) + "..." : str;
		}
		return StringUtils.EMPTY_STRING;
	}
}
