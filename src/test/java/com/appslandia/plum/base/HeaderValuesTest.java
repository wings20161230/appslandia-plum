package com.appslandia.plum.base;

import org.junit.Assert;
import org.junit.Test;

public class HeaderValuesTest {

	@Test
	public void test() {
		HeaderValues hv = new HeaderValues();

		hv.add("key1");
		hv.add("key2", "value2");
		hv.add("key3", "value3");
		hv.add("key4");

		Assert.assertEquals(hv.toString(), "key1, key2=value2, key3=value3, key4");
	}

	@Test
	public void test_customSeparator() {
		HeaderValues hv = new HeaderValues("; ");

		hv.add("key1");
		hv.add("key2", "value2");
		hv.add("key3", "value3");
		hv.add("key4");

		Assert.assertEquals(hv.toString(), "key1; key2=value2; key3=value3; key4");
	}
}
