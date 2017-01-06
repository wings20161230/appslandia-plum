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

import java.util.LinkedHashMap;
import java.util.Map;

import com.appslandia.common.base.ENLanguage;
import com.appslandia.common.base.Language;
import com.appslandia.common.base.VILanguage;
import com.appslandia.common.utils.AssertUtils;
import com.appslandia.plum.base.HeaderValues;
import com.appslandia.plum.base.LanguageProvider;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class MockLanguageProvider implements LanguageProvider {

	private Map<String, Language> languageMap = new LinkedHashMap<>();
	private Language defaultLanguage;

	public MockLanguageProvider() {
		initLanguages();
	}

	protected void initLanguages() {
		addLanguage(new ENLanguage(), true);
		addLanguage(new VILanguage(), false);
	}

	@Override
	public Language getDefault() {
		AssertUtils.assertNotNull(this.defaultLanguage, "No default language.");
		return this.defaultLanguage;
	}

	@Override
	public Language getLanguage(String id) {
		return this.languageMap.get(id);
	}

	@Override
	public String getLanguages() {
		return HeaderValues.toValueList(this.languageMap.keySet().toArray(new String[this.languageMap.size()]));
	}

	public MockLanguageProvider addLanguage(Language language) {
		return this.addLanguage(language, false);
	}

	public MockLanguageProvider addLanguage(Language language, boolean isDefault) {
		AssertUtils.assertFalse(this.languageMap.containsKey(language.getId()), "language is already existed.");

		this.languageMap.put(language.getId(), language);
		if (isDefault) {
			this.defaultLanguage = language;
		}
		return this;
	}
}
