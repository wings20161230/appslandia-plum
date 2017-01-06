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

package com.appslandia.plum.base;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.appslandia.common.base.JsonProcessor;
import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.ContentTypes;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public abstract class RequestExecutor extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public abstract AppConfig getAppConfig();

	public abstract RemoteClientFilter getRemoteClientFilter();

	public abstract CorsPolicyHandler getCorsPolicyHandler();

	public abstract ControllerProvider getControllerProvider();

	public abstract ActionInvoker getActionInvoker();

	public abstract JsonProcessor getJsonProcessor();

	protected void doRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// ActionContext
			ActionContext actionContext = (ActionContext) request.getAttribute(ServletUtils.REQUEST_ATTRIBUTE_ACTION_CONTEXT);
			AssertUtils.assertNotNull(actionContext);

			// Filter client
			if (getRemoteClientFilter().filter(request) == false) {
				throw new HttpException(HttpServletResponse.SC_FORBIDDEN, actionContext.getMessage(ServletUtils.ERROR_MSGKEY_FORBIDDEN, request.getRequestURI()));
			}

			// Allow Method?
			if (actionContext.getActionDesc().allowActionVerb(request.getMethod()) == false) {
				throw new HttpException(HttpServletResponse.SC_METHOD_NOT_ALLOWED, actionContext.getMessage(ServletUtils.ERROR_MSGKEY_METHOD_NOT_ALLOWED));
			}

			// HTTPS
			if (request.isSecure() == false) {
				if (actionContext.getActionDesc().isEnableHttps()) {
					askHttps(request, response, actionContext);
					return;
				}
			}

			// Authorize
			if (actionContext.getActionDesc().getAuthorize() != null) {
				if (actionContext.getUserData() == null) {
					askAuth(request, response, actionContext);
					return;
				}
				if (actionContext.getActionDesc().getAuthorize().reauth()) {
					if (isReauthed(request, response, actionContext) == false) {
						return;
					}
				}
				if (actionContext.getActionDesc().getAuthorize().userRoles().length != 0) {
					if (isUserInRole(request, actionContext.getActionDesc().getAuthorize().userRoles()) == false) {
						throw new ForbiddenException(actionContext.getMessage(ServletUtils.ERROR_MSGKEY_FORBIDDEN, request.getRequestURI()));
					}
				}
			}

			// controller
			Object controller = getControllerProvider().getController(actionContext.getControllerDesc().getControllerClass(), actionContext.getControllerDesc().getQualifiers());

			// onActionExecuting
			onActionExecuting(request, response, actionContext);

			// Invoke Action
			Object result = getActionInvoker().invoke(request, response, actionContext, controller);

			// AsyncStarted?
			if (request.isAsyncStarted()) {
				AssertUtils.assertNotNull(actionContext.getActionDesc().getAsync(), "@Async is required.");
			}

			// onResultExecuting
			onResultExecuting(request, response, actionContext);

			// Execute Result
			if (result instanceof ActionResult) {
				((ActionResult) result).execute(request, response, actionContext);
			} else if (result != null) {
				writeJsonResult(response, result);
			}

		} catch (RuntimeException ex) {
			throw ex;
		} catch (ServletException | IOException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	protected boolean isUserInRole(HttpServletRequest request, String[] userRoles) {
		for (String role : userRoles) {
			if (request.isUserInRole(role)) {
				return true;
			}
		}
		return false;
	}

	protected void writeJsonResult(HttpServletResponse response, Object result) throws Exception {
		response.setContentType(ContentTypes.APP_JSON);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		getJsonProcessor().write(response.getWriter(), result);
	}

	protected abstract void askHttps(HttpServletRequest request, HttpServletResponse response, ActionContext actionContext) throws Exception;

	protected abstract void askAuth(HttpServletRequest request, HttpServletResponse response, ActionContext actionContext) throws Exception;

	protected abstract boolean isReauthed(HttpServletRequest request, HttpServletResponse response, ActionContext actionContext) throws Exception;

	protected void onActionExecuting(HttpServletRequest request, HttpServletResponse response, ActionContext actionContext) throws Exception {
	}

	protected void onResultExecuting(HttpServletRequest request, HttpServletResponse response, ActionContext actionContext) throws Exception {
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doRequest(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doRequest(request, response);
	}

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doRequest(request, response);
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doRequest(request, response);
	}

	@Override
	protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ActionContext actionContext = (ActionContext) request.getAttribute(ServletUtils.REQUEST_ATTRIBUTE_ACTION_CONTEXT);
		AssertUtils.assertNotNull(actionContext);

		if (actionContext.getActionDesc().getCorsPolicy() != null) {
			getCorsPolicyHandler().handle(request, response, actionContext);
		} else {
			if (getAppConfig().isProduction()) {
				throw new HttpException(HttpServletResponse.SC_METHOD_NOT_ALLOWED, actionContext.getMessage(ServletUtils.ERROR_MSGKEY_METHOD_NOT_ALLOWED));
			}
			response.setHeader("Allow", ServletUtils.getAllowMethods(actionContext.getActionDesc().getActionVerbs(), true, true));
		}
	}

	@Override
	protected void doTrace(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (getAppConfig().isProduction()) {
			ActionContext actionContext = (ActionContext) request.getAttribute(ServletUtils.REQUEST_ATTRIBUTE_ACTION_CONTEXT);
			AssertUtils.assertNotNull(actionContext);

			throw new HttpException(HttpServletResponse.SC_METHOD_NOT_ALLOWED, actionContext.getMessage(ServletUtils.ERROR_MSGKEY_METHOD_NOT_ALLOWED));
		}
		super.doTrace(request, response);
	}
}
