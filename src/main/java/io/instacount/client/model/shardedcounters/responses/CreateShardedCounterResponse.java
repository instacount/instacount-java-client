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

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import feign.Response;
import io.instacount.client.model.InstacountResponse;
import io.instacount.client.model.headers.Quota;

/**
 * A class for modeling HTTP responses that have no content, such as an HTTP 201, 204, and the like.
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CreateShardedCounterResponse extends InstacountResponse
{
	@NonNull
	private final Optional<CounterLocationInfo> optCounterInfo;

	public CreateShardedCounterResponse(final Response response, final Quota quota,
			final Optional<CounterLocationInfo> optCounterInfo)
	{
		super(response, quota);
		this.optCounterInfo = Preconditions.checkNotNull(optCounterInfo);
	}

	public int getHttpResponseCode()
	{
		return getResponse().status();
	}

}
