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
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.security.SecureRandom;
import java.util.Random;

import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.RandomUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class FishEyeGimpyRenderer extends InitializeObject implements ImageRenderer {

	private Color hColor;
	private Color vColor;

	final Random random = new SecureRandom();

	@Override
	protected void init() throws Exception {
		AssertUtils.assertNotNull(this.hColor, "hColor is required.");
		AssertUtils.assertNotNull(this.vColor, "vColor is required.");
	}

	@Override
	public void render(BufferedImage img, int width, int height) {
		initialize();

		int hStripes = height / 7;
		int vStripes = width / 7;

		int hSpace = height / (hStripes + 1);
		int vSpace = width / (vStripes + 1);

		Graphics2D g = img.createGraphics();

		// Horizontal stripes
		for (int i = hSpace; i < height; i = i + hSpace) {
			g.setColor(this.hColor);
			g.drawLine(0, i, width, i);
		}

		// Vertical stripes
		for (int i = vSpace; i < width; i = i + vSpace) {
			g.setColor(this.vColor);
			g.drawLine(i, 0, i, height);
		}

		// Pixel array
		int pix[] = new int[height * width];
		int j = 0;

		for (int j1 = 0; j1 < width; j1++) {
			for (int k1 = 0; k1 < height; k1++) {
				pix[j] = img.getRGB(j1, k1);
				j++;
			}
		}

		int distance = RandomUtils.nextInt(this.random, width / 4, width / 3);
		int wMid = width / 2;
		int hMid = height / 2;

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {

				int relX = x - wMid;
				int relY = y - hMid;

				double d1 = Math.sqrt(relX * relX + relY * relY);
				if (d1 < distance) {

					int j2 = wMid + (int) (((fishEyeFormula(d1 / distance) * distance) / d1) * (x - wMid));
					int k2 = hMid + (int) (((fishEyeFormula(d1 / distance) * distance) / d1) * (y - hMid));
					img.setRGB(x, y, pix[j2 * height + k2]);
				}
			}
		}

		g.dispose();
	}

	public FishEyeGimpyRenderer setHColor(Color hColor) {
		assertUninitialized();
		this.hColor = hColor;
		return this;
	}

	public FishEyeGimpyRenderer setVColor(Color vColor) {
		assertUninitialized();
		this.vColor = vColor;
		return this;
	}

	// -0.75*s^3 + 1.5*s^2 + 0.25*s
	private double fishEyeFormula(double s) {
		if (s < 0.0) {
			return 0.0;
		}
		if (s > 1.0) {
			return s;
		}
		return -0.75 * s * s * s + 1.5 * s * s + 0.25 * s;
	}
}
