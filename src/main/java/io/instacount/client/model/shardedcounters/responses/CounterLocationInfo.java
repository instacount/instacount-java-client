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
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * An internal class to model information about a newly created counter. All of the information in this class is taken
 * from the Location header in the HTTP resposne.
 */
@Getter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class CounterLocationInfo
{
	@NonNull
	private final String locationUrl;
	@NonNull
	private final String counterName;
}
