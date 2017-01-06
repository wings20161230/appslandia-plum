package com.appslandia.plum.base;

import org.junit.Assert;
import org.junit.Test;

public class RemoteIpFilterTest {

	@Test
	public void test() {
		RemoteIpFilter filter = new RemoteIpFilter();
		try {
			Assert.assertTrue(filter.filter("192.168.10.1"));
			Assert.assertTrue(filter.filter("192.168.11.1"));

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_setAllows() {
		RemoteIpFilter filter = new RemoteIpFilter();
		filter.setAllows("192\\.168\\.10\\.\\d+");

		try {
			Assert.assertTrue(filter.filter("192.168.10.1"));
			Assert.assertFalse(filter.filter("192.168.11.1"));

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_setDenies() {
		RemoteIpFilter filter = new RemoteIpFilter();
		filter.setDenies("192\\.168\\.10\\.\\d+");

		try {
			Assert.assertFalse(filter.filter("192.168.10.1"));
			Assert.assertTrue(filter.filter("192.168.11.1"));

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_all() {
		RemoteIpFilter filter = new RemoteIpFilter();

		filter.setAllows("192\\.168\\.10\\.\\d+");
		filter.setDenies("192\\.168\\.11\\.\\d+");

		try {
			Assert.assertTrue(filter.filter("192.168.10.1"));
			Assert.assertTrue(filter.filter("192.168.10.10"));
			Assert.assertFalse(filter.filter("192.168.11.1"));

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}
}
