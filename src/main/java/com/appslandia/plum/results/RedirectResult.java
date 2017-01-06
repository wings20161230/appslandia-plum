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

package com.appslandia.plum.results;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.UriBuilder;

import com.appslandia.common.utils.AssertUtils;
import com.appslandia.plum.base.ActionContext;
import com.appslandia.plum.base.ActionParser;
import com.appslandia.plum.base.ActionResult;
import com.appslandia.plum.base.ServletUtils;
import com.appslandia.plum.web.Messages;
import com.appslandia.plum.web.TempData;
import com.appslandia.plum.web.TempDataManager;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class RedirectResult extends ActionResult {

	private String controller;
	private String action;
	private Map<String, Object> params;

	private String internalUrl;
	private ActionParser actionParser;
	private TempDataManager tempDataManager;

	public RedirectResult(String internalUrl) {
		this.internalUrl = internalUrl;
	}

	public RedirectResult(String action, String controller) {
		this.action = action;
		this.controller = controller;
	}

	public RedirectResult param(String name, Object value) {
		if (this.params == null) {
			this.params = new HashMap<>();
		}
		this.params.put(name, value);
		return this;
	}

	public RedirectResult setActionParser(ActionParser actionParser) {
		this.actionParser = actionParser;
		return this;
	}

	public RedirectResult setTempDataManager(TempDataManager tempDataManager) {
		this.tempDataManager = tempDataManager;
		return this;
	}

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response, ActionContext actionContext) throws Exception {
		// internalUrl?
		if (this.internalUrl != null) {
			handleInternalUrl(request, response);
			return;
		}

		// Defaults
		if (this.controller == null) {
			this.controller = actionContext.getController();
		}

		String tempDataId = saveTempData(request);
		if (tempDataId != null) {
			param(ServletUtils.PARAM_TEMP_DATA_ID, tempDataId);
		}

		String url = AssertUtils.assertNotNull(this.actionParser).toActionUrl(request, null, this.controller, this.action, this.params);
		response.sendRedirect(response.encodeRedirectURL(url));
	}

	protected void handleInternalUrl(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String tempDataId = saveTempData(request);
		if (tempDataId != null) {
			URI uri = UriBuilder.fromUri(this.internalUrl).queryParam(ServletUtils.PARAM_TEMP_DATA_ID, tempDataId).build();
			response.sendRedirect(response.encodeRedirectURL(uri.toString()));
		} else {
			response.sendRedirect(response.encodeRedirectURL(this.internalUrl));
		}
	}

	protected String saveTempData(HttpServletRequest request) throws Exception {
		TempData tempData = (TempData) request.getAttribute(ServletUtils.REQUEST_ATTRIBUTE_TEMP_DATA);
		Messages msgs = (Messages) request.getAttribute(ServletUtils.REQUEST_ATTRIBUTE_MESSAGES);

		if ((msgs != null) && (msgs.isEmpty() == false)) {
			if (tempData == null) {
				tempData = new TempData();
			}
			tempData.put(ServletUtils.REQUEST_ATTRIBUTE_MESSAGES, msgs);
		}
		if ((tempData != null) && (tempData.isEmpty() == false)) {
			return AssertUtils.assertNotNull(this.tempDataManager).saveTempData(request, tempData);
		}
		return null;
	}
}
