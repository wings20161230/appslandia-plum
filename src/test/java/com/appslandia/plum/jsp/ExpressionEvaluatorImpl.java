package com.appslandia.plum.jsp;

import javax.servlet.jsp.PageContext;

import org.apache.commons.beanutils.PropertyUtils;

import com.appslandia.common.utils.AssertUtils;
import com.appslandia.plum.base.ServletUtils;

public class ExpressionEvaluatorImpl extends ExpressionEvaluator {

	@Override
	public Object getValue(PageContext pc, String property) {
		AssertUtils.assertTrue(property.startsWith("__model."), "Property is invalid.");

		Object model = pc.getRequest().getAttribute(ServletUtils.REQUEST_ATTRIBUTE_MODEL);

		String nestedProp = property.substring("__model.".length());
		try {
			return PropertyUtils.getNestedProperty(model, nestedProp);
		} catch (Exception ex) {
			throw new javax.el.ELException(ex);
		}
	}
}
