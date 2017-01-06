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

import javax.servlet.ServletContext;
import javax.validation.Validation;
import javax.validation.Validator;

import com.appslandia.common.objects.ObjectException;
import com.appslandia.common.objects.ObjectFactory;
import com.appslandia.common.objects.ObjectProducer;
import com.appslandia.plum.base.FormatterProviderImpl;
import com.appslandia.plum.base.GsonProcessorImpl;
import com.appslandia.plum.base.RemoteIpFilter;
import com.appslandia.plum.base.RequestExecutor;
import com.appslandia.plum.base.RequestInitializer;
import com.appslandia.plum.web.ConstDescProvider;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class MockWebContext {

	final MockServletContext mockServletContext;
	final MockServletConfig mockServletConfig;

	private volatile ObjectFactory objectFactory;
	private volatile RequestInitializer requestInitializer;
	private volatile RequestExecutor requestExecutor;

	final Object mutex = new Object();
	private boolean webApiVersion;

	public MockWebContext() {
		this.mockServletContext = new MockServletContext(new MockSessionCookieConfig());
		this.mockServletConfig = new MockServletConfig(this.mockServletContext);
	}

	public MockWebContext useWebApiVersion() {
		this.webApiVersion = true;
		return this;
	}

	public MockServletContext getMockServletContext() {
		return this.mockServletContext;
	}

	public ObjectFactory getObjectFactory() {
		ObjectFactory obj = this.objectFactory;
		if (obj == null) {
			synchronized (this.mutex) {
				if ((obj = this.objectFactory) == null) {
					if (this.webApiVersion == false) {
						this.objectFactory = obj = createObjectFactory(this.mockServletContext);
					} else {
						this.objectFactory = obj = createObjectFactory_webApi(this.mockServletContext);
					}
				}
			}
		}
		return obj;
	}

	public RequestInitializer getRequestInitializer() {
		RequestInitializer obj = this.requestInitializer;
		if (obj == null) {
			synchronized (this.mutex) {
				if ((obj = this.requestInitializer) == null) {
					if (this.webApiVersion == false) {
						obj = new MockWebRequestInitializer();
					} else {
						obj = new MockWebApiRequestInitializer();
					}
					this.getObjectFactory().inject(obj).postConstruct(obj);
					this.requestInitializer = obj;
				}
			}
		}
		return obj;
	}

	public RequestExecutor getRequestExecutor() {
		RequestExecutor obj = this.requestExecutor;
		if (obj == null) {
			synchronized (this.mutex) {
				if ((obj = this.requestExecutor) == null) {
					if (this.webApiVersion == false) {
						obj = new MockWebRequestExecutor().setServletConfig(this.mockServletConfig);
					} else {
						obj = new MockWebApiRequestExecutor().setServletConfig(this.mockServletConfig);
					}
					this.getObjectFactory().inject(obj).postConstruct(obj);
					this.requestExecutor = obj;
				}
			}
		}
		return obj;
	}

	public MockHttpServletRequest createMockHttpServletRequest() {
		MockHttpServletRequest request = new MockHttpServletRequest(this.mockServletContext);
		request.setMockUserInfoManager(this.getObjectFactory().getObject(MockUserInfoManager.class));
		return request;
	}

	public MockHttpServletRequest createMockHttpServletRequest(MockHttpSession session) {
		MockHttpServletRequest request = new MockHttpServletRequest(this.mockServletContext, session);
		request.setMockUserInfoManager(this.getObjectFactory().getObject(MockUserInfoManager.class));
		return request;
	}

	public MockHttpServletResponse createMockHttpServletResponse() {
		return new MockHttpServletResponse(this.mockServletContext);
	}

	public MockHttpSession createMockHttpSession() {
		return new MockHttpSession(this.mockServletContext);
	}

	public void execute(MockHttpServletRequest request, MockHttpServletResponse response) throws Exception {
		MockFilterChain chain = new MockFilterChain();
		chain.addFilter(getRequestInitializer());
		chain.setServlet(getRequestExecutor());
		chain.doFilter(request, response);
	}

	public <T> T getObject(Class<?> type) {
		return this.getObjectFactory().getObject(type);
	}

	public ObjectFactory register(Class<?> objectClass) {
		return this.getObjectFactory().register(objectClass);
	}

	protected ObjectFactory createObjectFactory(final ServletContext sc) {
		ObjectFactory factory = new ObjectFactory();

		factory.register(MockAppConfig.class);
		factory.register(MockLanguageProvider.class);
		factory.register(MockMessageBundleProvider.class);

		factory.register(ConstDescProvider.class);
		factory.register(FormatterProviderImpl.class);
		factory.register(GsonProcessorImpl.class);
		factory.register(MockRequestErrorLogger.class);

		factory.register(RemoteIpFilter.class);
		factory.register(MockRemoteClientFilter.class);
		factory.register(MockHeaderPolicyProvider.class);
		factory.register(MockCorsPolicyProvider.class);
		factory.register(MockCorsPolicyHandler.class);

		factory.register(MockUserInfoManager.class);
		factory.register(MockUserAuthManager.class);
		factory.register(MockAuthTokenHandler.class);
		factory.register(MockJ2eeBasedAuthenticator.class);

		factory.register(MockUserReauthManager.class);
		factory.register(MockReauthTokenHandler.class);
		factory.register(MockReauthTokenReauthenticator.class);
		factory.register(MockTokenTheftHandler.class);

		factory.register(MockCsrfTokenManager.class);
		factory.register(MockCaptchaManager.class);
		factory.register(MockCaptchaProducer.class);
		factory.register(MockPrefCookieHandler.class);
		factory.register(MockTempDataManager.class);

		factory.register(MockModelBinder.class);
		factory.register(MockActionParser.class);
		factory.register(MockActionInvoker.class);
		factory.register(MockControllerDescProvider.class);
		factory.register(MockControllerProvider.class);

		factory.register(MockSessionConfig.class);

		factory.register(Validator.class, new ObjectProducer<Validator>() {
			@Override
			public Validator produce(ObjectFactory factory) throws ObjectException {
				return Validation.buildDefaultValidatorFactory().getValidator();
			}
		});

		factory.register(ServletContext.class, new ObjectProducer<ServletContext>() {
			@Override
			public ServletContext produce(ObjectFactory factory) throws ObjectException {
				return sc;
			}
		});

		return factory;
	}

	protected ObjectFactory createObjectFactory_webApi(final ServletContext sc) {
		ObjectFactory factory = new ObjectFactory();

		factory.register(MockAppConfig.class);
		factory.register(MockLanguageProvider.class);
		factory.register(MockMessageBundleProvider.class);

		factory.register(FormatterProviderImpl.class);
		factory.register(GsonProcessorImpl.class);
		factory.register(MockRequestErrorLogger.class);

		factory.register(RemoteIpFilter.class);
		factory.register(MockRemoteClientFilter.class);
		factory.register(MockHeaderPolicyProvider.class);
		factory.register(MockCorsPolicyProvider.class);
		factory.register(MockCorsPolicyHandler.class);

		factory.register(MockUserInfoManager.class);
		factory.register(MockWebApiAuthenticator.class);

		factory.register(MockModelBinder.class);
		factory.register(MockWebApiActionParser.class);
		factory.register(MockActionInvoker.class);
		factory.register(MockWebApiControllerDescProvider.class);
		factory.register(MockControllerProvider.class);

		factory.register(Validator.class, new ObjectProducer<Validator>() {
			@Override
			public Validator produce(ObjectFactory factory) throws ObjectException {
				return Validation.buildDefaultValidatorFactory().getValidator();
			}
		});

		factory.register(ServletContext.class, new ObjectProducer<ServletContext>() {
			@Override
			public ServletContext produce(ObjectFactory factory) throws ObjectException {
				return sc;
			}
		});
		return factory;
	}
}
