package com.appslandia.plum.base;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

public class CacheControlBuilderTest {

	@Test
	public void test_noStore() {
		CacheControlBuilder builder = new CacheControlBuilder();

		builder.noStore();
		Assert.assertTrue(builder.toString().contains("no-store"));
	}

	@Test
	public void test_noCache() {
		CacheControlBuilder builder = new CacheControlBuilder();

		builder.noCache();
		Assert.assertTrue(builder.toString().contains("no-cache"));
	}

	@Test
	public void test_mustRevalidate() {
		CacheControlBuilder builder = new CacheControlBuilder();

		builder.mustRevalidate();
		Assert.assertTrue(builder.toString().contains("must-revalidate"));
	}

	@Test
	public void test_proxyRevalidate() {
		CacheControlBuilder builder = new CacheControlBuilder();

		builder.proxyRevalidate();
		Assert.assertTrue(builder.toString().contains("proxy-revalidate"));
	}

	@Test
	public void test_usePublic() {
		CacheControlBuilder builder = new CacheControlBuilder();

		builder.usePublic();
		Assert.assertTrue(builder.toString().contains("public"));
	}

	@Test
	public void test_usePrivate() {
		CacheControlBuilder builder = new CacheControlBuilder();

		builder.usePrivate();
		Assert.assertTrue(builder.toString().contains("private"));
	}

	@Test
	public void test_noTransform() {
		CacheControlBuilder builder = new CacheControlBuilder();

		builder.noTransform();
		Assert.assertTrue(builder.toString().contains("no-transform"));
	}

	@Test
	public void test_maxAge() {
		CacheControlBuilder builder = new CacheControlBuilder();

		builder.maxAge(10000, TimeUnit.SECONDS);
		Assert.assertTrue(builder.toString().contains("max-age=10000"));
	}

	@Test
	public void test_mixed() {
		CacheControlBuilder builder = new CacheControlBuilder();

		builder.maxAge(10000, TimeUnit.SECONDS).usePrivate();
		Assert.assertTrue(builder.toString().contains("max-age=10000, private"));
	}

	@Test
	public void test_disabledCache() {
		CacheControlBuilder builder = new CacheControlBuilder();

		builder.maxAge(0, TimeUnit.SECONDS).noCache().noStore().mustRevalidate();
		Assert.assertTrue(builder.toString().contains("max-age=0, no-cache, no-store, must-revalidate"));
	}
}
