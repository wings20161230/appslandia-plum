package com.appslandia.plum.base;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.appslandia.common.base.MemoryStream;
import com.appslandia.common.utils.FileUtils;
import com.appslandia.plum.mocks.MockHttpServletResponse;
import com.appslandia.plum.mocks.MockServletContext;
import com.appslandia.plum.mocks.MockSessionCookieConfig;

public class MemoryResponseWrapperTest {

	MockServletContext servletContext;
	MockHttpServletResponse response;

	@Before
	public void initialize() {
		servletContext = new MockServletContext(new MockSessionCookieConfig());
		response = new MockHttpServletResponse(servletContext);
	}

	@Test
	public void test() {
		MemoryResponseWrapper wrapper = new MemoryResponseWrapper(response, new MemoryStream(), false);
		try {
			wrapper.getOutputStream().write("data".getBytes(StandardCharsets.UTF_8));
			wrapper.finishWrapper();

			Assert.assertEquals(wrapper.getContent().toString(StandardCharsets.UTF_8.name()), "data");
		} catch (IOException ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_gzip() {
		MemoryResponseWrapper wrapper = new MemoryResponseWrapper(response, new MemoryStream(), true);
		try {
			wrapper.getOutputStream().write("data".getBytes(StandardCharsets.UTF_8));
			wrapper.finishWrapper();

			// wrapper.getContent(): Compressed content
			Assert.assertNotEquals(wrapper.getContent().toString(StandardCharsets.UTF_8.name()), "data");

			ByteArrayOutputStream gzipMem = new ByteArrayOutputStream();
			wrapper.getContent().writeTo(gzipMem);

			ByteArrayOutputStream ungzipMem = new ByteArrayOutputStream();
			FileUtils.copy(new GZIPInputStream(new ByteArrayInputStream(gzipMem.toByteArray())), ungzipMem);

			// ungzipMem
			Assert.assertEquals(ungzipMem.toString(StandardCharsets.UTF_8.name()), "data");
		} catch (IOException ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_resetBuffer() {
		MemoryResponseWrapper wrapper = new MemoryResponseWrapper(response, new MemoryStream(), false);
		try {
			wrapper.getOutputStream().write("data".getBytes(StandardCharsets.UTF_8));
			wrapper.resetBuffer();
			Assert.assertEquals(wrapper.getContent().size(), 0);

		} catch (IOException ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_sendError() {
		MemoryResponseWrapper wrapper = new MemoryResponseWrapper(response, new MemoryStream(), false);
		try {
			wrapper.getOutputStream().write("data".getBytes(StandardCharsets.UTF_8));
			wrapper.sendError(500);

			Assert.assertEquals(wrapper.getContent().size(), 0);
			Assert.assertEquals(wrapper.getStatus(), 500);
			Assert.assertFalse(wrapper.isCommitted());

		} catch (IOException ex) {
			Assert.fail(ex.getMessage());
		}
	}
}
