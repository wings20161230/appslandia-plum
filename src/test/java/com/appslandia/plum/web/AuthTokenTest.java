package com.appslandia.plum.web;

import org.junit.Assert;
import org.junit.Test;

public class AuthTokenTest {

	@Test
	public void test() {
		AuthToken authToken = new AuthToken();
		authToken.setSeries("series").setToken("token");
		authToken.setExpires(100_000);

		Assert.assertEquals(authToken.getSeries(), "series");
		Assert.assertEquals(authToken.getToken(), "token");

		Assert.assertEquals(authToken.getExpires(), 100_000);
	}

	@Test
	public void test_empty() {
		AuthToken authToken = AuthToken.EMPTY;
		try {
			authToken.setSeries("series");
			Assert.fail();
		} catch (Exception ex) {
		}
	}
}
