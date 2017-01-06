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

package com.appslandia.plum.mocks;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.appslandia.common.base.JsonProcessor;
import com.appslandia.plum.base.ActionParser;
import com.appslandia.plum.base.AppConfig;
import com.appslandia.plum.base.Authenticator;
import com.appslandia.plum.base.HeaderPolicyProvider;
import com.appslandia.plum.base.LanguageProvider;
import com.appslandia.plum.base.MessageBundleProvider;
import com.appslandia.plum.base.RequestErrorLogger;
import com.appslandia.plum.wpi.WebApiRequestInitializer;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class MockWebApiRequestInitializer extends WebApiRequestInitializer {

	@Inject
	private AppConfig appConfig;

	@Inject
	private LanguageProvider languageProvider;

	@Inject
	private MessageBundleProvider messageBundleProvider;

	@Inject
	private Authenticator authenticator;

	@Inject
	private HeaderPolicyProvider headerPolicyProvider;

	@Inject
	private ActionParser actionParser;

	@Inject
	private JsonProcessor jsonProcessor;

	@Inject
	private RequestErrorLogger requestErrorLogger;

	@Override
	public AppConfig getAppConfig() {
		return this.appConfig;
	}

	@Override
	public LanguageProvider getLanguageProvider() {
		return this.languageProvider;
	}

	@Override
	public MessageBundleProvider getMessageBundleProvider() {
		return this.messageBundleProvider;
	}

	@Override
	public Authenticator getAuthenticator() {
		return this.authenticator;
	}

	@Override
	public HeaderPolicyProvider getHeaderPolicyProvider() {
		return this.headerPolicyProvider;
	}

	@Override
	public ActionParser getActionParser() {
		return this.actionParser;
	}

	@Override
	public JsonProcessor getJsonProcessor() {
		return this.jsonProcessor;
	}

	@Override
	public RequestErrorLogger getRequestErrorLogger() {
		return this.requestErrorLogger;
	}

	@Override
	public void sendError(HttpServletRequest request, HttpServletResponse response, Throwable error) throws ServletException, IOException {
		request.setAttribute(RequestDispatcher.ERROR_EXCEPTION, error);
		super.sendError(request, response, error);
	}

	@Override
	protected boolean isTestContext() {
		return true;
	}
}
