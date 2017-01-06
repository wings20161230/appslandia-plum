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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import javax.servlet.ServletContext;

import com.appslandia.common.base.InitializeException;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public abstract class MessageBundleProviderBase implements MessageBundleProvider {

	public abstract ServletContext getServletContext();

	public abstract String getResourceDir(String language);

	public abstract String[] getResourceNames();

	protected void loadMessages(String location, Properties messages) throws Exception {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(getServletContext().getResourceAsStream(location), StandardCharsets.UTF_8))) {
			messages.load(br);
		}
	}

	protected MessageBundle initMessageBundle(String language) throws InitializeException {
		Properties messages = new Properties();
		String resourceDir = getResourceDir(language);
		for (String resourceName : getResourceNames()) {
			String location = resourceDir + "/" + resourceName;

			try {
				loadMessages(location, messages);
			} catch (Exception ex) {
				throw new InitializeException(ex);
			}
		}
		return new MessageBundle(messages);
	}
}
