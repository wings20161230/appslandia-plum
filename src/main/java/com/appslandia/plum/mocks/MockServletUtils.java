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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class MockServletUtils {

	private static final String DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";
	private static final TimeZone GMT = TimeZone.getTimeZone("GMT");

	public static String toDateHeaderString(long timeInMillis) {
		SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT, Locale.US);
		format.setTimeZone(GMT);
		return format.format(new Date(timeInMillis));
	}

	public static long parseDateHeader(String value) {
		SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT, Locale.US);
		format.setTimeZone(GMT);
		try {
			return format.parse(value).getTime();
		} catch (ParseException ex) {
			throw new IllegalArgumentException("Date header is invalid (value=" + value + ")");
		}
	}

	public static long noMilliseconds(long timeInMillis) {
		Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(timeInMillis);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTimeInMillis();
	}

	public static void addHeaderValues(Map<String, MockHttpHeader> headers, String name, Object[] values) {
		MockHttpHeader header = MockHttpHeader.getByName(headers, name);
		if (header == null) {
			header = new MockHttpHeader();
			headers.put(name, header);
		}
		header.addValues(values);
	}

	public static void setHeaderValues(Map<String, MockHttpHeader> headers, String name, Object[] values) {
		MockHttpHeader header = MockHttpHeader.getByName(headers, name);
		if (header == null) {
			header = new MockHttpHeader();
			headers.put(name, header);
		}
		header.setValues(values);
	}

	public static long getDateHeader(Map<String, MockHttpHeader> headers, String name) {
		MockHttpHeader header = MockHttpHeader.getByName(headers, name);
		Object value = (header != null ? header.getValue() : null);
		if (value instanceof Date) {
			return noMilliseconds(((Date) value).getTime());
		}
		if (value instanceof Number) {
			return noMilliseconds(((Number) value).longValue());
		}
		if (value instanceof String) {
			return MockServletUtils.parseDateHeader((String) value);
		}
		if (value != null) {
			throw new IllegalArgumentException("Date header is invalid (value=" + value + ")");
		}
		return -1L;
	}

	public static int getIntHeader(Map<String, MockHttpHeader> headers, String name) {
		MockHttpHeader header = MockHttpHeader.getByName(headers, name);
		Object value = (header != null ? header.getValue() : null);
		if (value instanceof Number) {
			return ((Number) value).intValue();
		}
		if (value instanceof String) {
			return Integer.parseInt((String) value);
		}
		if (value != null) {
			throw new IllegalArgumentException("Int header is invalid (value=" + value + ")");
		}
		return -1;
	}

	public static Cookie createCookie(ServletContext sc, String name, String value, int maxAge, boolean isSecure, boolean httpOnly, String domain, String path) {
		Cookie cookie = new Cookie(name, value);
		if (domain != null) {
			cookie.setDomain(domain);

		} else if (sc.getSessionCookieConfig().getDomain() != null) {
			cookie.setDomain(sc.getSessionCookieConfig().getDomain());
		}

		if (path != null) {
			cookie.setPath(path);
		} else {
			cookie.setPath(sc.getSessionCookieConfig().getPath());
		}

		if (maxAge >= 0) {
			cookie.setMaxAge(maxAge);
		}
		cookie.setSecure(isSecure);
		cookie.setHttpOnly(httpOnly);
		return cookie;
	}

	public static Cookie createSessionCookie(ServletContext sc, String value) {
		Cookie cookie = new Cookie(sc.getSessionCookieConfig().getName(), value);

		if (sc.getSessionCookieConfig().getDomain() != null) {
			cookie.setDomain(sc.getSessionCookieConfig().getDomain());
		}
		cookie.setPath(sc.getSessionCookieConfig().getPath());

		cookie.setSecure(sc.getSessionCookieConfig().isSecure());
		cookie.setHttpOnly(sc.getSessionCookieConfig().isHttpOnly());
		return cookie;
	}

	public static Cookie getCookie(List<Cookie> cookies, String name) {
		for (Cookie cookie : cookies) {
			if (name.equals(cookie.getName())) {
				return cookie;
			}
		}
		return null;
	}

	public static Cookie[] toCookies(List<Cookie> cookies) {
		Cookie[] cks = new Cookie[cookies.size()];
		cookies.toArray(cks);
		return cks;
	}

	public static void addParameter(Map<String, String[]> parameterMap, String name, String... moreValues) {
		String[] values = parameterMap.get(name);
		if (values == null) {
			parameterMap.put(name, moreValues);
		} else {
			String[] mergedValues = new String[values.length + moreValues.length];
			System.arraycopy(values, 0, mergedValues, 0, values.length);
			System.arraycopy(moreValues, 0, mergedValues, values.length, moreValues.length);

			parameterMap.put(name, mergedValues);
		}
	}
}
