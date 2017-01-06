package com.appslandia.plum.web;

import org.junit.Assert;
import org.junit.Test;

public class ReauthTokenTest {

	@Test
	public void test() {
		ReauthToken reauthToken = new ReauthToken();
		reauthToken.setSeries("series").setToken("token");
		reauthToken.setExpires(100_000);

		Assert.assertEquals(reauthToken.getSeries(), "series");
		Assert.assertEquals(reauthToken.getToken(), "token");

		Assert.assertEquals(reauthToken.getExpires(), 100_000);
	}

	@Test
	public void test_empty() {
		ReauthToken reauthToken = ReauthToken.EMPTY;
		try {
			reauthToken.setSeries("series");
			Assert.fail();
		} catch (Exception ex) {
		}
	}
}
