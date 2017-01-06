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

import javax.servlet.http.HttpServletRequest;

import com.appslandia.common.utils.ContentTypes;
import com.appslandia.plum.base.ActionResult;
import com.appslandia.plum.base.GET;
import com.appslandia.plum.base.RequestParam;
import com.appslandia.plum.results.ContentResult;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public abstract class DebugController {

	@GET
	public ActionResult requestAttrs(HttpServletRequest request, @RequestParam("attrName") String attrName, @RequestParam("toStringLevel") int toStringLevel) {
		return new ContentResult(DebugUtils.requestAttrs(request, attrName, toStringLevel), ContentTypes.TEXT_PLAIN_UTF8);
	}

	@GET
	public ActionResult sessionAttrs(HttpServletRequest request, @RequestParam("attrName") String attrName, @RequestParam("toStringLevel") int toStringLevel) {
		return new ContentResult(DebugUtils.sessionAttrs(request, attrName, toStringLevel), ContentTypes.TEXT_PLAIN_UTF8);
	}

	@GET
	public ActionResult contextAttrs(HttpServletRequest request, @RequestParam("attrName") String attrName, @RequestParam("toStringLevel") int toStringLevel) {
		return new ContentResult(DebugUtils.contextAttrs(request, attrName, toStringLevel), ContentTypes.TEXT_PLAIN_UTF8);
	}
}
