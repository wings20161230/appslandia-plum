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

import com.appslandia.common.base.TextGenerator;
import com.appslandia.common.base.UrlSafeTextGenerator;
import com.appslandia.common.base.WordsGenerator;
import com.appslandia.common.crypto.DigesterImpl;
import com.appslandia.common.crypto.SaltedTextDigester;
import com.appslandia.common.crypto.TextDigester;
import com.appslandia.plum.base.AppConfig;
import com.appslandia.plum.web.ReauthTokenHandler;
import com.appslandia.plum.web.ReauthTokenReauthenticator;
import com.appslandia.plum.web.UserReauthManager;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class MockReauthTokenReauthenticator extends ReauthTokenReauthenticator {

	@Inject
	private AppConfig appConfig;

	@Inject
	private UserReauthManager userReauthManager;

	@Inject
	private ReauthTokenHandler reauthTokenHandler;

	final TextGenerator verCodeGenerator = new WordsGenerator().setLength(6);

	final TextGenerator tokenGenerator = new UrlSafeTextGenerator();

	final TextDigester tokenDigester = new SaltedTextDigester();

	final TextDigester sesTokenGenerator = new TextDigester().setDigester(new DigesterImpl("MD5"));

	@Override
	public AppConfig getAppConfig() {
		return this.appConfig;
	}

	@Override
	public UserReauthManager getUserReauthManager() {
		return this.userReauthManager;
	}

	@Override
	public ReauthTokenHandler getReauthTokenHandler() {
		return this.reauthTokenHandler;
	}

	@Override
	public TextGenerator getVerCodeGenerator() {
		return this.verCodeGenerator;
	}

	@Override
	public TextGenerator getTokenGenerator() {
		return this.tokenGenerator;
	}

	@Override
	public TextDigester getTokenDigester() {
		return this.tokenDigester;
	}

	@Override
	public TextDigester getSesTokenGenerator() {
		return this.sesTokenGenerator;
	}
}
