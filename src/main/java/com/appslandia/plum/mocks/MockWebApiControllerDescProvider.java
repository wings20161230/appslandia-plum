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

package com.appslandia.plum.mocks;

import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.appslandia.common.objects.ObjectFactory;
import com.appslandia.plum.base.AppConfig;
import com.appslandia.plum.base.ControllerDescProvider;
import com.appslandia.plum.base.Path;
import com.appslandia.plum.wpi.WebApiVersion;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class MockWebApiControllerDescProvider extends ControllerDescProvider {

	@Inject
	private ObjectFactory factory;

	@Inject
	private AppConfig appConfig;

	@PostConstruct
	protected void initialize() {

		Iterator<ObjectFactory.ObjectDesc> iter = this.factory.getDescIterator();
		while (iter.hasNext()) {
			ObjectFactory.ObjectDesc desc = iter.next();

			// @Path + @WebApiVersion
			if ((desc.getType().getDeclaredAnnotation(Path.class) != null) && (desc.getType().getDeclaredAnnotation(WebApiVersion.class) != null)) {
				parseControllerDesc(desc.getType());
			}
		}
	}

	@Override
	public boolean isEnableHttps() {
		return this.appConfig.isEnableHttps();
	}
}
