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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.appslandia.common.utils.AssertUtils;
import com.appslandia.plum.base.MessageBundle;
import com.appslandia.plum.base.MessageBundleProvider;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class MockMessageBundleProvider implements MessageBundleProvider {

	private ConcurrentMap<String, MessageBundle> messageBundleMap = new ConcurrentHashMap<>();

	@Override
	public MessageBundle getMessageBundle(String language) throws IllegalArgumentException {
		return this.messageBundleMap.computeIfAbsent(language, l -> new MessageBundle());
	}

	public MockMessageBundleProvider addMessageBundle(String language, MessageBundle messageBundle) {
		AssertUtils.assertFalse(this.messageBundleMap.containsKey(language), "messageBundle is already existed.");
		this.messageBundleMap.put(language, messageBundle);
		return this;
	}
}
