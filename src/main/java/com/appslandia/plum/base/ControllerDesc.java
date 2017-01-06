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

import java.lang.annotation.Annotation;
import java.util.Map;

import com.appslandia.common.base.CaseInsensitiveMap;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class ControllerDesc {

	private String controller;
	private Class<?> controllerClass;
	private Annotation[] qualifiers;

	final Map<String, ActionDesc> actionDescMap = new CaseInsensitiveMap<ActionDesc>();

	public String getController() {
		return this.controller;
	}

	protected void setController(String controller) {
		this.controller = controller;
	}

	public Class<?> getControllerClass() {
		return this.controllerClass;
	}

	protected void setControllerClass(Class<?> controllerClass) {
		this.controllerClass = controllerClass;
	}

	public Annotation[] getQualifiers() {
		return this.qualifiers;
	}

	protected void setQualifiers(Annotation[] qualifiers) {
		this.qualifiers = qualifiers;
	}

	public Map<String, ActionDesc> getActionDescMap() {
		return this.actionDescMap;
	}

	public ActionDesc getActionDesc(String action) {
		return this.actionDescMap.get(action);
	}
}
