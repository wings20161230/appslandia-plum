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
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.appslandia.common.base.Mutex;
import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.BaseEncodingUtils;
import com.appslandia.common.utils.SplitUtils;
import com.appslandia.common.utils.StringUtils;
import com.appslandia.common.utils.URLEncoding;
import com.appslandia.common.utils.URLUtils;
import com.appslandia.plum.web.Messages;
import com.appslandia.plum.web.SortBag;
import com.appslandia.plum.web.TempData;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class ServletUtils {

	// -------------------- Request Attributes -------------------- //

	public static final String REQUEST_ATTRIBUTE_REQUEST_WRAPPER = "__requestWrapper";
	public static final String REQUEST_ATTRIBUTE_ACTION_CONTEXT = "__actionContext";
	public static final String REQUEST_ATTRIBUTE_INCLUDE_PARAMS = "__includeParams";

	public static final String REQUEST_ATTRIBUTE_CSRF_TOKEN = "__csrfToken";
	public static final String REQUEST_ATTRIBUTE_CAPTCHA_ID = "__captchaId";

	public static final String REQUEST_ATTRIBUTE_MODEL = "__model";
	public static final String REQUEST_ATTRIBUTE_MODEL_STATE = "__modelState";
	public static final String REQUEST_ATTRIBUTE_CHECK_NOT_MODIFIED = "__checkNotModified";

	public static final String REQUEST_ATTRIBUTE_TEMP_DATA = "__tempData";
	public static final String REQUEST_ATTRIBUTE_PREF_COOKIE = "__prefCookie";
	public static final String REQUEST_ATTRIBUTE_TOKEN_THEFT = "__tokenTheft";

	public static final String REQUEST_ATTRIBUTE_MESSAGES = "__messages";
	public static final String REQUEST_ATTRIBUTE_SORT_BAG = "__sortBag";

	// -------------------- Session Attributes -------------------- //

	public static final String SESSION_ATTRIBUTE_CSRF_CACHE = "__csrfCache";
	public static final String SESSION_ATTRIBUTE_TEMP_DATA_CACHE = "__tempDataCache";
	public static final String SESSION_ATTRIBUTE_CAPTCHA_CACHE = "__captchaCache";
	public static final String SESSION_ATTRIBUTE_MUTEX = "__mutex";

	// -------------------- Context Attributes -------------------- //

	public static final String CONTEXT_ATTRIBUTE_ACTION_PARSER = "__actionParser";
	public static final String CONTEXT_ATTRIBUTE_CONST_DESC_PROVIDER = "__constDescProvider";

	public static final String CONTEXT_ATTRIBUTE_ACTION_INVOKER = "__actionInvoker";
	public static final String CONTEXT_ATTRIBUTE_CONTROLLER_PROVIDER = "__controllerProvider";
	public static final String CONTEXT_ATTRIBUTE_CONTROLLER_DESC_PROVIDER = "__controllerDescProvider";

	public static final String CONTEXT_ATTRIBUTE_MUTEX = "__mutex";
	public static final String CONTEXT_ATTRIBUTE_INCLUDE_TAG_CACHE = "__includeTagCache";

	// -------------------- Request Parameters -------------------- //

	public static final String PARAM_DESC = "__desc";
	public static final String PARAM_LANGUAGE = "__language";

	public static final String PARAM_CAPTCHA_ID = "__captchaId";
	public static final String PARAM_CAPTCHA_WORDS = "__captchaWords";
	public static final String PARAM_CSRF_TOKEN = "__csrfToken";

	public static final String PARAM_TEMP_DATA_ID = "__tempDataId";
	public static final String PARAM_RETURN_URL = "__returnUrl";

	public static final String PARAM_REAUTH_SERIES = "__rseries";
	public static final String PARAM_REAUTH_TOKEN = "__rtoken";

	// -------------------- Error Keys -------------------- //

	public static final String ERROR_MSGKEY_BAD_REQUEST = "errors.badRequest";
	public static final String ERROR_MSGKEY_METHOD_NOT_ALLOWED = "errors.methodNotAllowed";
	public static final String ERROR_MSGKEY_UNAUTHORIZED = "errors.unauthorized";

	public static final String ERROR_MSGKEY_NOT_FOUND = "errors.notNound";
	public static final String ERROR_MSGKEY_FORBIDDEN = "errors.forbidden";
	public static final String ERROR_MSGKEY_INTERNAL_SERVER_ERROR = "errors.internalServerError";
	public static final String ERROR_MSGKEY_HTTPS_REQUIRED = "errors.httpsRequired";

	public static final String ERROR_MSGKEY_CSRF_FAILED = "errors.csrfFailed";
	public static final String ERROR_MSGKEY_CAPTCHA_FAILED = "errors.captchaFailed";
	public static final String ERROR_MSGKEY_LIST_FAILED = "errors.listFailed";
	public static final String ERROR_MSGKEY_FIELD_REQUIRED = "errors.fieldRequired";

	// -------------------- AppConfig Keys -------------------- //

	public static final String CONFIG_HTTP_PORT = "httpPort";
	public static final String CONFIG_HTTPS_PORT = "httpsPort";
	public static final String CONFIG_ENABLE_HTTPS = "enableHttps";

	public static final String CONFIG_LOGIN_URL = "loginUrl";
	public static final String CONFIG_REAUTH_URL = "reauthUrl";
	public static final String CONFIG_PRODUCTION = "production";

	public static final String CONFIG_JSP_ROOT = "jspRoot";

	// -------------------- Other Constants -------------------- //

	public static final String ACTION_INDEX = "index";
	public static final String ATTRIBUTE_USER_DATA = "__userData";

	// --------------------------------------------------------- //

	public static String getURIWithQuery(HttpServletRequest request) {
		if (request.getQueryString() != null) {
			return request.getRequestURI() + "?" + request.getQueryString();
		}
		return request.getRequestURI();
	}

	public static String getSecureUrl(HttpServletRequest request, int httpsPort) {
		StringBuilder url = new StringBuilder();
		url.append("https://").append(request.getServerName());

		if (httpsPort == 443) {
			url.append(ServletUtils.getURIWithQuery(request));
		} else {
			url.append(':').append(httpsPort).append(ServletUtils.getURIWithQuery(request));
		}
		return url.toString();
	}

	public static String getURIWithQuery(HttpServletRequest request, String language) {
		StringBuilder sb = new StringBuilder();
		sb.append(request.getServletContext().getContextPath());

		ActionContext actionContext = (ActionContext) request.getAttribute(ServletUtils.REQUEST_ATTRIBUTE_ACTION_CONTEXT);
		AssertUtils.assertNotNull(actionContext);

		sb.append('/').append(language);
		if (actionContext.getLanguageId() == null) {
			sb.append(request.getServletPath());
		} else {
			// Exclude language
			sb.append(request.getServletPath().substring(actionContext.getLanguageId().length() + 1));
		}

		if (request.getPathInfo() != null) {
			sb.append(request.getPathInfo());
		}
		if (request.getQueryString() != null) {
			sb.append('?').append(request.getQueryString());
		}
		return sb.toString();
	}

	public static String getRedirectUrl(HttpServletRequest request, String redirectUrl, Map<String, Object> parameterMap) {
		StringBuilder sb = new StringBuilder();
		sb.append(redirectUrl).append('?');
		sb.append(PARAM_RETURN_URL).append('=').append(URLEncoding.encode(ServletUtils.getURIWithQuery(request)));
		if ((parameterMap != null) && (parameterMap.isEmpty() == false)) {
			sb.append('&');
			URLUtils.addQueryParams(sb, parameterMap);
		}
		return sb.toString();
	}

	public static String getRemoteIp(HttpServletRequest request) {
		String xIps = request.getHeader("X-Forwarded-For");
		if ((xIps == null) || xIps.isEmpty()) {
			return request.getRemoteAddr();
		}
		if (xIps.equalsIgnoreCase("unknown")) {
			return request.getRemoteAddr();
		}
		for (String xIp : SplitUtils.split(xIps, (','))) {
			if (xIp.equalsIgnoreCase("unknown") == false) {
				return xIp;
			}
		}
		return request.getRemoteAddr();
	}

	public static String getExtension(String requestURI) {
		int index = requestURI.lastIndexOf('.');
		AssertUtils.assertTrue(index > 0, "Couldn't parse extension.");
		return requestURI.substring(index);
	}

	public static String getAppPath(ServletContext sc) {
		String path = sc.getRealPath("/");
		AssertUtils.assertNotNull(path, "Couldn't determine application path.");
		return path;
	}

	public static boolean isGetOrHead(HttpServletRequest request) {
		return request.getMethod().equals(HttpMethod.GET) || request.getMethod().equals(HttpMethod.HEAD);
	}

	public static String getAllowMethods(String[] actionVerbs, boolean allowOptions, boolean allowTrace) {
		StringBuilder sb = new StringBuilder();
		for (String httpMethod : actionVerbs) {
			if (sb.length() > 0) {
				sb.append(", ");
			}
			sb.append(httpMethod);
		}
		if (allowOptions) {
			sb.append(", ").append(HttpMethod.OPTIONS);
		}
		if (allowTrace) {
			sb.append(", ").append(HttpMethod.TRACE);
		}
		return sb.toString();
	}

	public static RequestDispatcher getRequestDispatcher(HttpServletRequest request, String location) {
		RequestDispatcher dispatcher = request.getRequestDispatcher(location);
		if (dispatcher == null) {
			throw new IllegalArgumentException("getRequestDispatcher (location=" + location + ")");
		}
		return dispatcher;
	}

	public static void forward(HttpServletRequest request, HttpServletResponse response, String location) throws ServletException, IOException {
		if (response.isCommitted()) {
			throw new IllegalStateException("The response is already committed. Can't forward.");
		}
		getRequestDispatcher(request, location).forward(request, response);
	}

	public static void include(HttpServletRequest request, HttpServletResponse response, String location) throws ServletException, IOException {
		getRequestDispatcher(request, location).include(request, response);
	}

	public static PrintWriter getWriter(HttpServletResponse response) throws IOException {
		try {
			return new PrintWriter(new OutputStreamWriter(response.getOutputStream(), response.getCharacterEncoding()));
		} catch (IllegalStateException ex) {
			return response.getWriter();
		}
	}

	public static String getCookieValue(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			return null;
		}
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(name)) {
				return StringUtils.trimToNull(cookie.getValue());
			}
		}
		return null;
	}

	public static void removeCookie(HttpServletResponse response, String name, String domain, String path) {
		Cookie cookie = new Cookie(name, StringUtils.EMPTY_STRING);
		cookie.setMaxAge(0);
		if (domain != null) {
			cookie.setDomain(domain);
		}
		cookie.setPath(path);
		response.addCookie(cookie);
	}

	public static Boolean checkNotModified(HttpServletRequest request, HttpServletResponse response, long lastModifiedMs) {
		Boolean checkedAlready = (Boolean) request.getAttribute(REQUEST_ATTRIBUTE_CHECK_NOT_MODIFIED);
		if (checkedAlready != null) {
			return null;
		}

		response.setDateHeader("Last-Modified", lastModifiedMs);
		long ifModifiedSince = request.getDateHeader("If-Modified-Since");

		boolean notModified = ifModifiedSince >= (lastModifiedMs / 1000 * 1000);
		if (notModified) {
			response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
		}

		request.setAttribute(REQUEST_ATTRIBUTE_CHECK_NOT_MODIFIED, Boolean.TRUE);
		return Boolean.valueOf(notModified);
	}

	public static Boolean checkNotModified(HttpServletRequest request, HttpServletResponse response, String etag) {
		Boolean checkedAlready = (Boolean) request.getAttribute(REQUEST_ATTRIBUTE_CHECK_NOT_MODIFIED);
		if (checkedAlready != null) {
			return null;
		}

		response.setHeader("ETag", etag);
		String ifNoneMatch = request.getHeader("If-None-Match");

		boolean notModified = etag.equals(ifNoneMatch);
		if (notModified) {
			response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
		}

		request.setAttribute(REQUEST_ATTRIBUTE_CHECK_NOT_MODIFIED, Boolean.TRUE);
		return Boolean.valueOf(notModified);
	}

	public static String generateEtag(byte[] md5, boolean weak) {
		StringBuilder sb = (weak) ? new StringBuilder(36) : new StringBuilder(34);
		sb.append(weak ? ("W/\"") : ('"'));
		BaseEncodingUtils.appendAsHex(sb, md5);
		sb.append('"');
		return sb.toString();
	}

	public static boolean isGzipSupported(HttpServletRequest request) {
		String ae = request.getHeader("Accept-Encoding");
		return (ae != null) && (ae.toLowerCase(Locale.ENGLISH).contains("gzip"));
	}

	public static Object getMutex(HttpSession session) {
		Mutex mutex = (Mutex) session.getAttribute(SESSION_ATTRIBUTE_MUTEX);
		AssertUtils.assertNotNull(mutex);
		return mutex;
	}

	public static Object getMutex(ServletContext context) {
		Object mutex = context.getAttribute(CONTEXT_ATTRIBUTE_MUTEX);
		AssertUtils.assertNotNull(mutex);
		return mutex;
	}

	public static ModelState getModelState(HttpServletRequest request) {
		ModelState modelState = (ModelState) request.getAttribute(REQUEST_ATTRIBUTE_MODEL_STATE);
		if (modelState == null) {
			modelState = new ModelState();
			request.setAttribute(REQUEST_ATTRIBUTE_MODEL_STATE, modelState);
		}
		return modelState;
	}

	public static Messages getMessages(HttpServletRequest request) {
		Messages messages = (Messages) request.getAttribute(REQUEST_ATTRIBUTE_MESSAGES);
		if (messages == null) {
			messages = new Messages();
			request.setAttribute(REQUEST_ATTRIBUTE_MESSAGES, messages);
		}
		return messages;
	}

	public static SortBag getSortBag(HttpServletRequest request) {
		SortBag sortBag = (SortBag) request.getAttribute(REQUEST_ATTRIBUTE_SORT_BAG);
		if (sortBag == null) {
			sortBag = new SortBag();
			request.setAttribute(REQUEST_ATTRIBUTE_SORT_BAG, sortBag);
		}
		return sortBag;
	}

	public static TempData getTempData(HttpServletRequest request) {
		TempData tempData = (TempData) request.getAttribute(REQUEST_ATTRIBUTE_TEMP_DATA);
		if (tempData == null) {
			tempData = new TempData();
			request.setAttribute(REQUEST_ATTRIBUTE_TEMP_DATA, tempData);
		}
		return tempData;
	}
}
