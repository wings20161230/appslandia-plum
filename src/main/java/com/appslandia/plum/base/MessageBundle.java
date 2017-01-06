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

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.appslandia.common.utils.StringFormatUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class MessageBundle extends MapAccessor<String, String> {

	protected final Map<String, String> messageMap = new HashMap<>();

	public MessageBundle() {
	}

	public MessageBundle(Properties messages) {
		for (Map.Entry<Object, Object> msg : messages.entrySet()) {
			String key = ((String) msg.getKey());
			String value = ((String) msg.getValue());

			if ((key.isEmpty() == false) && (value.isEmpty() == false)) {
				this.messageMap.put(key, value);
			}
		}
	}

	protected String buildMissingMsg(String key) {
		return "?" + key;
	}

	@Override
	public String get(Object key) {
		return getMessage((String) key);
	}

	public String get(String key, String defaultMsg) {
		String msg = this.messageMap.get(key);
		if (msg == null) {
			return defaultMsg;
		}
		return msg;
	}

	public String getMessage(String key) {
		String msg = this.messageMap.get(key);
		if (msg == null) {
			return buildMissingMsg(key);
		}
		return msg;
	}

	public String getMessage(String key, Object... params) {
		String msg = this.messageMap.get(key);
		if (msg == null) {
			return buildMissingMsg(key);
		}
		return StringFormatUtils.format(msg, params);
	}

	public String getMessage(String key, Map<String, Object> params) {
		String msg = this.messageMap.get(key);
		if (msg == null) {
			return buildMissingMsg(key);
		}
		return StringFormatUtils.format(msg, params);
	}
}
