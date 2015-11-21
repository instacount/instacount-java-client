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
package io.instacount.client;

import com.google.common.base.Preconditions;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * An implementation of {@link RequestInterceptor} for supplying proper Instacount Authentication headers.
 */
public interface InstacountClientParams extends RequestInterceptor
{
	/**
	 * Return the application identifier for the Instacount application this client should operate upon.
	 * 
	 * @return
	 * 
	 * @see "https://instacount.readme.io/docs/instacount-credentials#application-identifiers"
	 */
	String getInstacountApplicationId();

	/**
	 * Return the application read-only key for the Instacount application this client should operate upon.
	 * 
	 * @return
	 *
	 * @see "https://instacount.readme.io/docs/instacount-credentials#api-keys"
	 */
	String getInstacountReadOnlyApplicationKey();

	/**
	 * Return the application read-write key for the Instacount application this client should operate upon.
	 *
	 * @return
	 *
	 * @see "https://instacount.readme.io/docs/instacount-credentials#api-keys"
	 */
	String getInstacountReadWriteApplicationKey();

	/**
	 * A unique name for the application making this request. This is useful for support requests, and will likely be
	 * mandatory in the future. For example, "cool-co-java-application".
	 * 
	 * @return
	 */
	String getClientIdentifier();

	/**
	 * A default implementation of {@link InstacountClientParams}.
	 */
	abstract class AbstractInstacountClientParams implements InstacountClientParams
	{
		private final boolean readOnly;

		/**
		 * Required-args constructor.
		 * 
		 * @param readOnly Set to {@code true} to use the read-only API keys. Set to {@code false} to use the read-write
		 *            API keys.
		 */
		protected AbstractInstacountClientParams(final boolean readOnly)
		{
			this.readOnly = readOnly;
		}

		@Override
		public void apply(final RequestTemplate requestTemplate)
		{
			final String instacountApplicationId = this.getInstacountApplicationId();

			if (instacountApplicationId != null && instacountApplicationId.length() > 0)
			{
				requestTemplate.header(Constants.Auth.X_INSTACOUNT_APPLICATION_ID, instacountApplicationId);
			}

			if (readOnly)
			{
				final String instacountReadOnlyApplicationKey = this.getInstacountReadOnlyApplicationKey();
				if (instacountReadOnlyApplicationKey != null && instacountReadOnlyApplicationKey.length() > 0)
				{
					requestTemplate.header(Constants.Auth.X_INSTACOUNT_API_KEY, instacountReadOnlyApplicationKey);
				}
			}
			else
			{
				final String instacountReadWriteApplicationKey = this.getInstacountReadWriteApplicationKey();
				if (instacountReadWriteApplicationKey != null && instacountReadWriteApplicationKey.length() > 0)
				{
					requestTemplate.header(Constants.Auth.X_INSTACOUNT_API_KEY, instacountReadWriteApplicationKey);
				}
			}

			Preconditions.checkNotNull(this.getClientIdentifier(),
				"You must specify a client identifier in order to make calls agains the Instacount API!");
			requestTemplate.header(Constants.X_INSTACOUNT_CLIENT_ID, this.getClientIdentifier());
		}
	}
}
