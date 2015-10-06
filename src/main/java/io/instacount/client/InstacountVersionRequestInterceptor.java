package io.instacount.client;

import feign.RequestInterceptor;
import feign.RequestTemplate;

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
			requestTemplate.header("Accept", Headers.ApiVersions.API_VERSION_1);
			requestTemplate.header("Content-Type", Headers.ApiVersions.API_VERSION_1);
		}
	}
}
