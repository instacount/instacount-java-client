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
import io.instacount.client.model.shardedcounters.meta.ShardedCounterOperationMeta;

/**
 * A class for modeling a Counter Operation resource in the Instacount API.
 *
 * @see "https://instacount.readme.io/docs/shardedcountersname"
 */
@Getter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class ShardedCounterOperation
{
	@JsonProperty("meta")
	@NonNull
	private final ShardedCounterOperationMeta meta;

	@JsonProperty("id")
	@NonNull
	private final String id;

	@JsonProperty("shardIndex")
	@NonNull
	private final Integer shardIndex;

	@JsonProperty("type")
	@NonNull
	private final CounterOperationType counterOperationType;

	@JsonProperty("amount")
	@NonNull
	private final BigInteger amount;

	@JsonProperty("created")
	@NonNull
	private final DateTime createdDateTime;

	/**
	 * No-args Constructor, for use only for Jackson.
	 */
	private ShardedCounterOperation()
	{
		this.meta = null;
		this.id = null;
		this.shardIndex = null;
		this.counterOperationType = null;
		this.amount = null;
		this.createdDateTime = null;
	}

	/**
	 * The type of operation that was performed on a Counter resource.
	 */
	public enum CounterOperationType
	{
		INCREMENT, DECREMENT;
	}
}
