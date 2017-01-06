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

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.appslandia.common.utils.AssertUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class RequestWrapper extends HttpServletRequestWrapper {

	final Map<String, String[]> mergedParamMap;

	public RequestWrapper(HttpServletRequest request, Map<String, String> pathParamMap) {
		super(request);
		this.mergedParamMap = (pathParamMap.isEmpty() == false) ? this.mergeParameters(pathParamMap) : (null);
	}

	@Override
	public boolean isUserInRole(String role) {
		ActionContext ac = (ActionContext) super.getAttribute(ServletUtils.REQUEST_ATTRIBUTE_ACTION_CONTEXT);
		AssertUtils.assertNotNull(ac);

		if (ac.getUserData() != null) {
			if (ac.getUserData().getUserRoles().contains(role)) {
				return true;
			}
		}
		return super.isUserInRole(role);
	}

	@Override
	public String getParameter(String name) {
		if (this.mergedParamMap == null) {
			return super.getParameter(name);
		}
		String[] values = this.mergedParamMap.get(name);
		return (values != null) ? values[0] : null;
	}

	@Override
	public String[] getParameterValues(String name) {
		if (this.mergedParamMap == null) {
			return super.getParameterValues(name);
		}
		return this.mergedParamMap.get(name);
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		if (this.mergedParamMap == null) {
			return super.getParameterMap();
		}
		return this.mergedParamMap;
	}

	@Override
	public Enumeration<String> getParameterNames() {
		if (this.mergedParamMap == null) {
			return super.getParameterNames();
		}
		return Collections.enumeration(this.mergedParamMap.keySet());
	}

	protected Map<String, String[]> mergeParameters(Map<String, String> pathParamMap) {
		// ParameterMap
		ParameterMap map = new ParameterMap();

		// Path Parameters
		for (Entry<String, String> param : pathParamMap.entrySet()) {
			map.put(param.getKey(), new String[] { param.getValue() });
		}

		// Request Parameters
		for (Entry<String, String[]> param : this.getRequest().getParameterMap().entrySet()) {
			String[] values = map.get(param.getKey());
			if (values == null) {
				map.put(param.getKey(), param.getValue());
			} else {
				String[] mergedValues = new String[values.length + param.getValue().length];
				System.arraycopy(values, 0, mergedValues, 0, values.length);
				System.arraycopy(param.getValue(), 0, mergedValues, values.length, param.getValue().length);

				map.put(param.getKey(), mergedValues);
			}
		}
		map.setLocked(true);
		return map;
	}

	private static class ParameterMap extends HashMap<String, String[]> {
		private static final long serialVersionUID = 1L;

		private boolean locked = false;

		public ParameterMap() {
			super();
		}

		protected void setLocked(boolean locked) {
			this.locked = locked;
		}

		@Override
		public String[] put(String key, String[] value) {
			if (this.locked) {
				throw new IllegalStateException(this + "locked.");
			}
			return super.put(key, value);
		}

		@Override
		public void putAll(Map<? extends String, ? extends String[]> m) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void clear() {
			throw new UnsupportedOperationException();
		}

		@Override
		public String[] remove(Object key) {
			throw new UnsupportedOperationException();
		}
	}
}
