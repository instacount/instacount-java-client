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
import lombok.ToString;

import com.google.common.base.Optional;

/**
 * A class for creating a new Counter resource in the Instacount API.
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UpdateShardedCounterInput extends AbstractCounterInput
{
	/**
	 * Required-args Constructor.
	 *
	 * @param name The unique name of the counter trying to be created.
	 * @param optDescription The description of this counter.
	 * @param numShards The number of shards this counter should incrementShardedCounter across.
	 * @param counterStatusInput
	 *
	 * @see ""
	 */
	public UpdateShardedCounterInput(final String name, final Optional<String> optDescription, final int numShards,
			final ShardedCounterStatusInput counterStatusInput)
	{
		super(name, optDescription, numShards, counterStatusInput);
	}

	/**
	 * Construct an instance that has only the name populated.
	 *
	 * @param name The unique name of the counter trying to be created.
	 */
	public UpdateShardedCounterInput(final String name)
	{
		super(name);
	}

	/**
	 * Construct an instance that has only the name populated.
	 *
	 * @param name The unique name of the counter trying to be created.
	 * @param description The description of this counter.
	 */
	public UpdateShardedCounterInput(final String name, final String description)
	{
		super(name, description);
	}
}
