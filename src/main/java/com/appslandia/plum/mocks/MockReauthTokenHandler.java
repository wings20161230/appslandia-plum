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

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.appslandia.common.crypto.MacBasedDigester;
import com.appslandia.common.crypto.MacBasedSigner;
import com.appslandia.common.crypto.TextEncryptor;
import com.appslandia.plum.web.ReauthTokenHandler;
import com.appslandia.plum.web.SessionConfig;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class MockReauthTokenHandler extends ReauthTokenHandler {

	@Inject
	private SessionConfig sessionConfig;

	private TextEncryptor tokenSinger;

	@PostConstruct
	protected void initialize() {
		MacBasedDigester digester = new MacBasedDigester();
		digester.setAlgorithm("HmacMD5").setPassword("password".toCharArray());

		MacBasedSigner signer = new MacBasedSigner().setMacBasedDigester(digester);
		this.tokenSinger = new TextEncryptor().setEncryptor(signer);
	}

	@Override
	public SessionConfig getSessionConfig() {
		return this.sessionConfig;
	}

	@Override
	public TextEncryptor getTokenSigner() {
		return this.tokenSinger;
	}
}
