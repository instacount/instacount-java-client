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
package io.instacount.client.model.shardedcounters.meta;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import com.sappenin.utils.rest.v2.model.Link;
import com.sappenin.utils.rest.v2.model.meta.Meta;
import com.sappenin.utils.rest.v2.model.meta.impl.AbstractMeta;

/**
 * An extension of {@link AbstractMeta} for Counter resources in the Instacount API.
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ShardedCounterOperationMeta extends AbstractMeta implements Meta
{
	@JsonProperty("@counter")
	private final Link counterLink;

	/**
	 * Required-args Constructor.
	 *
	 * @param selfLink
	 * @param counterLink
	 */
	public ShardedCounterOperationMeta(@JsonProperty("@self") final Link selfLink,
			@JsonProperty("@counter") final Link counterLink)
	{
		super(selfLink);
		this.counterLink = Preconditions.checkNotNull(counterLink);
	}

}
