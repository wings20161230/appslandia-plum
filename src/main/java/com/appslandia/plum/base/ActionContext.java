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

import java.util.Map;

import com.appslandia.common.base.FormatProvider;
import com.appslandia.common.base.Language;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class ActionContext {

	private String languageId;
	private ControllerDesc controllerDesc;
	private ActionDesc actionDesc;

	private Language language;
	private FormatProvider formatProvider;
	private MessageBundle messageBundle;
	private UserData userData;

	public boolean isAuthenticated() {
		return this.userData != null;
	}

	public boolean isAuthApp() {
		if (this.userData == null) {
			throw new IllegalStateException();
		}
		return this.userData.getAuthType() == AuthTypes.APP;
	}

	public ActionContext createActionContext(ControllerDesc controllerDesc, ActionDesc actionDesc) {
		ActionContext ac = new ActionContext();
		ac.setLanguageId(this.languageId);
		ac.setControllerDesc(controllerDesc);
		ac.setActionDesc(actionDesc);

		ac.setLanguage(this.language);
		ac.setFormatProvider(this.formatProvider);
		ac.setMessageBundle(this.messageBundle);
		ac.setUserData(this.userData);
		return ac;
	}

	public String getLanguageId() {
		return this.languageId;
	}

	protected void setLanguageId(String languageId) {
		this.languageId = languageId;
	}

	public String getController() {
		return this.controllerDesc.getController();
	}

	public String getAction() {
		return this.actionDesc.getAction();
	}

	public ControllerDesc getControllerDesc() {
		return this.controllerDesc;
	}

	protected void setControllerDesc(ControllerDesc controllerDesc) {
		this.controllerDesc = controllerDesc;
	}

	public ActionDesc getActionDesc() {
		return this.actionDesc;
	}

	protected void setActionDesc(ActionDesc actionDesc) {
		this.actionDesc = actionDesc;
	}

	public Language getLanguage() {
		return this.language;
	}

	protected void setLanguage(Language language) {
		this.language = language;
	}

	public FormatProvider getFormatProvider() {
		return this.formatProvider;
	}

	protected void setFormatProvider(FormatProvider formatProvider) {
		this.formatProvider = formatProvider;
	}

	public MessageBundle getMessageBundle() {
		return this.messageBundle;
	}

	protected void setMessageBundle(MessageBundle messageBundle) {
		this.messageBundle = messageBundle;
	}

	public UserData getUserData() {
		return this.userData;
	}

	protected void setUserData(UserData userData) {
		this.userData = userData;
	}

	public String getMessage(String key) {
		return this.messageBundle.getMessage(key);
	}

	public String getMessage(String key, Object... params) {
		return this.messageBundle.getMessage(key, params);
	}

	public String getMessage(String key, Map<String, Object> params) {
		return this.messageBundle.getMessage(key, params);
	}
}
