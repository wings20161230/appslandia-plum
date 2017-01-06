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

package com.appslandia.plum.jsp;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import com.appslandia.common.base.TextBuilder;
import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.StringUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class TagTldGenerator {

	public static void main(String[] args) throws Exception {
		TextBuilder sb = new TextBuilder();

		Set<Class<?>> tagClasses = new TreeSet<Class<?>>(new Comparator<Class<?>>() {
			@Override
			public int compare(Class<?> c1, Class<?> c2) {
				return c1.getSimpleName().compareTo(c2.getSimpleName());
			}
		});

		tagClasses.add(FieldLabelTag.class);
		tagClasses.add(FieldErrorTag.class);
		tagClasses.add(FormErrorsTag.class);
		tagClasses.add(FieldOrdersTag.class);

		tagClasses.add(ConstDescTag.class);
		tagClasses.add(CsrfTokenTag.class);
		tagClasses.add(CsrfParamTag.class);

		tagClasses.add(CaptchaIdTag.class);
		tagClasses.add(CaptchaWordsTag.class);
		tagClasses.add(CaptchaImageTag.class);

		tagClasses.add(DynFormTag.class);
		tagClasses.add(DataListTag.class);
		tagClasses.add(TextBoxTag.class);
		tagClasses.add(TextAreaTag.class);
		tagClasses.add(RadioTag.class);
		tagClasses.add(CheckboxTag.class);
		tagClasses.add(BitMaskCheckboxTag.class);
		tagClasses.add(HiddenTag.class);

		tagClasses.add(SelectTag.class);
		tagClasses.add(OptionTag.class);
		tagClasses.add(ParamTag.class);

		tagClasses.add(ActionTag.class);
		tagClasses.add(DynActionTag.class);
		tagClasses.add(ActionImageTag.class);
		tagClasses.add(ActionLinkTag.class);

		tagClasses.add(LinkTextTag.class);
		tagClasses.add(IncludeTag.class);

		tagClasses.add(ForEachTag.class);
		tagClasses.add(IfTag.class);
		tagClasses.add(SwitchTag.class);
		tagClasses.add(CaseTag.class);
		tagClasses.add(DefaultTag.class);

		for (Class<?> clazz : tagClasses) {
			generateTag(clazz, sb);
		}

		System.out.println(sb);
	}

	protected static void generateTag(Class<?> tagClass, TextBuilder sb) throws Exception {
		Tag tag = tagClass.getDeclaredAnnotation(Tag.class);
		AssertUtils.assertNotNull(tag, "@Tag not found.");

		String name = tag.name().length() == 0 ? tagClass.getSimpleName().substring(0, tagClass.getSimpleName().lastIndexOf("Tag")) : tag.name();
		String desc = tag.description().length() == 0 ? name : tag.description();

		if (sb.length() == 0) {
			sb.appendtab().append("<tag>");
			sb.appendln();
		} else {
			sb.appendln(2).appendtab().append("<tag>");
			sb.appendln();
		}

		sb.appendtab(2).append("<description>" + desc + "</description>");
		sb.appendln();
		sb.appendtab(2).append("<name>" + name + "</name>");
		sb.appendln();
		sb.appendtab(2).append("<tag-class>" + tagClass.getName() + "</tag-class>");
		sb.appendln();
		sb.appendtab(2).append("<body-content>" + tag.bodyContent() + "</body-content>");
		sb.appendln();

		for (PropertyDescriptor pd : Introspector.getBeanInfo(tagClass).getPropertyDescriptors()) {
			if (pd.getWriteMethod() != null && (pd.getWriteMethod().getDeclaredAnnotation(Attribute.class) != null)) {

				Class<?> type = pd.getWriteMethod().getParameterTypes()[0];

				Attribute attribute = pd.getWriteMethod().getDeclaredAnnotation(Attribute.class);
				String attname = StringUtils.firstLowerCase(pd.getName());
				String attdesc;

				if (attribute.description().isEmpty() == false) {
					attdesc = attribute.description();
				} else {
					if (attribute.rtexprvalue()) {
						attdesc = attname + ": " + type.getName() + " (expression)";
					} else {
						attdesc = attname + ": " + type.getName();
					}
				}

				sb.appendtab(2).append("<attribute>");
				sb.appendln();
				sb.appendtab(3).append("<description>" + attdesc + "</description>");
				sb.appendln();
				sb.appendtab(3).append("<name>" + attname + "</name>");
				sb.appendln();
				sb.appendtab(3).append("<required>" + attribute.required() + "</required>");
				sb.appendln();
				sb.appendtab(3).append("<rtexprvalue>" + attribute.rtexprvalue() + "</rtexprvalue>");
				sb.appendln();
				sb.appendtab(3).append("<type>" + type.getName() + "</type>");
				sb.appendln();
				sb.appendtab(2).append("</attribute>");
				sb.appendln();
			}
		}

		sb.appendtab(2).append("<dynamic-attributes>" + tag.dynamicAttributes() + "</dynamic-attributes>");
		sb.appendln();
		sb.appendtab().append("</tag>");
	}
}
