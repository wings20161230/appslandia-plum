package com.appslandia.plum.web;

import org.junit.Assert;
import org.junit.Test;

public class HstsBuilderTest {

	@Test
	public void test_includeSubDomains() {
		HstsBuilder hsts = new HstsBuilder();

		hsts.includeSubDomains(true);
		Assert.assertTrue(hsts.toString().contains("includeSubDomains"));
	}

	@Test
	public void test_maxAge() {
		HstsBuilder hsts = new HstsBuilder();

		hsts.maxAge(100_000);
		Assert.assertTrue(hsts.toString().contains("max-age=100000"));
	}

	@Test
	public void test_preload() {
		HstsBuilder hsts = new HstsBuilder();

		hsts.preload();
		Assert.assertTrue(hsts.toString().contains("preload"));
	}

	@Test
	public void test_all() {
		HstsBuilder hsts = new HstsBuilder();

		hsts.maxAge(100000).includeSubDomains(true).preload();
		Assert.assertTrue(hsts.toString().contains("max-age=100000; includeSubDomains; preload"));
	}
}
