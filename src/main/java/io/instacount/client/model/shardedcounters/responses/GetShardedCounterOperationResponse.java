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
package io.instacount.client.model.shardedcounters.responses;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import feign.Response;
import io.instacount.client.model.InstacountResponse;
import io.instacount.client.model.headers.Quota;
import io.instacount.client.model.shardedcounters.ShardedCounterOperation;

/**
 * A class for modeling HTTP responses that have no content, such as an HTTP 201, 204, and the like.
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class GetShardedCounterOperationResponse extends InstacountResponse
{
	@NonNull
	private final ShardedCounterOperation counterOperation;

	/**
	 * Required-args Constructor.
	 *
	 * @param response
	 * @param quota
	 * @param counterOperation
	 */
	public GetShardedCounterOperationResponse(final Response response, final Quota quota,
			final ShardedCounterOperation counterOperation)
	{
		super(response, quota);
		this.counterOperation = counterOperation;
	}
}
