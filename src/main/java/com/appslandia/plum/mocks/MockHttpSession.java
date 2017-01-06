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

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSessionContext;

import com.appslandia.common.base.Mutex;
import com.appslandia.plum.base.ServletUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@SuppressWarnings("deprecation")
public class MockHttpSession implements HttpSession {

	private static final AtomicInteger sessionIdGenerator = new AtomicInteger(0);

	private String sessionId;
	private boolean invalidated = false;
	private int maxInactiveInterval = (int) TimeUnit.SECONDS.convert(30, TimeUnit.MINUTES);

	private ServletContext servletContext;
	private Map<String, Object> attributes = new HashMap<>();

	public MockHttpSession(ServletContext servletContext) {
		this.servletContext = servletContext;
		this.sessionId = nextSessionId();
		this.attributes.put(ServletUtils.SESSION_ATTRIBUTE_MUTEX, new Mutex());
	}

	@Override
	public String getId() {
		assertUninvalidated();
		return this.sessionId;
	}

	@Override
	public long getCreationTime() {
		assertUninvalidated();
		throw new UnsupportedOperationException();
	}

	@Override
	public long getLastAccessedTime() {
		assertUninvalidated();
		throw new UnsupportedOperationException();
	}

	@Override
	public int getMaxInactiveInterval() {
		assertUninvalidated();
		return this.maxInactiveInterval;
	}

	@Override
	public void setMaxInactiveInterval(int maxInactiveInterval) {
		this.maxInactiveInterval = maxInactiveInterval;
	}

	@Override
	public ServletContext getServletContext() {
		return this.servletContext;
	}

	@Override
	public HttpSessionContext getSessionContext() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void invalidate() {
		assertUninvalidated();
		this.invalidated = true;
		clearAttributes();
	}

	public boolean isInvalidated() {
		return this.invalidated;
	}

	@Override
	public boolean isNew() {
		assertUninvalidated();
		throw new UnsupportedOperationException();
	}

	private void assertUninvalidated() {
		if (this.invalidated) {
			throw new IllegalStateException("Invalidated.");
		}
	}

	private static String nextSessionId() {
		return "session-" + sessionIdGenerator.incrementAndGet();
	}

	public String changeSessionId() {
		this.sessionId = nextSessionId();
		return this.sessionId;
	}

	@Override
	public Object getAttribute(String name) {
		assertUninvalidated();
		return this.attributes.get(name);
	}

	@Override
	public void setAttribute(String name, Object value) {
		assertUninvalidated();

		if (value != null) {
			this.attributes.put(name, value);
			if (value instanceof HttpSessionBindingListener) {
				((HttpSessionBindingListener) value).valueBound(new HttpSessionBindingEvent(this, name, value));
			}
		} else {
			removeAttribute(name);
		}
	}

	@Override
	public void removeAttribute(String name) {
		assertUninvalidated();

		Object value = this.attributes.remove(name);
		if (value instanceof HttpSessionBindingListener) {
			((HttpSessionBindingListener) value).valueUnbound(new HttpSessionBindingEvent(this, name, value));
		}
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		assertUninvalidated();
		return Collections.enumeration(this.attributes.keySet());
	}

	private void clearAttributes() {
		for (Iterator<Map.Entry<String, Object>> it = this.attributes.entrySet().iterator(); it.hasNext();) {
			Map.Entry<String, Object> entry = it.next();
			String name = entry.getKey();
			Object value = entry.getValue();
			it.remove();
			if (value instanceof HttpSessionBindingListener) {
				((HttpSessionBindingListener) value).valueUnbound(new HttpSessionBindingEvent(this, name, value));
			}
		}
	}

	@Override
	public Object getValue(String name) {
		return getAttribute(name);
	}

	@Override
	public void putValue(String name, Object value) {
		setAttribute(name, value);
	}

	@Override
	public void removeValue(String name) {
		removeAttribute(name);
	}

	@Override
	public String[] getValueNames() {
		assertUninvalidated();
		return this.attributes.keySet().toArray(new String[this.attributes.size()]);
	}
}
