package com.appslandia.plum.web;

import org.junit.Assert;
import org.junit.Test;

public class HpkpBuilderTest {

	@Test
	public void test_includeSubDomains() {
		HpkpBuilder hpkp = new HpkpBuilder();

		hpkp.includeSubDomains(true);
		Assert.assertTrue(hpkp.toString().contains("includeSubDomains"));
	}

	@Test
	public void test_maxAge() {
		HpkpBuilder hpkp = new HpkpBuilder();

		hpkp.maxAge(100_000);
		Assert.assertTrue(hpkp.toString().contains("max-age=100000"));
	}

	@Test
	public void test_pinSha256() {
		HpkpBuilder hpkp = new HpkpBuilder();

		hpkp.pinSha256("based64");
		Assert.assertTrue(hpkp.toString().contains("pin-sha256=\"based64\""));
	}

	@Test
	public void test_reportUri() {
		HpkpBuilder hpkp = new HpkpBuilder();

		hpkp.reportUri("report-uri");
		Assert.assertTrue(hpkp.toString().contains("report-uri=\"report-uri\""));
	}

	@Test
	public void test_all() {
		HpkpBuilder hpkp = new HpkpBuilder();

		hpkp.pinSha256("based64").maxAge(100000).includeSubDomains(true).reportUri("report-uri");
		Assert.assertTrue(hpkp.toString().contains("pin-sha256=\"based64\"; max-age=100000; includeSubDomains; report-uri=\"report-uri\""));
	}
}
