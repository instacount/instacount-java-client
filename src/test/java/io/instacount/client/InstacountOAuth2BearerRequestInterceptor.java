/**
 * Copyright (C) 2015 Instacount Inc. (developers@instacount.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.instacount.client;

import com.google.common.base.Preconditions;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * An implementation of {@link RequestInterceptor} for supplying a Google Accounts OAuth2 bearer token.
 *
 * @see ""
 */
interface InstacountOAuth2BearerRequestInterceptor extends RequestInterceptor
{
	String GOOGLE_OAUTH2_SCOPE = "https://www.googleapis.com/auth/userinfo.email";

	/**
	 * A default implementation of {@link InstacountOAuth2BearerRequestInterceptor}.
	 */
	class Impl implements InstacountOAuth2BearerRequestInterceptor
	{
		private final String accessToken;

		/**
		 * Required-args Constructor.
		 * 
		 * @param accessToken
		 */
		public Impl(final String accessToken)
		{
			this.accessToken = Preconditions.checkNotNull(accessToken);
		}

		@Override
		public void apply(final RequestTemplate requestTemplate)
		{
			if (accessToken != null && accessToken.length() > 0)
			{
				requestTemplate.header("Authorization", "Bearer " + accessToken);
			}
		}
	}
}
