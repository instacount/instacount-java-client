package io.instacount.client;

/**
 * Headers supported by the Instacount API.
 */
public class Headers
{
	public static final String X_INSTACOUNT_CLIENT_ID = "X-Instacount-Client-Id";

	/**
	 * Headers for API Authn/z
	 */
	final class Auth
	{
		public static final String X_INSTACOUNT_APPLICATION_ID = "X-Instacount-Application-Id";

		public static final String X_INSTACOUNT_API_KEY = "X-Instacount-API-Key";

		public static final String X_MASHAPE_KEY = "X-Mashape-Key";
	}

	/**
	 * Headers for API Versioning
	 */
	final class ApiVersions
	{
		public static final String API_VERSION_1 = "application/vnd.instacount.v1+json";
	}
}
