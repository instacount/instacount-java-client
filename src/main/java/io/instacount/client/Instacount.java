package io.instacount.client;

import feign.Feign;
import feign.Logger.Level;
import feign.Param;
import feign.RequestLine;
import feign.jackson.JacksonDecoder;

/**
 * A feign client for interacting with the Instacount API.
 *
 * @see "https://www.instacount.io"
 * @see "https://instacount.readme.io/docs/api-clients"
 */
public interface Instacount
{
	String INSTACOUNT_V1_MEDIA_TYPE = "application/vnd.instacount.v1+json";

	// TODO: Create with name, etc, increment with payloads, async, etc.
	// TODO: Application Access?
	// TODO: Counter Operations.

	@RequestLine("POST /sharded_counters")
	void createCounter();

	@RequestLine("POST /sharded_counters/{counterName}/increments")
	void increment(@Param("counterName") String counterName);

	@RequestLine("POST /sharded_counters/{counterName}/decrements")
	void decrement(@Param("counterName") String counterName);

	/**
	 * A class for building instances of {@link Instacount}.
	 */
	class Builder
	{
		/**
		 * A default builder for creating an instacount client that will interact with the Instacount API using supplied
		 * credentials.
		 * 
		 * @param authRequestInterceptor An instance of {@link InstacountAuthRequestInterceptor} for providing custom
		 *            authentication credentials.
		 * @return
		 */
		public static Instacount build(final InstacountAuthRequestInterceptor authRequestInterceptor)
		{
			Instacount instacount = Feign.builder()
				// .client(new OkHttpClient())
				.logLevel(Level.FULL).decoder(new JacksonDecoder()).requestInterceptor(authRequestInterceptor)
				.target(Instacount.class, "https://api.instacount.io");

			return instacount;
		}
	}

}
