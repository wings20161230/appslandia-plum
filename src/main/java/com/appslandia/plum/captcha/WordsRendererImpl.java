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

package com.appslandia.plum.captcha;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.appslandia.common.base.InitializeObject;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class WordsRendererImpl extends InitializeObject implements WordsRenderer {

	final List<Color> textColors = new ArrayList<Color>(6);
	final List<Font> textFonts = new ArrayList<Font>(6);

	final Random random = new SecureRandom();

	@Override
	protected void init() throws Exception {
		if (this.textColors.isEmpty()) {
			setTextColor(Color.BLACK);
		}
		if (this.textFonts.isEmpty()) {
			addTextFont("Arial", 36);
			addTextFont("Courier", 36);
		}
	}

	@Override
	public void render(BufferedImage img, int width, int height, String words) {
		initialize();
		Graphics2D g = img.createGraphics();

		// hints
		RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		hints.add(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
		g.setRenderingHints(hints);

		// Baseline
		int xBaseline = (int) (0.05f * width);
		int yBaseline = (int) (0.72f * height);

		char[] drawChars = new char[1];
		char[] srcChars = words.toCharArray();
		for (int i = 0; i < srcChars.length; i++) {
			drawChars[0] = srcChars[i];

			g.setColor(this.textColors.get(this.random.nextInt(this.textColors.size())));
			Font rdFont = this.textFonts.get(this.random.nextInt(this.textFonts.size()));
			g.setFont(rdFont);

			if (i > 0) {
				xBaseline += 2; // gap=2px
			}
			g.drawChars(drawChars, 0, drawChars.length, xBaseline, yBaseline);

			GlyphVector gv = rdFont.createGlyphVector(g.getFontRenderContext(), drawChars);
			xBaseline += (int) gv.getVisualBounds().getWidth();
		}

		g.dispose();
	}

	public WordsRendererImpl setTextColor(Color color) {
		assertUninitialized();
		this.textColors.clear();
		this.textColors.add(color);

		Color brighter = color.brighter();
		this.textColors.add(brighter);
		this.textColors.add(brighter.brighter());
		return this;
	}

	public WordsRendererImpl addTextFont(String name, int size) {
		assertUninitialized();
		this.textFonts.add(new Font(name, Font.BOLD, size));
		return this;
	}

	public WordsRendererImpl addTextFont(Font font) {
		assertUninitialized();
		this.textFonts.add(font);
		return this;
	}
}
