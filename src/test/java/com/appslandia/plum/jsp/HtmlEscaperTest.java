package com.appslandia.plum.jsp;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.appslandia.plum.mocks.MockJspWriter;

public class HtmlEscaperTest {

	@Test
	public void test_writeEscapedXml() {
		MockJspWriter out = new MockJspWriter();
		try {
			HtmlEscaper.writeEscapedXml("text", out);
			Assert.assertTrue(out.toString().equals("text"));
		} catch (IOException ex) {
			Assert.fail(ex.getMessage());
		}

		out = new MockJspWriter();
		try {
			HtmlEscaper.writeEscapedXml(">", out);
			Assert.assertTrue(out.toString().equals("&gt;"));
		} catch (IOException ex) {
			Assert.fail(ex.getMessage());
		}

		out = new MockJspWriter();
		try {
			HtmlEscaper.writeEscapedXml("<", out);
			Assert.assertTrue(out.toString().equals("&lt;"));
		} catch (IOException ex) {
			Assert.fail(ex.getMessage());
		}

		out = new MockJspWriter();
		try {
			HtmlEscaper.writeEscapedXml("\'", out);
			Assert.assertTrue(out.toString().equals("&apos;"));
		} catch (IOException ex) {
			Assert.fail(ex.getMessage());
		}

		out = new MockJspWriter();
		try {
			HtmlEscaper.writeEscapedXml("\"", out);
			Assert.assertTrue(out.toString().equals("&quot;"));
		} catch (IOException ex) {
			Assert.fail(ex.getMessage());
		}

		out = new MockJspWriter();
		try {
			HtmlEscaper.writeEscapedXml("&", out);
			Assert.assertTrue(out.toString().equals("&amp;"));
		} catch (IOException ex) {
			Assert.fail(ex.getMessage());
		}
	}
}
