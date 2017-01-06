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

import javax.inject.Inject;
import javax.servlet.ServletConfig;

import com.appslandia.common.base.JsonProcessor;
import com.appslandia.plum.base.ActionInvoker;
import com.appslandia.plum.base.AppConfig;
import com.appslandia.plum.base.ControllerProvider;
import com.appslandia.plum.base.CorsPolicyHandler;
import com.appslandia.plum.base.RemoteClientFilter;
import com.appslandia.plum.wpi.WebApiRequestExecutor;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class MockWebApiRequestExecutor extends WebApiRequestExecutor {
	private static final long serialVersionUID = 1L;

	@Inject
	private AppConfig appConfig;

	@Inject
	private RemoteClientFilter remoteClientFilter;

	@Inject
	private CorsPolicyHandler corsPolicyHandler;

	@Inject
	private ControllerProvider controllerProvider;

	@Inject
	private ActionInvoker actionInvoker;

	@Inject
	private JsonProcessor jsonProcessor;

	@Override
	public AppConfig getAppConfig() {
		return this.appConfig;
	}

	@Override
	public RemoteClientFilter getRemoteClientFilter() {
		return this.remoteClientFilter;
	}

	@Override
	public CorsPolicyHandler getCorsPolicyHandler() {
		return this.corsPolicyHandler;
	}

	@Override
	public ControllerProvider getControllerProvider() {
		return this.controllerProvider;
	}

	@Override
	public ActionInvoker getActionInvoker() {
		return this.actionInvoker;
	}

	@Override
	public JsonProcessor getJsonProcessor() {
		return this.jsonProcessor;
	}

	private MockServletConfig servletConfig;

	@Override
	public ServletConfig getServletConfig() {
		return this.servletConfig;
	}

	public MockWebApiRequestExecutor setServletConfig(MockServletConfig servletConfig) {
		this.servletConfig = servletConfig;
		return this;
	}
}
