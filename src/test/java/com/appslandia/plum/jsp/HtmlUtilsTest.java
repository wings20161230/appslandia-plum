package com.appslandia.plum.jsp;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.appslandia.plum.mocks.MockJspWriter;

public class HtmlUtilsTest {

	@Test
	public void test_attribute() {
		MockJspWriter out = new MockJspWriter();
		try {
			HtmlUtils.attribute(out, "method", "POST");
			Assert.assertTrue(out.toString().contains("method=\"POST\""));
		} catch (IOException ex) {
			Assert.fail(ex.getMessage());
		}

		out = new MockJspWriter();
		try {
			HtmlUtils.attribute(out, "method", "");
			Assert.assertTrue(out.toString().contains("method=\"\""));
		} catch (IOException ex) {
			Assert.fail(ex.getMessage());
		}

		out = new MockJspWriter();
		try {
			HtmlUtils.attribute(out, "method", null);
			Assert.assertTrue(out.toString().isEmpty());
		} catch (IOException ex) {
			Assert.fail(ex.getMessage());
		}
	}

	public void test_escapeValueAttribute() {
		MockJspWriter out = new MockJspWriter();
		try {
			HtmlUtils.escapeValueAttribute(out, "value", "100");
			Assert.assertTrue(out.toString().contains("value=\"100\""));
		} catch (IOException ex) {
			Assert.fail(ex.getMessage());
		}

		out = new MockJspWriter();
		try {
			HtmlUtils.escapeValueAttribute(out, "value", "");
			Assert.assertTrue(out.toString().contains("value=\"\""));
		} catch (IOException ex) {
			Assert.fail(ex.getMessage());
		}

		out = new MockJspWriter();
		try {
			HtmlUtils.escapeValueAttribute(out, "method", null);
			Assert.assertTrue(out.toString().contains("value=\"\""));
		} catch (IOException ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_escapeAttribute() {
		MockJspWriter out = new MockJspWriter();
		try {
			HtmlUtils.escapeAttribute(out, "title", "fake > title");
			Assert.assertTrue(out.toString().contains("title=\"fake &gt; title\""));
		} catch (IOException ex) {
			Assert.fail(ex.getMessage());
		}

		out = new MockJspWriter();
		try {
			HtmlUtils.escapeAttribute(out, "title", "");
			Assert.assertTrue(out.toString().contains("title=\"\""));
		} catch (IOException ex) {
			Assert.fail(ex.getMessage());
		}

		out = new MockJspWriter();
		try {
			HtmlUtils.escapeAttribute(out, "title", null);
			Assert.assertTrue(out.toString().isEmpty());
		} catch (IOException ex) {
			Assert.fail(ex.getMessage());
		}
	}
}
