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

package com.appslandia.plum.web;

import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.appslandia.common.base.ParameterMap;
import com.appslandia.common.utils.AssertUtils;
import com.appslandia.plum.base.ActionContext;
import com.appslandia.plum.base.ActionParser;
import com.appslandia.plum.base.ActionResult;
import com.appslandia.plum.base.GET;
import com.appslandia.plum.base.NotFoundException;
import com.appslandia.plum.base.POST;
import com.appslandia.plum.base.PathParams;
import com.appslandia.plum.base.RequestParam;
import com.appslandia.plum.base.Result;
import com.appslandia.plum.base.ServletUtils;
import com.appslandia.plum.captcha.CaptchaProducer;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public abstract class CaptchaController {

	public abstract CaptchaManager getCaptchaManager();

	public abstract CaptchaProducer getCaptchaProducer();

	public abstract ActionParser getActionParser();

	@GET
	@PathParams("/{captchaId}")
	public ActionResult index(@RequestParam("captchaId") String captchaId, HttpServletRequest request, ActionContext actionContext) throws Exception {
		String captchaWords = this.getCaptchaManager().getCaptchaWords(request, captchaId);
		if (captchaWords == null) {
			throw new NotFoundException(actionContext.getMessage(ServletUtils.ERROR_MSGKEY_NOT_FOUND, request.getRequestURI()));
		}

		// BufferedImage
		final BufferedImage img = this.getCaptchaProducer().produce(captchaWords);

		return new ActionResult() {
			@Override
			public void execute(HttpServletRequest request, HttpServletResponse response, ActionContext actionContext) throws Exception {
				response.setContentType("image/png");

				try (OutputStream os = response.getOutputStream()) {
					ImageIO.write(img, "png", os);
				}
			}
		};
	}

	@GET
	@POST
	public Result reload(HttpServletRequest request, HttpServletResponse response, ActionContext actionContext) throws Exception {
		getCaptchaManager().initCaptcha(request);
		String captchaId = (String) request.getAttribute(ServletUtils.REQUEST_ATTRIBUTE_CAPTCHA_ID);
		AssertUtils.assertNotNull(captchaId, "captchaId is required.");

		ParameterMap parameters = new ParameterMap().of("captchaId", captchaId);
		String url = getActionParser().toActionUrl(request, null, actionContext.getController(), "index", parameters);
		url = response.encodeURL(url);

		Map<String, Object> resultMap = new ParameterMap().of("captchaId", captchaId).of("captchaSrc", url);
		return new Result().setData(resultMap);
	}
}
