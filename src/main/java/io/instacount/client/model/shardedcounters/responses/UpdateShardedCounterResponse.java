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
package io.instacount.client.model.shardedcounters.responses;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import com.google.common.base.Preconditions;

import feign.Response;
import io.instacount.client.model.InstacountResponse;
import io.instacount.client.model.headers.Quota;
import io.instacount.client.model.shardedcounters.ShardedCounter;

/**
 * A class for modeling the respose of an update-counter operation.
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UpdateShardedCounterResponse extends InstacountResponse
{
	@NonNull
	private final ShardedCounter shardedCounter;

	/**
	 * Required-args Constructor.
	 *
	 * @param response
	 * @param quota
	 * @param shardedCounter
	 */
	public UpdateShardedCounterResponse(final Response response, final Quota quota, final ShardedCounter shardedCounter)
	{
		super(response, quota);
		this.shardedCounter = Preconditions.checkNotNull(shardedCounter);
	}

}
