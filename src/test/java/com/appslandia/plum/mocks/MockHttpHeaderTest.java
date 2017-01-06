package com.appslandia.plum.mocks;

import org.junit.Assert;
import org.junit.Test;

public class MockHttpHeaderTest {

	@Test
	public void test_addValues() {
		MockHttpHeader header = new MockHttpHeader();
		header.addValues(1, 2);

		Assert.assertEquals(header.getStringValue(), "1");
		Assert.assertEquals(header.getValue(), 1);

		Assert.assertEquals(header.getStringValues().get(0), "1");
		Assert.assertEquals(header.getStringValues().get(1), "2");

		Assert.assertEquals(header.getValues().get(0), 1);
		Assert.assertEquals(header.getValues().get(1), 2);
	}

	@Test
	public void test_setValues() {
		MockHttpHeader header = new MockHttpHeader();
		header.addValues(1, 2);

		Assert.assertEquals(header.getValues().get(0), 1);
		Assert.assertEquals(header.getValues().get(1), 2);

		header.setValues(3, 4);
		Assert.assertEquals(header.getValues().get(0), 3);
		Assert.assertEquals(header.getValues().get(1), 4);
	}
}
