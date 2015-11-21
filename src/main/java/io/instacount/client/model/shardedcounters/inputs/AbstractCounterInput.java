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
package io.instacount.client.model.shardedcounters.inputs;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

/**
 * A class for creating a new Counter resource in the Instacount API.
 */
@Getter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public abstract class AbstractCounterInput
{
	/** Limited to 500 characters! */
	@JsonProperty("name")
	@NonNull
	private final String name;

	/** Limited to 500 characters! */
	@JsonProperty("description")
	@NonNull
	private final Optional<String> optDescription;

	/** Currently limited to 10! */
	@JsonProperty("numShards")
	@NonNull
	private final int numShards;

	@JsonProperty("status")
	@NonNull
	private final ShardedCounterStatusInput counterStatusInput;

	/**
	 * Construct an instance that has only the name populated.
	 *
	 * @param name The unique name of the counter trying to be created.
	 */
	public AbstractCounterInput(final String name)
	{
		this.name = this.validateCounterName(name);
		this.optDescription = Optional.absent();
		this.numShards = 3;
		this.counterStatusInput = ShardedCounterStatusInput.AVAILABLE;
	}

	/**
	 * Construct an instance that has only the name populated.
	 *
	 * @param name The unique name of the counter trying to be created.
	 * @param description The description of this counter.
	 */
	public AbstractCounterInput(final String name, final String description)
	{
		this.name = this.validateCounterName(name);
		this.optDescription = Optional.of(description);
		this.numShards = 3;
		this.counterStatusInput = ShardedCounterStatusInput.AVAILABLE;
	}

	/**
	 * Helper to validate the name of the counter to be operated upon.
	 * 
	 * @param counterName
	 * @return
	 */
	protected String validateCounterName(final String counterName)
	{
		Preconditions.checkNotNull(counterName);
		Preconditions.checkArgument(counterName.length() > 0);
		return counterName;
	}
}
