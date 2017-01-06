// The MIT License (MIT)
// Copyright © 2015 AppsLandia. All rights reserved.

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

package com.appslandia.plum.jsp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.appslandia.common.base.CaseInsensitiveMap;
import com.appslandia.common.base.CaseInsensitiveSet;
import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.FileUtils;
import com.appslandia.common.utils.StringUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class JspSourceProcessor {
	public static final String JSP_PATH = "/WEB-INF/jsp";

	public JspSourceProcessor(String appPath) throws IOException {
		AssertUtils.assertNotNull(appPath);
		process(appPath, false);
	}

	public JspSourceProcessor(String appPath, boolean minimize) throws IOException {
		AssertUtils.assertNotNull(appPath);
		process(appPath, minimize);
	}

	void process(String appPath, boolean minimize) throws IOException {
		Path jspAbsPath = Paths.get(appPath, JSP_PATH);
		File jspDir = jspAbsPath.toFile();
		AssertUtils.assertTrue(jspDir.exists(), "/WEB-INF/jsp not found.");

		Path sharedPath = jspAbsPath.resolve("shared");
		Path viewsPath = jspAbsPath.resolve("views");
		Path generatedPath = jspAbsPath.resolve("generated");

		Queue<File> queue = new LinkedList<>();
		queue.add(viewsPath.toFile());

		while (queue.isEmpty() == false) {
			File file = queue.remove();

			if (file.isDirectory()) {
				for (File sub : file.listFiles()) {
					queue.add(sub);
				}
				continue;
			}
			if (file.isFile() == false) {
				continue;
			}

			Path targetFilePath = generatedPath.resolve(viewsPath.relativize(file.toPath()));
			Files.createDirectories(targetFilePath.getParent());

			// JSP?
			if (file.getName().toLowerCase(Locale.ENGLISH).endsWith(".jsp")) {

				// ParseBodyModel
				ParseBodyModel model = new ParseBodyModel();
				List<String> bodySource = Files.readAllLines(file.toPath());
				parseVariables(bodySource, model);
				parseSectionSources(file.getName(), bodySource, model);

				// layoutSource
				String layoutJspName = model.getLayoutName() + ".jsp";
				List<String> layoutSource = Files.readAllLines(sharedPath.resolve(layoutJspName));

				// processLayoutSource
				processLayoutSource(layoutJspName, layoutSource, file.getName(), model);

				// Other Processes
				if (minimize) {
					removeBlankLines(bodySource);
					removeBlankLines(layoutSource);
				}
				processVariables(bodySource, model.variables);
				processVariables(layoutSource, model.variables);

				// Save Sources
				Path targetIncludePath = targetFilePath.getParent().resolve(getIncludeBodyJspName(file.getName()));
				try (BufferedWriter out = Files.newBufferedWriter(targetIncludePath, StandardCharsets.UTF_8)) {
					saveSource(bodySource, out);
				}
				try (BufferedWriter out = Files.newBufferedWriter(targetFilePath, StandardCharsets.UTF_8)) {
					saveSource(layoutSource, out);
				}

			} else {

				// Not JSP -> Just copy
				Files.copy(file.toPath(), targetFilePath);
			}
		}
	}

	// <!-- @doBody -->
	// <!-- @someSection? -->

	final Pattern bodySectionHolderPattern = Pattern.compile("(\\s)*<!--(\\s)*@doBody(\\s)*-->", Pattern.CASE_INSENSITIVE);
	final Pattern otherSectionHolderPattern = Pattern.compile("(\\s)*<!--(\\s)*@[^\\s]+(\\?)?(\\s)*-->", Pattern.CASE_INSENSITIVE);

	void processLayoutSource(String layoutJspName, List<String> layoutSource, String bodyJspName, ParseBodyModel parseBodyModel) {
		// doBody
		boolean doBody = false;
		while (true) {
			int pos = -1;
			while ((++pos < layoutSource.size()) && (bodySectionHolderPattern.matcher(layoutSource.get(pos)).matches() == false)) {
			}
			if (pos == layoutSource.size()) {
				break;
			}
			if (doBody == true) {
				throw new IllegalArgumentException("@doBody duplicated.");
			}

			String bodyLine = layoutSource.get(pos);
			String indents = copyIndents(bodyLine);

			layoutSource.set(pos, indents + "<!-- @doBody begin -->");
			layoutSource.add(pos + 1, indents + "<!-- @doBody end -->");
			layoutSource.add(pos + 1, indents + "<%@ include file=\"" + getIncludeBodyJspName(bodyJspName) + "\" %>");

			doBody = true;
		}

		if (doBody == false) {
			throw new IllegalArgumentException("no @doBody directive found.");
		}

		// Sections
		Set<String> sectionsInLayout = new CaseInsensitiveSet();
		while (true) {
			int pos = -1;
			while ((++pos < layoutSource.size()) && (otherSectionHolderPattern.matcher(layoutSource.get(pos)).matches() == false)) {
			}
			if (pos == layoutSource.size()) {
				break;
			}

			String sectionHolderLine = layoutSource.get(pos);
			String sectionName = sectionHolderLine.substring(sectionHolderLine.indexOf("@") + 1, sectionHolderLine.indexOf("-->")).trim();

			boolean required = true;
			if (sectionName.endsWith("?")) {
				sectionName = sectionName.substring(0, sectionName.length() - 1);
				required = false;
			}

			sectionsInLayout.add(sectionName);

			List<String> sectionSource = parseBodyModel.sectionSources.get(sectionName);
			String indents = copyIndents(sectionHolderLine);

			if (sectionSource != null) {
				layoutSource.remove(pos);
				layoutSource.addAll(pos, sectionSource);
			} else {
				if (required) {
					throw new IllegalArgumentException("section @" + sectionName + " is required in " + bodyJspName);
				} else {
					layoutSource.set(pos, indents + "<!-- @" + sectionName + "? undefined -->");
				}
			}
		}

		for (String sectionInBody : parseBodyModel.sectionSources.keySet()) {
			if (sectionsInLayout.contains(sectionInBody) == false) {
				throw new IllegalArgumentException("section @" + sectionInBody + " undefined in " + layoutJspName);
			}
		}
	}

	// <!-- @variables
	// title=some_expression
	// -->

	final Pattern variablesStartPattern = Pattern.compile("(\\s)*<!--(\\s)*@variables(\\s)*", Pattern.CASE_INSENSITIVE);
	final Pattern variablesEndPattern = Pattern.compile("(\\s)*-->(\\s)*");
	final Pattern variableLinePattern = Pattern.compile("[^\\s=]+(\\s)*=.*", Pattern.CASE_INSENSITIVE);

	void parseVariables(List<String> bodySource, ParseBodyModel model) {
		while (true) {
			int start = -1;
			while ((++start < bodySource.size()) && (variablesStartPattern.matcher(bodySource.get(start)).matches() == false)) {
			}

			if (start == bodySource.size()) {
				break;
			}

			int end = start;
			while ((++end < bodySource.size()) && (variablesEndPattern.matcher(bodySource.get(end)).matches() == false)) {
			}

			String variablesLine = bodySource.get(start);

			if (end == bodySource.size()) {
				throw new IllegalArgumentException("no ending tag found (start=" + variablesLine + ")");
			}

			// variables: start-end
			for (int i = start + 1; i < end; i++) {
				String line = bodySource.get(i).trim();
				if ((line.isEmpty()) || line.startsWith("//")) {
					continue;
				}
				if (variableLinePattern.matcher(line).matches() == false) {
					throw new IllegalArgumentException(line);
				}
				int eqIdx = line.indexOf('=');
				model.variables.put(line.substring(0, eqIdx).trim(), StringUtils.trimToEmpty(line.substring(eqIdx + 1)));
			}

			removeSubSource(bodySource, start, end);
			bodySource.add(start, copyIndents(variablesLine) + "<!-- @variables removed -->");
		}
	}

	// <!-- @someSection begin -->
	// HTML/JSP
	// <!-- @someSection end -->

	final Pattern sectionStartPattern = Pattern.compile("(\\s)*<!--(\\s)*@[^\\s]+(\\s)+begin(\\s)*-->", Pattern.CASE_INSENSITIVE);
	final Pattern sectionEndPattern = Pattern.compile("(\\s)*<!--(\\s)*@[^\\s]+(\\s)+end(\\s)*-->", Pattern.CASE_INSENSITIVE);

	void parseSectionSources(String bodyJspName, List<String> bodySource, ParseBodyModel model) {
		while (true) {
			int start = -1;
			while ((++start < bodySource.size()) && (sectionStartPattern.matcher(bodySource.get(start)).matches() == false)) {
			}

			if (start == bodySource.size()) {
				break;
			}

			int end = start;
			while ((++end < bodySource.size()) && (sectionEndPattern.matcher(bodySource.get(end)).matches() == false)) {
			}

			String startLine = bodySource.get(start);

			if (end == bodySource.size()) {
				throw new IllegalArgumentException("no ending tag found (start=" + startLine + ")");
			}

			// Section
			int idxAt = startLine.indexOf("@");
			String sectionName = startLine.substring(idxAt + 1, startLine.indexOf(' ', idxAt)).trim();
			if (model.sectionSources.containsKey(sectionName)) {
				throw new IllegalArgumentException("section @" + sectionName + " duplicated in " + bodyJspName);
			}
			model.sectionSources.put(sectionName, copySubSource(bodySource, start, end));

			String sectionLine = bodySource.get(start);
			removeSubSource(bodySource, start, end);
			bodySource.add(start, copyIndents(sectionLine) + "<!-- @" + sectionName + " removed -->");
		}
	}

	void processVariables(List<String> source, Map<String, String> variables) {
		for (int i = 0; i < source.size(); i++) {
			String line = source.get(i);
			for (Entry<String, String> entry : variables.entrySet()) {
				// @{variable}
				String holder = "@\\{\\s*" + entry.getKey() + "\\s*\\}";
				line = Pattern.compile(holder, Pattern.CASE_INSENSITIVE).matcher(line).replaceAll(Matcher.quoteReplacement(entry.getValue()));
			}
			source.set(i, line);
		}
	}

	String copyIndents(String line) {
		int end = -1;
		while ((++end < line.length()) && Character.isWhitespace(line.charAt(end))) {
		}
		return line.substring(0, end);
	}

	List<String> copySubSource(List<String> source, int start, int end) {
		List<String> list = new ArrayList<>();
		for (int i = start; i <= end; i++) {
			list.add(source.get(i));
		}
		return list;
	}

	void removeSubSource(List<String> source, int start, int end) {
		for (int i = end; i >= start; i--) {
			source.remove(i);
		}
	}

	final Pattern blankLinePattern = Pattern.compile("(\\s)*");

	void removeBlankLines(List<String> source) {
		for (int i = source.size() - 1; i >= 0; i--) {
			if (blankLinePattern.matcher(source.get(i)).matches()) {
				source.remove(i);
			}
		}
	}

	void saveSource(List<String> source, BufferedWriter out) throws IOException {
		for (int i = 0; i < source.size(); i++) {
			if (i > 0) {
				out.newLine();
			}
			out.write(source.get(i));
		}
	}

	String getIncludeBodyJspName(String bodyJspName) {
		return FileUtils.buildFileName(bodyJspName, "__");
	}

	static class ParseBodyModel {
		final Map<String, String> variables = new CaseInsensitiveMap<String>();
		final Map<String, List<String>> sectionSources = new CaseInsensitiveMap<>();

		public String getLayoutName() {
			return this.variables.getOrDefault("__layout", "layout");
		}
	}
}
