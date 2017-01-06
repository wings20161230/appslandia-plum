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

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class ParamDesc {

	private Class<?> paramType;
	private String paramName;

	private String requestParam;
	private boolean pathParam;

	private String formatterId;
	private FormModel formModel;
	private JsonModel jsonModel;

	public Class<?> getParamType() {
		return this.paramType;
	}

	protected void setParamType(Class<?> paramType) {
		this.paramType = paramType;
	}

	public String getParamName() {
		return this.paramName;
	}

	protected void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public String getRequestParam() {
		return this.requestParam;
	}

	protected void setRequestParam(String requestParam) {
		this.requestParam = requestParam;
	}

	public boolean isPathParam() {
		return this.pathParam;
	}

	protected void setPathParam(boolean pathParam) {
		this.pathParam = pathParam;
	}

	public String getFormatterId() {
		return this.formatterId;
	}

	protected void setFormatterId(String formatterId) {
		this.formatterId = formatterId;
	}

	public FormModel getFormModel() {
		return this.formModel;
	}

	protected void setFormModel(FormModel formModel) {
		this.formModel = formModel;
	}

	public JsonModel getJsonModel() {
		return this.jsonModel;
	}

	protected void setJsonModel(JsonModel jsonModel) {
		this.jsonModel = jsonModel;
	}
}
