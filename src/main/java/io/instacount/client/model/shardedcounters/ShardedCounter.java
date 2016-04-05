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
package io.instacount.client.model.shardedcounters;

import java.math.BigInteger;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Optional;
import io.instacount.client.model.shardedcounters.meta.ShardedCounterMeta;

/**
 * A class for modeling a Counter resource in the Instacount API.
 *
 * @see "https://instacount.readme.io/docs/shardedcountersname"
 */
@Getter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class ShardedCounter
{
	@JsonProperty("meta")
	@NonNull
	private final ShardedCounterMeta meta;

	@JsonProperty("name")
	@NonNull
	private final String name;

	@JsonProperty("description")
	@NonNull
	private final Optional<String> optDescription;

	@JsonProperty("numShards")
	@NonNull
	private final Integer numShards;

	@JsonProperty("status")
	@NonNull
	private final CounterStatus counterStatus;

	@JsonProperty("created")
	@NonNull
	private final DateTime createdDateTime;

	@JsonProperty("count")
	@NonNull
	private final BigInteger count;

	/**
	 * No-args Constructor, for use only for Jackson.
	 */
	private ShardedCounter()
	{
		this.meta = null;
		this.name = null;
		this.optDescription = null;
		this.numShards = null;
		this.counterStatus = null;
		this.createdDateTime = null;
		this.count = null;
	}

	/**
	 * An enumeration of potential counter statuses that a newly created or updated counter might be put into. This
	 * differs from {@link CounterStatus} in that the API can set a counter into various statuses that a client is not
	 * able to set.
	 */
	public enum CounterStatus
	{
		/** This Counter is available to be incremented, decremented, or deleted. */
		AVAILABLE,

		/** This Counter is not available to be incremented or decremented, though its details can be updated. */
		READ_ONLY_COUNT,

		/**
		 * This Counter is expanding the number of shards it holds internally, and may not be incremented,
		 * decremented,or deleted, or mutated.
		 */
		EXPANDING_SHARDS,

		/**
		 * This Counter is contracting the number of shards it holds internally, and may not be incremented,decremented,
		 * or deleted, or mutated.
		 */
		CONTRACTING_SHARDS,

		/**
		 * This Counter is in the process of having all shard amounts set to zerp, and may not be incremented or
		 * decremented.
		 **/
		RESETTING,

		/**
		 * This Counter is in the process of being deleted, and may not be incremented or decremented and its details
		 * may not be changed.
		 **/
		DELETING
	}
}
