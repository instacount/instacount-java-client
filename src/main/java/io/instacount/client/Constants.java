/**
 * Copyright (C) 2016 Instacount Inc. (developers@instacount.io)
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

/**
 * Constants used by the Instacount client to communicated with the Instacount API.
 *
 * @see "https://instacount.readme.io"
 */
public final class Constants
{
	public static final String X_INSTACOUNT_CLIENT_ID = "X-Instacount-Client-Id";
	public static final String USER_AGENT = "User-Agent";

	/**
	 * A class that provides constants for various links used by the client.
	 */
	public static final class Links
	{
		public static final String API_URL = "https://api.instacount.io";
	}

	/**
	 * A class that provides constants for various Auth headers used by the client.
	 */
	public static final class Auth
	{
		public static final String X_INSTACOUNT_APPLICATION_ID = "X-Instacount-Application-Id";

		public static final String X_INSTACOUNT_API_KEY = "X-Instacount-API-Key";
	}

	/**
	 * A class that provides constants for various headers related to API versioning used by the client.
	 */
	public static final class ApiVersions
	{
		public static final String API_VERSION_1 = "application/vnd.instacount.v1+json";
	}
}
