/**
 * Copyright (C) 2015 Instacount Inc. (developers@instacount.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package io.instacount.client.interceptors;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.instacount.client.Constants;

/**
 * An implementation of {@link RequestInterceptor} for supplying proper Accept headers for Instacount API requests.
 */
public interface InstacountVersionRequestInterceptor extends RequestInterceptor
{
	/**
	 * A default implementation of {@link InstacountVersionRequestInterceptor}.
	 */
	class Impl implements InstacountVersionRequestInterceptor
	{
		@Override
		public void apply(final RequestTemplate requestTemplate)
		{
			requestTemplate.header("Accept", Constants.ApiVersions.API_VERSION_1);
			requestTemplate.header("Content-Type", Constants.ApiVersions.API_VERSION_1);
		}
	}
}
