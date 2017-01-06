package com.appslandia.plum.jsp;

import org.junit.Assert;
import org.junit.Test;

public class TagUtilsTest {

	@Test
	public void test_parseId() {
		String id = TagUtils.parseId("user.userName");
		Assert.assertEquals(id, "user_userName");

		id = TagUtils.parseId("user.location.address");
		Assert.assertEquals(id, "user_location_address");

		id = TagUtils.parseId("user.addresses[1].location");
		Assert.assertEquals(id, "user_addresses_1__location");
	}
}
