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

import java.lang.reflect.Method;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class ActionDesc {

	private String action;
	private Method method;
	private PathParam[] pathParams;
	private ParamDesc[] paramDescs;

	private boolean childAction;
	private String[] actionVerbs;
	private Authorize authorize;
	private String[] headerPolicys;

	private String corsPolicy;
	private boolean enableHttps;
	private EnableEtag enableEtag;
	private EnableCsrf enableCsrf;
	private EnableCaptcha enableCaptcha;

	private OutputConfig outputConfig;
	private DisableGzip disableGzip;
	private Async async;

	public ParamDesc getParamDesc(String paramName) {
		for (ParamDesc paramDesc : this.paramDescs) {
			if (paramDesc.getParamName().equals(paramName)) {
				return paramDesc;
			}
		}
		return null;
	}

	public boolean allowActionVerb(String actionVerb) {
		for (String verb : this.actionVerbs) {
			if (actionVerb.equals(verb)) {
				return true;
			}
		}
		return false;
	}

	public String getAction() {
		return this.action;
	}

	protected void setAction(String action) {
		this.action = action;
	}

	public Method getMethod() {
		return this.method;
	}

	protected void setMethod(Method method) {
		this.method = method;
	}

	public PathParam[] getPathParams() {
		return this.pathParams;
	}

	protected void setPathParams(PathParam[] pathParams) {
		this.pathParams = pathParams;
	}

	public ParamDesc[] getParamDescs() {
		return this.paramDescs;
	}

	protected void setParamDescs(ParamDesc[] paramDescs) {
		this.paramDescs = paramDescs;
	}

	public boolean isChildAction() {
		return this.childAction;
	}

	protected void setChildAction(boolean childAction) {
		this.childAction = childAction;
	}

	public String[] getActionVerbs() {
		return this.actionVerbs;
	}

	protected void setActionVerbs(String[] actionVerbs) {
		this.actionVerbs = actionVerbs;
	}

	public Authorize getAuthorize() {
		return this.authorize;
	}

	protected void setAuthorize(Authorize authorize) {
		this.authorize = authorize;
	}

	public String[] getHeaderPolicys() {
		return this.headerPolicys;
	}

	protected void setHeaderPolicys(String[] headerPolicys) {
		this.headerPolicys = headerPolicys;
	}

	public String getCorsPolicy() {
		return this.corsPolicy;
	}

	protected void setCorsPolicy(String corsPolicy) {
		this.corsPolicy = corsPolicy;
	}

	public boolean isEnableHttps() {
		return this.enableHttps;
	}

	protected void setEnableHttps(boolean enableHttps) {
		this.enableHttps = enableHttps;
	}

	public EnableEtag getEnableEtag() {
		return this.enableEtag;
	}

	protected void setEnableEtag(EnableEtag enableEtag) {
		this.enableEtag = enableEtag;
	}

	public EnableCsrf getEnableCsrf() {
		return this.enableCsrf;
	}

	protected void setEnableCsrf(EnableCsrf enableCsrf) {
		this.enableCsrf = enableCsrf;
	}

	public EnableCaptcha getEnableCaptcha() {
		return this.enableCaptcha;
	}

	protected void setEnableCaptcha(EnableCaptcha enableCaptcha) {
		this.enableCaptcha = enableCaptcha;
	}

	public OutputConfig getOutputConfig() {
		return this.outputConfig;
	}

	protected void setOutputConfig(OutputConfig outputConfig) {
		this.outputConfig = outputConfig;
	}

	public DisableGzip getDisableGzip() {
		return this.disableGzip;
	}

	protected void setDisableGzip(DisableGzip disableGzip) {
		this.disableGzip = disableGzip;
	}

	public Async getAsync() {
		return this.async;
	}

	protected void setAsync(Async async) {
		this.async = async;
	}
}
