package com.appslandia.plum.web;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.appslandia.plum.base.ServletUtils;
import com.appslandia.plum.mocks.MockHttpServletRequest;
import com.appslandia.plum.mocks.MockHttpSession;
import com.appslandia.plum.mocks.MockTempDataManager;
import com.appslandia.plum.mocks.MockWebContext;

public class TempDataManagerTest {

	MockWebContext mockWebContext;
	TempDataManager tempDataManager;

	MockHttpSession session;
	MockHttpServletRequest request;

	@Before
	public void initialize() {
		mockWebContext = new MockWebContext();
		tempDataManager = mockWebContext.getObject(TempDataManager.class);

		session = mockWebContext.createMockHttpSession();
		request = mockWebContext.createMockHttpServletRequest(session);
	}

	private void initTempData() throws Exception {
		MockHttpServletRequest request = mockWebContext.createMockHttpServletRequest(session);
		TempData tempData = new TempData().put("key1", "data1");
		request.setAttribute(ServletUtils.REQUEST_ATTRIBUTE_TEMP_DATA, tempData);
		try {
			tempDataManager.saveTempData(request, tempData);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_saveTempData() {
		MockHttpServletRequest request = mockWebContext.createMockHttpServletRequest();
		TempData tempData = new TempData().put("key1", "data1");
		request.setAttribute(ServletUtils.REQUEST_ATTRIBUTE_TEMP_DATA, tempData);
		try {
			String tempDataId = tempDataManager.saveTempData(request, tempData);
			Assert.assertNotNull(tempDataId);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_loadTempData() {
		try {
			initTempData();

			TempData tempDataData = tempDataManager.loadTempData(request, MockTempDataManager.MOCK_TEMP_DATA_ID);

			Assert.assertNotNull(tempDataData);
			Assert.assertNotNull(tempDataData.get("key1"));
			Assert.assertEquals(tempDataData.get("key1"), "data1");

			TempData nullTempData = tempDataManager.loadTempData(request, MockTempDataManager.MOCK_TEMP_DATA_ID);
			Assert.assertNull(nullTempData);

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_loadTempData_noSession() {
		try {
			initTempData();

			MockHttpServletRequest noSessionRequest = mockWebContext.createMockHttpServletRequest();
			TempData tempDataData = tempDataManager.loadTempData(noSessionRequest, MockTempDataManager.MOCK_TEMP_DATA_ID);
			Assert.assertNull(tempDataData);

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}
}
