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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public abstract class CorsPolicyHandler {

	public static final String HEADER_CORS_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
	public static final String HEADER_CORS_ALLOW_METHODS = "Access-Control-Allow-Methods";
	public static final String HEADER_CORS_ALLOW_HEADERS = "Access-Control-Allow-Headers";
	public static final String HEADER_CORS_EXPOSE_HEADERS = "Access-Control-Expose-Headers";

	public static final String HEADER_CORS_MAX_AGE = "Access-Control-Max-Age";
	public static final String HEADER_CORS_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
	public static final String HEADER_CORS_ORIGIN_UNMATCHED = "Access-Control-Origin-Unmatched";

	public abstract CorsPolicyProvider getCorsPolicyProvider();

	final ConcurrentMap<String, Pattern> patternCache = new ConcurrentHashMap<>();

	public abstract AppConfig getAppConfig();

	protected Pattern getPattern(String domain) {
		return this.patternCache.computeIfAbsent(domain, d -> Pattern.compile(d, Pattern.CASE_INSENSITIVE));
	}

	protected boolean allowOrigin(URL originUrl, CorsPolicy corsPolicy, HttpServletRequest request, ActionContext actionContext) {
		final boolean originHttps = originUrl.getProtocol().equals("https");

		// Protocol
		if (actionContext.getActionDesc().isEnableHttps()) {
			if (originHttps == false) {
				return false;
			}
		}

		// Host
		if (originUrl.getHost().equals(request.getServerName()) == false) {
			boolean matched = false;
			for (String domain : corsPolicy.getAllowDomains()) {
				if (domain.equals("*") || originUrl.getHost().equals(domain) || getPattern(domain).matcher(originUrl.getHost()).matches()) {
					matched = true;
					break;
				}
			}
			if (matched == false) {
				return false;
			}
		}

		// Port
		int originPort = originUrl.getPort() != -1 ? originUrl.getPort() : originUrl.getDefaultPort();
		if (originHttps) {
			if (originPort != getAppConfig().getHttpsPort()) {
				return false;
			}
		} else {
			if (originPort != getAppConfig().getHttpPort()) {
				return false;
			}
		}
		return true;
	}

	protected String buildAllowOrigin(HttpServletRequest request, ActionContext actionContext, URL originUrl) {
		StringBuilder sb = new StringBuilder();
		if (originUrl.getProtocol().equals("https") || actionContext.getActionDesc().isEnableHttps()) {
			sb.append("https://").append(request.getServerName());

			if (this.getAppConfig().getHttpsPort() != 443) {
				sb.append(':').append(this.getAppConfig().getHttpsPort());
			}
		} else {
			sb.append("http://").append(request.getServerName());
			if (this.getAppConfig().getHttpPort() != 80) {
				sb.append(':').append(this.getAppConfig().getHttpPort());
			}
		}
		return sb.toString();
	}

	// @Override
	public void handle(HttpServletRequest request, HttpServletResponse response, ActionContext actionContext) throws ServletException, IOException {
		// Origin
		String origin = request.getHeader("Origin");
		if (origin == null) {
			if (isTestContext()) {
				origin = request.getHeader("Test-Origin");
			}
			if (origin == null) {
				throw new HttpException(HttpServletResponse.SC_BAD_REQUEST, actionContext.getMessage(ServletUtils.ERROR_MSGKEY_BAD_REQUEST));
			}
		}

		URL originUrl = null;
		try {
			originUrl = new URL(origin);
		} catch (MalformedURLException ex) {
			throw new HttpException(HttpServletResponse.SC_BAD_REQUEST, actionContext.getMessage(ServletUtils.ERROR_MSGKEY_BAD_REQUEST));
		}

		// Policy
		String policy = actionContext.getActionDesc().getCorsPolicy();
		if (policy.isEmpty()) {
			policy = CorsPolicy.POLICY_DEFAULT;
		}
		final CorsPolicy corsPolicy = getCorsPolicyProvider().getCorsPolicy(policy);

		// allowOrigin
		String allowOrigin = null;
		if (allowOrigin(originUrl, corsPolicy, request, actionContext)) {
			allowOrigin = origin;
		} else {
			allowOrigin = buildAllowOrigin(request, actionContext, originUrl);
			if (getAppConfig().isProduction() == false) {
				response.setHeader(HEADER_CORS_ORIGIN_UNMATCHED, Boolean.TRUE.toString());
			}
		}

		// Allow-Origin
		response.setHeader(HEADER_CORS_ALLOW_ORIGIN, allowOrigin);

		// Allow-Methods
		response.setHeader(HEADER_CORS_ALLOW_METHODS, ServletUtils.getAllowMethods(actionContext.getActionDesc().getActionVerbs(), true, getAppConfig().isProduction() == false));

		// Allow-Headers
		if (corsPolicy.getAllowHeaders() != null) {
			response.setHeader(HEADER_CORS_ALLOW_HEADERS, corsPolicy.getAllowHeaders());
		}

		// Expose-Headers
		if (corsPolicy.getExposeHeaders() != null) {
			response.setHeader(HEADER_CORS_EXPOSE_HEADERS, corsPolicy.getExposeHeaders());
		}

		// Allow-Credentials
		response.setHeader(HEADER_CORS_ALLOW_CREDENTIALS, String.valueOf(corsPolicy.isAllowCredentials()));

		// Max-Age
		response.setHeader(HEADER_CORS_MAX_AGE, String.valueOf(corsPolicy.getMaxAge()));
	}

	protected boolean isTestContext() {
		return false;
	}
}
