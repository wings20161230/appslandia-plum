package com.appslandia.plum.web;

import org.junit.Assert;
import org.junit.Test;

import com.appslandia.common.base.StandardConsts;

public class ConstDescProviderTest {

	@Test
	public void test() {
		ConstDescProvider constDescProvider = new ConstDescProvider();
		try {
			constDescProvider.parseDescKeys(StandardConsts.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		Assert.assertEquals("activeConsts.active", constDescProvider.getDescKey("activeConsts", StandardConsts.ACTIVE));
		Assert.assertEquals("activeConsts.inactive", constDescProvider.getDescKey("activeConsts", StandardConsts.INACTIVE));
	}
}
