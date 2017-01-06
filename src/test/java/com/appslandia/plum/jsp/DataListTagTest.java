package com.appslandia.plum.jsp;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.appslandia.plum.mocks.MockHttpServletRequest;
import com.appslandia.plum.mocks.MockHttpServletResponse;
import com.appslandia.plum.mocks.MockJspContext;
import com.appslandia.plum.mocks.MockWebContext;

public class DataListTagTest {

	MockWebContext mockWebContext;
	MockHttpServletRequest request;
	MockHttpServletResponse response;

	DataListTag tag;

	@Before
	public void initialize() {
		mockWebContext = new MockWebContext();

		request = mockWebContext.createMockHttpServletRequest();
		response = mockWebContext.createMockHttpServletResponse();

		tag = new DataListTag();
		tag.setJspContext(new MockJspContext(request, response));
	}

	@Test
	public void test() {
		List<Object> options = new ArrayList<>();

		options.add("admin");
		options.add("manager");
		options.add(1);
		options.add(2);
		options.add(null);

		tag.setId("testDataList");
		tag.setOptions(options);

		try {
			tag.doTag();
			String html = tag.getPageContext().getOut().toString();

			Assert.assertTrue(html.contains("<datalist"));
			Assert.assertTrue(html.contains("id=\"testDataList\""));

			Assert.assertTrue(html.contains("value=\"admin\""));
			Assert.assertTrue(html.contains("value=\"manager\""));

			Assert.assertTrue(html.contains("value=\"1\""));
			Assert.assertTrue(html.contains("value=\"null\""));
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}
}
