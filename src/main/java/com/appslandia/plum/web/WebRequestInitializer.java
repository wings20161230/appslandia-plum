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

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.appslandia.common.base.Language;
import com.appslandia.plum.base.ActionContext;
import com.appslandia.plum.base.ActionResult;
import com.appslandia.plum.base.HeaderPolicy;
import com.appslandia.plum.base.HttpException;
import com.appslandia.plum.base.RequestInitializer;
import com.appslandia.plum.base.ServletUtils;
import com.appslandia.plum.results.JsonResult;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public abstract class WebRequestInitializer extends RequestInitializer {

	public abstract PrefCookieHandler getPrefCookieHandler();

	@Override
	protected void initialize(HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initialize(request, response);

		// PrefCookie
		request.setAttribute(ServletUtils.REQUEST_ATTRIBUTE_PREF_COOKIE, getPrefCookieHandler().parsePrefCookie(request, response));
	}

	@Override
	protected boolean redirectLanguage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (ServletUtils.isGetOrHead(request) == false) {
			return false;
		}
		PrefCookie prefCookie = (PrefCookie) request.getAttribute(ServletUtils.REQUEST_ATTRIBUTE_PREF_COOKIE);
		if (prefCookie.getLanguage() != null) {
			Language language = getLanguageProvider().getLanguage(prefCookie.getLanguage());
			if (language != null) {
				response.sendRedirect(response.encodeRedirectURL(ServletUtils.getURIWithQuery(request, language.getId())));
				return true;
			}
		}
		return false;
	}

	@Override
	protected void initialize(HttpServletRequest request, HttpServletResponse response, ActionContext actionContext) throws Exception {
		super.initialize(request, response, actionContext);

		// Content-Language
		response.setHeader("Content-Language", getLanguageProvider().getLanguages());

		// HSTS
		if (this.getAppConfig().isEnableHttps()) {
			getHeaderPolicyProvider().getHeaderPolicy(HeaderPolicy.POLICY_HSTS).write(request, response, actionContext);
		}
	}

	protected boolean isJsonResult(HttpServletRequest request) {
		ActionContext actionContext = (ActionContext) request.getAttribute(ServletUtils.REQUEST_ATTRIBUTE_ACTION_CONTEXT);
		if (actionContext != null) {
			Class<?> type = actionContext.getActionDesc().getMethod().getReturnType();
			return (type == JsonResult.class) || ((ActionResult.class.isAssignableFrom(type) == false) && (type != void.class));
		}
		return false;
	}

	@Override
	protected void sendError(HttpServletRequest request, HttpServletResponse response, Throwable error) throws ServletException, IOException {
		if (isJsonResult(request)) {
			writeJsonError(request, response, error);
			return;
		}
		if (error instanceof HttpException) {
			if (error.getClass() == HttpException.class) {
				writeSimpleError(request, response, (HttpException) error);
			} else {
				response.sendError(((HttpException) error).getStatus(), error.getMessage());
			}
		} else {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	protected void writeSimpleError(HttpServletRequest request, HttpServletResponse response, HttpException error) throws ServletException, IOException {
		ErrorServlet.writeSimpleError(request, response, error.getStatus(), error.getMessage());
	}
}
