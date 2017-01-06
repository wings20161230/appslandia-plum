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

package com.appslandia.plum.web;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import com.appslandia.common.base.ConstDesc;
import com.appslandia.common.base.InitializeException;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class ConstDescProvider {

	final Map<ConstKey, String> constMap = new HashMap<>();

	public String getDescKey(String constGroup, Object value) throws IllegalArgumentException {
		return this.constMap.get(new ConstKey(constGroup, value));
	}

	public void parseDescKeys(Class<?> constantClass) {
		Field[] fields = constantClass.getFields();
		try {
			for (Field field : fields) {
				// @ConstDesc
				if ((field.getDeclaredAnnotation(ConstDesc.class) != null) && Modifier.isPublic(field.getModifiers()) && Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers())) {
					ConstDesc constDesc = field.getDeclaredAnnotation(ConstDesc.class);

					// descKey: constGroup.constName
					String descKey = constDesc.value() + "." + field.getName().toLowerCase(Locale.ENGLISH);
					this.constMap.put(new ConstKey(constDesc.value(), field.get(null)), descKey);
				}
			}
		} catch (Exception ex) {
			throw new InitializeException(ex);
		}
	}

	private static class ConstKey {

		final String constGroup;
		final Object value;

		public ConstKey(String constGroup, Object value) {
			this.constGroup = constGroup;
			this.value = value;
		}

		@Override
		public boolean equals(Object obj) {
			ConstKey another = (ConstKey) obj;
			return Objects.equals(this.constGroup, another.constGroup) && Objects.equals(this.value, another.value);
		}

		@Override
		public int hashCode() {
			int hash = 1, p = 31;
			hash = p * hash + Objects.hashCode(this.constGroup);
			hash = p * hash + Objects.hashCode(this.value);
			return hash;
		}
	}
}
