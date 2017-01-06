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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class ModelState extends HashMap<String, ModelState.FieldErrors> {
	private static final long serialVersionUID = 1L;

	public static final String MODEL_FIELD = "__model";

	protected FieldErrors getFieldError(String fieldName) {
		FieldErrors fieldErrors = this.get(fieldName);
		if (fieldErrors == null) {
			fieldErrors = new FieldErrors();
			super.put(fieldName, fieldErrors);
		}
		return fieldErrors;
	}

	public void setParseError(String fieldName, String message) {
		getFieldError(fieldName).addError(message, true);
	}

	public void addModelError(String message) {
		addFieldError(MODEL_FIELD, message);
	}

	public void addFieldError(String fieldName, String message) {
		getFieldError(fieldName).addError(message, false);
	}

	public void removeErrors(String prefix) {
		Iterator<String> it = this.keySet().iterator();
		while (it.hasNext()) {
			if (it.next().startsWith(prefix)) {
				it.remove();
			}
		}
	}

	public void removeError(String fieldName) {
		remove(fieldName);
	}

	public boolean isModelValid() {
		return this.isEmpty();
	}

	public boolean isFieldValid(String fieldName) {
		return this.containsKey(fieldName) == false;
	}

	@Override
	public FieldErrors put(String key, FieldErrors value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void putAll(Map<? extends String, ? extends FieldErrors> m) {
		throw new UnsupportedOperationException();
	}

	@Override
	public FieldErrors putIfAbsent(String key, FieldErrors value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean remove(Object key, Object value) {
		throw new UnsupportedOperationException();
	}

	public static class FieldErrors {

		private String parseError;
		final List<String> errors = new ArrayList<>(1);;

		public void addError(String error, boolean isParseError) {
			if (isParseError) {
				this.parseError = error;
			} else if (this.parseError == null) {
				this.errors.add(error);
			}
		}

		public String getError() {
			return this.parseError != null ? this.parseError : this.errors.get(0);
		}

		public List<String> getErrors() {
			return this.errors;
		}
	}
}
