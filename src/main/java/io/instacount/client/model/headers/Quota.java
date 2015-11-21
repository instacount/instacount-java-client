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
package io.instacount.client.model.headers;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * A class for accessing quota information gleaned from headers in the Instacount API response.
 */
@Getter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class Quota
{
	public static final String X_RATELIMIT_ACCESS_COUNTERS_LIMIT = "X-Ratelimit-Access-Counters-Limit";
	public static final String X_RATELIMIT_ACCESS_COUNTERS_REMAINING = "X-Ratelimit-Access-Counters-Remaining";
	public static final String X_RATELIMIT_MUTATE_COUNTERS_LIMIT = "X-Ratelimit-Mutate-Counters-Limit";
	public static final String X_RATELIMIT_MUTATE_COUNTERS_REMAINING = "X-Ratelimit-Mutate-Counters-Limit";

	/**
	 * The number of resource access requests allowed for the current subscription. Access requests types include GET,
	 * OPTION, HEAD, and TRACE.
	 */
	@NonNull
	private final Long numAccessRequestsLimit;

	/**
	 * The number of resource access requests remaining/available in the current subscription's quota. This number is
	 * the numAccessRequestsLimit minus the total number of requests that have been made during the current billing
	 * period. Access requests types include GET, OPTION, HEAD, and TRACE.
	 */
	@NonNull
	private final Long numAccessRequestsRemaining;

	/**
	 * The number of resource mutation requests allowed for the current subscription. Access requests types include
	 * POST, PUT, and DELETE.
	 */
	@NonNull
	private final Long numMutationRequestsLimit;

	/**
	 * The number of resource mutation requests remaining/available in the current subscription's quota. This number is
	 * the numAccessRequestsLimit minus the total number of requests that have been made during the current billing
	 * period. Access requests types include* POST, PUT, and DELETE.
	 */
	@NonNull
	private final Long numMutationRequestsRemaining;
}
