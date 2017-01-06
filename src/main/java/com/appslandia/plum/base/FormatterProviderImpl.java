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

import com.appslandia.common.formatters.Formatter;
import com.appslandia.common.formatters.FormatterProvider;
import com.appslandia.common.formatters.LocalDateFormatter;
import com.appslandia.common.formatters.LocalDateTime24Formatter;
import com.appslandia.common.formatters.LocalDateTimeFormatter;
import com.appslandia.common.formatters.LocalTime24Formatter;
import com.appslandia.common.formatters.LocalTimeFormatter;
import com.appslandia.common.formatters.OffsetDateTime24Formatter;
import com.appslandia.common.formatters.OffsetDateTimeFormatter;
import com.appslandia.common.formatters.OffsetTime24Formatter;
import com.appslandia.common.formatters.OffsetTimeFormatter;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class FormatterProviderImpl extends FormatterProvider {

	@Override
	protected void initFormatters(Map<String, Formatter> map) {
		super.initFormatters(map);

		// JDK8+ Date/Time

		map.put(Formatter.LOCAL_DATE, new LocalDateFormatter());
		map.put(Formatter.LOCAL_TIME, new LocalTimeFormatter());
		map.put(Formatter.LOCAL_DATE_TIME, new LocalDateTimeFormatter());
		map.put(Formatter.LOCAL_TIME_24, new LocalTime24Formatter());
		map.put(Formatter.LOCAL_DATE_TIME_24, new LocalDateTime24Formatter());

		map.put(Formatter.OFFSET_TIME, new OffsetTimeFormatter());
		map.put(Formatter.OFFSET_DATE_TIME, new OffsetDateTimeFormatter());
		map.put(Formatter.OFFSET_TIME_24, new OffsetTime24Formatter());
		map.put(Formatter.OFFSET_DATE_TIME_24, new OffsetDateTime24Formatter());
	}
}
