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
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.appslandia.common.base.FormatProvider;
import com.appslandia.common.base.FormatProviderImpl;
import com.appslandia.common.base.JsonProcessor;
import com.appslandia.common.base.Language;
import com.appslandia.common.base.MemoryStream;
import com.appslandia.common.utils.ContentTypes;
import com.appslandia.common.utils.ExceptionUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public abstract class RequestInitializer implements Filter {

	public abstract AppConfig getAppConfig();

	public abstract LanguageProvider getLanguageProvider();

	public abstract MessageBundleProvider getMessageBundleProvider();

	public abstract Authenticator getAuthenticator();

	public abstract HeaderPolicyProvider getHeaderPolicyProvider();

	public abstract ActionParser getActionParser();

	public abstract JsonProcessor getJsonProcessor();

	public abstract RequestErrorLogger getRequestErrorLogger();

	protected void initialize(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (request.getCharacterEncoding() == null) {
			request.setCharacterEncoding(StandardCharsets.UTF_8.name());
		}
	}

	protected void initialize(HttpServletRequest request, HttpServletResponse response, ActionContext actionContext) throws Exception {
		// Header Polices
		if (actionContext.getActionDesc().getHeaderPolicys() != null) {
			for (String policyName : actionContext.getActionDesc().getHeaderPolicys()) {
				getHeaderPolicyProvider().getHeaderPolicy(policyName).write(request, response, actionContext);
			}
		}
	}

	protected FormatProvider initFormatProvider(Language language) throws Exception {
		return new FormatProviderImpl(language);
	}

	protected String initLanguage(HttpServletRequest request, HttpServletResponse response, ActionContext actionContext, List<String> pathItems) throws Exception {
		// language parameter
		String languageId = pathItems.get(0);
		Language language = getLanguageProvider().getLanguage(languageId);

		if (language == null) {
			languageId = null;
			language = getLanguageProvider().getDefault();
		} else {
			// Remove language parameter
			pathItems.remove(0);
			actionContext.setLanguageId(languageId);
		}

		actionContext.setLanguage(language);
		actionContext.setFormatProvider(initFormatProvider(language));
		actionContext.setMessageBundle(getMessageBundleProvider().getMessageBundle(language.getId()));
		return languageId;
	}

	protected boolean redirectLanguage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		return false;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		try {
			// initialize
			initialize(request, response);

			// ActionContext
			final ActionContext actionContext = new ActionContext();

			// parsePathItems
			List<String> pathItems = ActionPathUtils.parsePathItems(request.getServletPath(), getActionParser().getActionExtension());

			// Language
			final String languageId = initLanguage(request, response, actionContext, pathItems);

			// Path Parameters
			Map<String, String> pathParamMap = new HashMap<>();
			if (getActionParser().parseActionContext(pathItems, actionContext, pathParamMap) == false) {
				throw new NotFoundException(actionContext.getMessage(ServletUtils.ERROR_MSGKEY_NOT_FOUND, request.getRequestURI()));
			}
			request.setAttribute(ServletUtils.REQUEST_ATTRIBUTE_ACTION_CONTEXT, actionContext);

			// sendRedirectLanguage
			if (languageId == null) {
				if (redirectLanguage(request, response)) {
					return;
				}
			}

			// Authenticate
			actionContext.setUserData(getAuthenticator().authenticate(request, response));

			// RequestWrapper
			if ((pathParamMap.isEmpty() == false) || (actionContext.getUserData() != null)) {
				request = new RequestWrapper(request, pathParamMap);
				if (this.isTestContext()) {
					request.setAttribute(ServletUtils.REQUEST_ATTRIBUTE_REQUEST_WRAPPER, request);
				}
			}

			// initialize
			initialize(request, response, actionContext);

			// GZIP & ETag
			boolean handleGzip = ServletUtils.isGzipSupported(request) && (actionContext.getActionDesc().getDisableGzip() == null);
			boolean handleEtag = (actionContext.getActionDesc().getEnableEtag() != null) && ServletUtils.isGetOrHead(request);
			OutputConfig outputConfig = actionContext.getActionDesc().getOutputConfig();

			if (actionContext.getActionDesc().getAsync() == null) {
				// @Async = null
				if (handleEtag) {
					int blockSize = (outputConfig == null) ? 512 : Math.max(outputConfig.blockSize(), 512);
					response = new MemoryResponseWrapper(response, new MemoryStream(blockSize), handleGzip);

				} else if (handleGzip) {
					response = new GzipResponseWrapper(response);
				} else {
					response = new ResponseWrapperImpl(response);
				}
			} else {
				// @Async
				response = new ResponseWrapperImpl(response);
			}

			// setBufferSize
			if ((outputConfig != null) && (outputConfig.bufferSize() > 0)) {
				response.setBufferSize(Math.max(outputConfig.bufferSize(), 512));
			}

			// doFilter
			chain.doFilter(request, response);

			// 3XX
			if ((300 <= response.getStatus()) && (response.getStatus() < 400)) {
				return;
			}

			// 4XX | 5XX
			if ((400 <= response.getStatus()) && (response.getStatus() < 600)) {
				throw new HttpException(response.getStatus(), "HttpException");
			}

			// ResponseWrapper
			if (response instanceof ResponseWrapper) {
				((ResponseWrapper) response).finishWrapper();
			}

			if (response.getClass() != MemoryResponseWrapper.class) {
				return;
			}

			// ETAG
			MemoryResponseWrapper wrapper = (MemoryResponseWrapper) response;
			String eTag = ServletUtils.generateEtag(wrapper.getContent().digest("MD5"), actionContext.getActionDesc().getEnableEtag().weak());
			if (Boolean.TRUE.equals(ServletUtils.checkNotModified(request, wrapper, eTag)) == false) {
				if (handleGzip) {
					wrapper.setHeader("Content-Encoding", "gzip");
				}
				wrapper.getResponse().setContentLength(wrapper.getContent().size());
				wrapper.getContent().writeTo(wrapper.getResponse().getOutputStream());
			}

		} catch (Throwable ex) {
			handleError(request, response, ex);
		}
	}

	protected void logError(HttpServletRequest request, Throwable error) {
		getRequestErrorLogger().log(request, error);
	}

	protected boolean shouldLogError(Throwable error) {
		if (error.getClass() == BadRequestException.class) {
			return false;
		}
		if (error.getClass() == NotFoundException.class) {
			return false;
		}
		return true;
	}

	protected static HttpServletResponse unwrapToOrigin(HttpServletResponse response) {
		if (response.getClass() == GzipResponseWrapper.class) {
			return (HttpServletResponse) ((GzipResponseWrapper) response).getResponse();
		}
		if (response.getClass() == MemoryResponseWrapper.class) {
			return (HttpServletResponse) ((MemoryResponseWrapper) response).getResponse();
		}
		if (response.getClass() == ResponseWrapperImpl.class) {
			return (HttpServletResponse) ((ResponseWrapperImpl) response).getResponse();
		}
		return response;
	}

	protected void handleError(HttpServletRequest request, HttpServletResponse response, Throwable error) throws ServletException, IOException {
		if (shouldLogError(error)) {
			logError(request, error);
		}
		if (response.isCommitted()) {
			if (response instanceof ResponseWrapper) {
				((ResponseWrapper) response).finishWrapper();
			} else {
				response.flushBuffer();
			}
		} else {
			// Not committed
			resetContentHeaders(request, response);
			response.resetBuffer();
			sendError(request, unwrapToOrigin(response), error);
		}
	}

	protected void resetContentHeaders(HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Content-Type", null);
		response.setHeader("Content-Length", null);
		response.setHeader("Content-Encoding", null);
		response.setHeader("Content-Disposition", null);
	}

	protected abstract void sendError(HttpServletRequest request, HttpServletResponse response, Throwable error) throws ServletException, IOException;

	protected void writeJsonError(HttpServletRequest request, HttpServletResponse response, Throwable error) throws ServletException, IOException {
		response.setContentType(ContentTypes.APP_JSON);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setStatus((error instanceof HttpException) ? ((HttpException) error).getStatus() : HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

		Result fault = new Result().asError().setMessage(ExceptionUtils.getMessage(error));
		if (getAppConfig().isProduction() == false) {
			fault.setException(error);
		}
		try (PrintWriter writer = ServletUtils.getWriter(response)) {
			this.getJsonProcessor().write(writer, fault);
		}
	}

	protected boolean isTestContext() {
		return false;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}
}
