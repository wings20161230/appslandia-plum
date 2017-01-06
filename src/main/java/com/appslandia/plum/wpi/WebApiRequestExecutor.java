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

package com.appslandia.plum.wpi;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.appslandia.plum.base.ActionContext;
import com.appslandia.plum.base.HttpException;
import com.appslandia.plum.base.RequestExecutor;
import com.appslandia.plum.base.ServletUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public abstract class WebApiRequestExecutor extends RequestExecutor {
	private static final long serialVersionUID = 1L;

	@Override
	protected void askHttps(HttpServletRequest request, HttpServletResponse response, ActionContext actionContext) throws Exception {
		if (ServletUtils.isGetOrHead(request) == false) {
			throw new HttpException(HttpServletResponse.SC_BAD_REQUEST, actionContext.getMessage(ServletUtils.ERROR_MSGKEY_HTTPS_REQUIRED));
		}
		String secureUrl = ServletUtils.getSecureUrl(request, getAppConfig().getHttpsPort());
		response.sendRedirect(secureUrl);
	}

	@Override
	protected void askAuth(HttpServletRequest request, HttpServletResponse response, ActionContext actionContext) throws Exception {
		throw new HttpException(HttpServletResponse.SC_UNAUTHORIZED, actionContext.getMessage(ServletUtils.ERROR_MSGKEY_UNAUTHORIZED, request.getRequestURI()));
	}

	@Override
	protected boolean isReauthed(HttpServletRequest request, HttpServletResponse response, ActionContext actionContext) throws Exception {
		throw new HttpException(HttpServletResponse.SC_UNAUTHORIZED, actionContext.getMessage(ServletUtils.ERROR_MSGKEY_UNAUTHORIZED, request.getRequestURI()));
	}
}
