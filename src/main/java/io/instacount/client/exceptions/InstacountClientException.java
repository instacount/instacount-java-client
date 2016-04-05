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
package io.instacount.client.exceptions;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import com.google.common.base.Preconditions;
import io.instacount.client.model.Errors;

/**
 * An instance of {@link RuntimeException} for providing information about Instacount errors.
 *
 * @see "https://instacount.readme.io/docs/errors"
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class InstacountClientException extends RuntimeException
{
	@NonNull
	private final Errors errors;

	/**
	 * Required args constructor.
	 * 
	 * @param errors An instance of {@link Errors}.
	 */
	public InstacountClientException(final Errors errors)
	{
		this.errors = Preconditions.checkNotNull(errors);
	}

	/**
	 * Required args constructor.
	 * 
	 * @param t An instance of {@link Throwable}.
	 * @param errors An instance of {@link Errors}.
	 */
	public InstacountClientException(final Throwable t, final Errors errors)
	{
		super(t);
		this.errors = errors;
	}

}
