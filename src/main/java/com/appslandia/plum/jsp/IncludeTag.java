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

package com.appslandia.plum.jsp;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.jsp.JspException;

import com.appslandia.common.base.MemoryStream;
import com.appslandia.common.utils.AssertUtils;
import com.appslandia.plum.base.ActionContext;
import com.appslandia.plum.base.ActionDesc;
import com.appslandia.plum.base.ActionInvoker;
import com.appslandia.plum.base.ActionResult;
import com.appslandia.plum.base.ControllerDesc;
import com.appslandia.plum.base.ControllerDescProvider;
import com.appslandia.plum.base.ControllerProvider;
import com.appslandia.plum.base.MemoryResponseWrapper;
import com.appslandia.plum.base.ServletUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@Tag(name = "include", bodyContent = "scriptless", dynamicAttributes = false)
public class IncludeTag extends SimpleTagBase implements ParamSupport {

	protected String controller;
	protected String action;
	protected String errorKey;
	protected boolean cache;

	protected Map<String, Object> _params;

	public ControllerDescProvider getControllerDescProvider() {
		return (ControllerDescProvider) getServletContext().getAttribute(ServletUtils.CONTEXT_ATTRIBUTE_CONTROLLER_DESC_PROVIDER);
	}

	public ControllerProvider getControllerProvider() {
		return (ControllerProvider) getServletContext().getAttribute(ServletUtils.CONTEXT_ATTRIBUTE_CONTROLLER_PROVIDER);
	}

	public ActionInvoker getActionInvoker() {
		return (ActionInvoker) getServletContext().getAttribute(ServletUtils.CONTEXT_ATTRIBUTE_ACTION_INVOKER);
	}

	@Override
	public void putParam(String name, Object value) {
		if (this._params == null) {
			this._params = new HashMap<>();
		}
		this._params.put(name, value);
	}

	protected String buildContent(CacheKey key) throws Exception {
		// Child ActionContext
		ActionContext actionContext = createChildActionContext();

		// MemoryResponseWrapper
		int blockSize = (actionContext.getActionDesc().getOutputConfig() == null) ? 512 : Math.max(actionContext.getActionDesc().getOutputConfig().blockSize(), 512);
		MemoryResponseWrapper wrapper = new MemoryResponseWrapper(getResponse(), new MemoryStream(blockSize), false);

		// Save parameters
		getRequest().setAttribute(ServletUtils.REQUEST_ATTRIBUTE_INCLUDE_PARAMS, this._params);
		try {
			// Invoke action
			Object childController = getControllerProvider().getController(actionContext.getControllerDesc().getControllerClass(), actionContext.getControllerDesc().getQualifiers());
			Object result = getActionInvoker().invoke(getRequest(), wrapper, actionContext, childController);

			// execute
			AssertUtils.assertTrue(result instanceof ActionResult);
			((ActionResult) result).execute(getRequest(), wrapper, actionContext);

			// finishWrapper
			wrapper.finishWrapper();

			return wrapper.getContent().toString(wrapper.getCharacterEncoding());
		} finally {
			// Remove parameters
			getRequest().removeAttribute(ServletUtils.REQUEST_ATTRIBUTE_INCLUDE_PARAMS);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void doTag() throws JspException, IOException {
		// Invoke body
		if (getJspBody() != null) {
			getJspBody().invoke(null);
		}

		if (this.cache) {
			CacheKey key = new CacheKey(getActionContext().getLanguage().getId(), this.controller, this.action, this._params);

			// Cache
			Map<CacheKey, String> cache = (Map<CacheKey, String>) getServletContext().getAttribute(ServletUtils.CONTEXT_ATTRIBUTE_INCLUDE_TAG_CACHE);
			if (cache == null) {
				synchronized (ServletUtils.getMutex(getServletContext())) {
					cache = (Map<CacheKey, String>) getServletContext().getAttribute(ServletUtils.CONTEXT_ATTRIBUTE_INCLUDE_TAG_CACHE);
					if (cache == null) {
						cache = new ConcurrentHashMap<>();
						getServletContext().setAttribute(ServletUtils.CONTEXT_ATTRIBUTE_INCLUDE_TAG_CACHE, cache);
					}
				}
			}
			try {
				String content = cache.computeIfAbsent(key, k -> {
					try {
						return buildContent(k);
					} catch (Exception ex) {
						if (ex instanceof RuntimeException) {
							throw (RuntimeException) ex;
						}
						throw new RuntimeException(ex);
					}
				});
				getJspContext().getOut().write(content);

			} catch (Exception ex) {
				handleException(ex);
			}
		} else {
			// Not cache
			try {
				String content = buildContent(null);
				getJspContext().getOut().write(content);

			} catch (Exception ex) {
				handleException(ex);
			}
		}
	}

	protected void handleException(Exception ex) throws JspException, IOException {
		if (this.errorKey != null) {
			HtmlEscaper.writeEscapedXml(this.getActionContext().getMessage(this.errorKey), getJspContext().getOut());
		} else {
			if (ex instanceof RuntimeException) {
				throw (RuntimeException) ex;
			}
			if (ex instanceof JspException) {
				throw (JspException) ex;
			}
			if (ex instanceof IOException) {
				throw (IOException) ex;
			}
			throw new JspException(ex);
		}
	}

	protected ActionContext createChildActionContext() {
		ControllerDesc controllerDesc = this.getControllerDescProvider().getControllerDesc(this.controller);
		if (controllerDesc == null) {
			throw new IllegalArgumentException("Controller is not found (controller=" + this.controller + ")");
		}
		ActionDesc actionDesc = controllerDesc.getActionDesc(this.action);
		if (actionDesc == null) {
			throw new IllegalArgumentException("Action is not found (controller=" + this.controller + ", action=" + this.action + ")");
		}
		if (actionDesc.isChildAction() == false) {
			throw new IllegalArgumentException("Child action is required (controller=" + this.controller + ", action=" + this.action + ")");
		}
		return getActionContext().createActionContext(controllerDesc, actionDesc);
	}

	@Attribute(required = true, rtexprvalue = false)
	public void setController(String controller) {
		this.controller = controller;
	}

	@Attribute(required = true, rtexprvalue = false)
	public void setAction(String action) {
		this.action = action;
	}

	@Attribute(required = false, rtexprvalue = false)
	public void setErrorKey(String errorKey) {
		this.errorKey = errorKey;
	}

	@Attribute(required = false, rtexprvalue = false)
	public void setCache(boolean cache) {
		this.cache = cache;
	}

	private static class CacheKey {
		final String language;
		final String controller;
		final String action;
		final Map<String, Object> params;

		public CacheKey(String language, String controller, String action, Map<String, Object> params) {
			this.language = language;
			this.controller = controller;
			this.action = action;
			this.params = params;
		}

		@Override
		public int hashCode() {
			int hash = 1, p = 31;
			hash = p * hash + Objects.hashCode(this.language);
			hash = p * hash + Objects.hashCode(this.controller);
			hash = p * hash + Objects.hashCode(this.action);
			hash = p * hash + Objects.hashCode(this.params);
			return hash;
		}

		@Override
		public boolean equals(Object obj) {
			CacheKey another = (CacheKey) obj;

			// @formatter:off
			return Objects.equals(this.language, another.language) && 
				   Objects.equals(this.controller, another.controller) &&
				   Objects.equals(this.action, another.action) &&
				   Objects.equals(this.params, another.params);
			// @formatter:on
		}
	}
}
