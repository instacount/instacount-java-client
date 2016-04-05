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
package io.instacount.client.model.shardedcounters.inputs;

import io.instacount.client.model.shardedcounters.ShardedCounter.CounterStatus;

/**
 * An enumeration of potential counter statuses that a newly created or updated counter might be put into. This differs
 * from {@link CounterStatus} in that the API can set a counter into various statuses that a client is not able to set.
 */
public enum ShardedCounterStatusInput
{
	/** This Counter is available to be incremented, decremented, or deleted. */
	AVAILABLE,

	/** This Counter is not available to be incremented or decremented, though its details can be updated. */
	READ_ONLY_COUNT
}
