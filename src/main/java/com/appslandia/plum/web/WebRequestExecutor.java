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
import com.appslandia.plum.base.HttpException;
import com.appslandia.plum.base.HttpMethod;
import com.appslandia.plum.base.RequestExecutor;
import com.appslandia.plum.base.ServletUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public abstract class WebRequestExecutor extends RequestExecutor {
	private static final long serialVersionUID = 1L;

	public abstract Reauthenticator getReauthenticator();

	public abstract CsrfTokenManager getCsrfTokenManager();

	public abstract CaptchaManager getCaptchaManager();

	public abstract TempDataManager getTempDataManager();

	@Override
	protected void onActionExecuting(HttpServletRequest request, HttpServletResponse response, ActionContext actionContext) throws Exception {
		super.onActionExecuting(request, response, actionContext);

		// CSRF
		if (actionContext.getActionDesc().getEnableCsrf() != null) {
			if (actionContext.getActionDesc().getEnableCsrf().init() == false) {

				// GET
				if (ServletUtils.isGetOrHead(request)) {
					boolean hasPost = true;
					for (String actionVerb : actionContext.getActionDesc().getActionVerbs()) {
						if (actionVerb.equals(HttpMethod.POST)) {
							hasPost = false;
							break;
						}
					}
					if (hasPost) {
						if (getCsrfTokenManager().isCsrfTokenValid(request, actionContext.getActionDesc().getEnableCsrf().reuse()) == false) {
							ServletUtils.getModelState(request).addFieldError(ServletUtils.PARAM_CSRF_TOKEN, actionContext.getMessage(ServletUtils.ERROR_MSGKEY_CSRF_FAILED));
						}
					}
				}
				// POST
				else if (request.getMethod().equals(HttpMethod.POST)) {
					if (getCsrfTokenManager().isCsrfTokenValid(request, actionContext.getActionDesc().getEnableCsrf().reuse()) == false) {
						ServletUtils.getModelState(request).addFieldError(ServletUtils.PARAM_CSRF_TOKEN, actionContext.getMessage(ServletUtils.ERROR_MSGKEY_CSRF_FAILED));
					}
				}
			}
		}

		// CAPTCHA & POST
		if ((actionContext.getActionDesc().getEnableCaptcha() != null) && request.getMethod().equals(HttpMethod.POST)) {
			if (getCaptchaManager().isCaptchaValid(request) == false) {
				ServletUtils.getModelState(request).addFieldError(ServletUtils.PARAM_CAPTCHA_WORDS, actionContext.getMessage(ServletUtils.ERROR_MSGKEY_CAPTCHA_FAILED));
			}
		}

		// Load TempData
		if (ServletUtils.isGetOrHead(request)) {
			String tempDataId = StringUtils.trimToNull(request.getParameter(ServletUtils.PARAM_TEMP_DATA_ID));
			if (tempDataId != null) {
				TempData tempData = getTempDataManager().loadTempData(request, tempDataId);
				if (tempData != null) {
					Messages msgs = (Messages) tempData.remove(ServletUtils.REQUEST_ATTRIBUTE_MESSAGES);
					if (tempData.isEmpty() == false) {
						request.setAttribute(ServletUtils.REQUEST_ATTRIBUTE_TEMP_DATA, tempData);
					}
					if (msgs != null) {
						request.setAttribute(ServletUtils.REQUEST_ATTRIBUTE_MESSAGES, msgs);
					}
				}
			}
		}
	}

	@Override
	protected void onResultExecuting(HttpServletRequest request, HttpServletResponse response, ActionContext actionContext) throws Exception {
		super.onResultExecuting(request, response, actionContext);

		// CSRF & POST|GET
		if ((actionContext.getActionDesc().getEnableCsrf() != null) && (request.getMethod().equals(HttpMethod.POST) || ServletUtils.isGetOrHead(request))) {
			getCsrfTokenManager().initCsrfToken(request);
		}

		// CAPTCHA & POST|GET
		if ((actionContext.getActionDesc().getEnableCaptcha() != null) && (request.getMethod().equals(HttpMethod.POST) || ServletUtils.isGetOrHead(request))) {
			getCaptchaManager().initCaptcha(request);
		}
	}

	@Override
	protected void askHttps(HttpServletRequest request, HttpServletResponse response, ActionContext actionContext) throws Exception {
		if (ServletUtils.isGetOrHead(request) == false) {
			throw new HttpException(HttpServletResponse.SC_BAD_REQUEST, actionContext.getMessage(ServletUtils.ERROR_MSGKEY_HTTPS_REQUIRED));
		}
		String secureUrl = ServletUtils.getSecureUrl(request, getAppConfig().getHttpsPort());
		response.sendRedirect(response.encodeRedirectURL(secureUrl));
	}

	@Override
	protected void askAuth(HttpServletRequest request, HttpServletResponse response, ActionContext actionContext) throws Exception {
		String loginUrl = getAppConfig().getRequiredString(ServletUtils.CONFIG_LOGIN_URL);
		response.sendRedirect(response.encodeRedirectURL(ServletUtils.getRedirectUrl(request, loginUrl, null)));
	}

	@Override
	protected boolean isReauthed(HttpServletRequest request, HttpServletResponse response, ActionContext actionContext) throws Exception {
		if (getReauthenticator().isReauthed(request, response, actionContext.getUserData()) == false) {
			getReauthenticator().askReauth(request, response, actionContext.getUserData());
			return false;
		}
		return true;
	}
}
