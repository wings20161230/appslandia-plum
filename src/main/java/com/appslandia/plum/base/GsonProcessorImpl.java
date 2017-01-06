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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;

import com.appslandia.common.gson.GsonProcessor;
import com.appslandia.common.gson.LocalDateAdapter;
import com.appslandia.common.gson.LocalDateTimeAdapter;
import com.appslandia.common.gson.LocalTimeAdapter;
import com.appslandia.common.gson.OffsetDateTimeAdapter;
import com.appslandia.common.gson.OffsetTimeAdapter;
import com.google.gson.GsonBuilder;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class GsonProcessorImpl extends GsonProcessor {

	@Override
	protected void registerDateAdapters(GsonBuilder builder) {
		super.registerDateAdapters(builder);

		builder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
		builder.registerTypeAdapter(LocalTime.class, new LocalTimeAdapter());
		builder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());

		builder.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeAdapter());
		builder.registerTypeAdapter(OffsetTime.class, new OffsetTimeAdapter());
	}
}
