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

package com.appslandia.plum.web;

import javax.servlet.http.HttpServletRequest;

import com.appslandia.common.base.TextGenerator;
import com.appslandia.common.utils.StringUtils;
import com.appslandia.plum.base.ServletUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public abstract class CsrfTokenManager {

	public abstract TextGenerator getCsrfTokenGenerator();

	protected abstract void saveCsrfToken(HttpServletRequest request, String csrfToken) throws Exception;

	public String initCsrfToken(HttpServletRequest request) throws Exception {
		String csrfToken = getCsrfTokenGenerator().generate();
		saveCsrfToken(request, csrfToken);

		request.setAttribute(ServletUtils.REQUEST_ATTRIBUTE_CSRF_TOKEN, csrfToken);
		return csrfToken;
	}

	protected abstract boolean isCsrfTokenValid(HttpServletRequest request, String csrfToken, boolean reuse) throws Exception;

	public boolean isCsrfTokenValid(HttpServletRequest request, boolean reuse) throws Exception {
		String csrfToken = StringUtils.trimToNull(request.getParameter(ServletUtils.PARAM_CSRF_TOKEN));
		if (csrfToken == null) {
			return false;
		}
		return isCsrfTokenValid(request, csrfToken, reuse);
	}

	public boolean isCsrfTokenValid(HttpServletRequest request, String csrfToken) throws Exception {
		return isCsrfTokenValid(request, csrfToken, false);
	}
}
