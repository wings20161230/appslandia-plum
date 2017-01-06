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
import javax.servlet.http.HttpServletResponse;

import com.appslandia.common.utils.StringUtils;
import com.appslandia.plum.base.ActionContext;
import com.appslandia.plum.base.ActionResult;
import com.appslandia.plum.base.GET;
import com.appslandia.plum.base.LanguageProvider;
import com.appslandia.plum.base.NotFoundException;
import com.appslandia.plum.base.POST;
import com.appslandia.plum.base.PathParams;
import com.appslandia.plum.base.RequestParam;
import com.appslandia.plum.base.ServletUtils;
import com.appslandia.plum.results.RedirectResult;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public abstract class LanguageController {

	public abstract LanguageProvider getLanguageProvider();

	public abstract PrefCookieHandler getPrefCookieParser();

	@PathParams("/{language}")
	@GET
	@POST
	public ActionResult index(@RequestParam("language") String language, HttpServletRequest request, HttpServletResponse response, ActionContext actionContext) throws Exception {

		// Check language
		if (this.getLanguageProvider().getLanguage(language) == null) {
			throw new NotFoundException(actionContext.getMessage(ServletUtils.ERROR_MSGKEY_NOT_FOUND, request.getRequestURI()));
		}

		// savePrefCookie
		getPrefCookieParser().savePrefCookie(request, response, language);

		final String returnUrl = StringUtils.trimToNull(request.getParameter(ServletUtils.PARAM_RETURN_URL));
		if (returnUrl != null) {
			return new RedirectResult(returnUrl);

		} else {
			return new ActionResult() {

				@Override
				public void execute(HttpServletRequest request, HttpServletResponse response, ActionContext actionContext) throws Exception {
					StringBuilder location = new StringBuilder();
					location.append(request.getServletContext().getContextPath());
					location.append('/').append(language).append('/').append(ServletUtils.ACTION_INDEX).append(ServletUtils.getExtension(request.getRequestURI()));
					response.sendRedirect(response.encodeRedirectURL(location.toString()));
				}
			};
		}
	}
}
