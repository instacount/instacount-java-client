package io.instacount.client;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * An implementation of {@link RequestInterceptor} for supply proper Instacount Authentication headers.
 */
public interface InstacountAuthRequestInterceptor extends RequestInterceptor
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
	 * Return the Mashape key for the Instacount application this client should operate upon.
	 *
	 * @return
	 *
	 * @see "https://instacount.readme.io/docs/api-identifiers"
	 */
	String getMashapeKey();

	/**
	 * A unique name for the application making this request. This is useful for support requests, and will likely be
	 * mandatory in the future. For example, "cool-co-java-application".
	 * 
	 * @return
	 */
	String getClientIdentifier();

	/**
	 * A default implementation of {@link InstacountAuthRequestInterceptor}.
	 */
	abstract class Impl implements InstacountAuthRequestInterceptor
	{
		private final boolean readOnly;

		/**
		 * Required-args constructor.
		 * 
		 * @param readOnly Set to {@code true} to use the read-only API keys. Set to {@code false} to use the read-write
		 *            API keys.
		 */
		protected Impl(final boolean readOnly)
		{
			this.readOnly = readOnly;
		}

		@Override
		public void apply(final RequestTemplate requestTemplate)
		{
			requestTemplate.header(Headers.Auth.X_INSTACOUNT_APPLICATION_ID, this.getInstacountApplicationId());

			if (readOnly)
			{
				requestTemplate.header(Headers.Auth.X_INSTACOUNT_API_KEY, this.getInstacountReadOnlyApplicationKey());
			}
			else
			{
				requestTemplate.header(Headers.Auth.X_INSTACOUNT_API_KEY, this.getInstacountReadWriteApplicationKey());
			}

			requestTemplate.header(Headers.Auth.X_MASHAPE_KEY, this.getMashapeKey());
			requestTemplate.header(Headers.X_INSTACOUNT_CLIENT_ID, this.getClientIdentifier());
		}
	}
}
