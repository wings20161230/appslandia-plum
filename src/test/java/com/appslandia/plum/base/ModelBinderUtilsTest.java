package com.appslandia.plum.base;

import java.lang.reflect.Array;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.appslandia.common.base.FormatProvider;
import com.appslandia.common.base.FormatProviderImpl;
import com.appslandia.common.base.Language;
import com.appslandia.common.formatters.Formatter;
import com.appslandia.common.formatters.FormatterProvider;

public class ModelBinderUtilsTest {

	FormatterProvider formatterProvider;
	Language language;

	@Before
	public void initialize() {
		formatterProvider = new FormatterProvider();
		language = Language.getDefault();
	}

	@Test
	public void test_parse() {
		// Integer
		Formatter formatter = formatterProvider.getFormatter(Integer.class);
		FormatProvider formatProvider = new FormatProviderImpl(language);

		// OK input - primitive
		ModelBinderUtils.ParseResult parseResult = new ModelBinderUtils.ParseResult();
		Object result = ModelBinderUtils.parse("100", int.class, parseResult, formatter, formatProvider);
		Assert.assertNotNull(result);
		Assert.assertEquals(result, Integer.valueOf(100));
		Assert.assertFalse(parseResult.isError());

		// OK input - wrapper
		parseResult = new ModelBinderUtils.ParseResult();
		result = ModelBinderUtils.parse("100,000", Integer.class, parseResult, formatter, formatProvider);
		Assert.assertNotNull(result);
		Assert.assertEquals(result, Integer.valueOf(100000));
		Assert.assertFalse(parseResult.isError());

		// Null input - primitive
		parseResult = new ModelBinderUtils.ParseResult();
		result = ModelBinderUtils.parse(null, int.class, parseResult, formatter, formatProvider);
		Assert.assertNotNull(result);
		Assert.assertEquals(result, Integer.valueOf(0));
		Assert.assertTrue(parseResult.isError());

		// Null input - wrapper
		parseResult = new ModelBinderUtils.ParseResult();
		result = ModelBinderUtils.parse(null, Integer.class, parseResult, formatter, formatProvider);
		Assert.assertNull(result);
		Assert.assertFalse(parseResult.isError());

		// Invalid input - primitive
		parseResult = new ModelBinderUtils.ParseResult();
		result = ModelBinderUtils.parse("invalid", int.class, parseResult, formatter, formatProvider);
		Assert.assertEquals(result, Integer.valueOf(0));
		Assert.assertTrue(parseResult.isError());

		// Invalid input - wrapper
		parseResult = new ModelBinderUtils.ParseResult();
		result = ModelBinderUtils.parse("invalid", Integer.class, parseResult, formatter, formatProvider);
		Assert.assertNull(result);
		Assert.assertTrue(parseResult.isError());
	}

	@Test
	public void test_parseArray() {
		// Integer
		Formatter formatter = formatterProvider.getFormatter(Integer.class);
		FormatProvider formatProvider = new FormatProviderImpl(language);

		// OK input - primitive
		ModelBinderUtils.ParseResult parseResult = new ModelBinderUtils.ParseResult();
		Object array = ModelBinderUtils.parseArray(new String[] { "100", "2000" }, int.class, parseResult, formatter, formatProvider);
		Assert.assertNotNull(array);
		Assert.assertEquals(Array.get(array, 0), 100);
		Assert.assertEquals(Array.get(array, 1), 2000);
		Assert.assertFalse(parseResult.isError());

		// OK input - wrapper
		parseResult = new ModelBinderUtils.ParseResult();
		array = ModelBinderUtils.parseArray(new String[] { "100", "2,000" }, Integer.class, parseResult, formatter, formatProvider);
		Assert.assertNotNull(array);
		Assert.assertEquals(Array.get(array, 0), Integer.valueOf(100));
		Assert.assertEquals(Array.get(array, 1), Integer.valueOf(2000));
		Assert.assertFalse(parseResult.isError());
	}
}
