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
package io.instacount.client.model;

import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import com.google.common.collect.ImmutableList;

/**
 * A class that models a collection of errors returned from the Instacount API.
 *
 * @see "https://instacount.readme.io/docs/errors"
 */
@Getter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class Errors
{
	@NonNull
	private final Integer httpResponseCode;

	@NonNull
	private final List<Error> errors;

	/**
	 * No-args Constructor, exists only for Jackson.
	 */
	private Errors()
	{
		this.httpResponseCode = null;
		this.errors = null;
	}

	/**
	 * An empty instance of {@link Errors}.
	 * 
	 * @param httpResponseCode
	 * @return
	 */
	public static Errors empty(final int httpResponseCode)
	{
		return new Errors(httpResponseCode, ImmutableList.<Error> of());
	}

	/**
	 * A class that models an error returned from the Instacount API.
	 *
	 * @see "https://instacount.readme.io/docs/errors"
	 */
	@Getter
	@RequiredArgsConstructor
	@ToString
	@EqualsAndHashCode
	public static class Error
	{
		@NonNull
		private final String message;

		@NonNull
		private final String developerMessage;

		@NonNull
		private final String moreInfo;

		/**
		 * No-args Constructor, for use only by Jackson.
		 */
		private Error()
		{
			this.message = null;
			this.developerMessage = null;
			this.moreInfo = null;
		}
	}
}