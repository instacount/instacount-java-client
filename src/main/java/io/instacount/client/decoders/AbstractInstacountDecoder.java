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
package io.instacount.client.decoders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.RuntimeJsonMappingException;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import feign.Response;
import feign.codec.ErrorDecoder;
import io.instacount.client.exceptions.InstacountClientException;
import io.instacount.client.model.Errors;

/**
 * An abstract implementation of {@link ErrorDecoder} for marshaling instacount.io API responses into typed java
 * instances.
 */
public abstract class AbstractInstacountDecoder
{
	protected final ObjectMapper objectMapper;

	/**
	 * Required-args Constructor.
	 *
	 * @param objectMapper An instance of {@link ObjectMapper}.
	 */
	public AbstractInstacountDecoder(final ObjectMapper objectMapper)
	{
		this.objectMapper = Preconditions.checkNotNull(objectMapper);
	}

	/**
	 * Helper method to construct an instance of {@link Reader} for reading the JSON response from Instacount.
	 *
	 * @param response
	 * @return
	 * @throws IOException
	 */
	protected Optional<Reader> constructReader(final Response response) throws IOException
	{
		Preconditions.checkNotNull(response);
		Preconditions.checkNotNull(response.body());

		Reader reader = response.body().asReader();
		if (!reader.markSupported())
		{
			reader = new BufferedReader(reader, 1);
		}
		try
		{
			// Read the first byte to see if we have any data
			reader.mark(1);
			if (reader.read() == -1)
			{
				// Eagerly returning null avoids "No content to map due to end-of-input"
				return Optional.absent();
			}
			reader.reset();
		}
		catch (RuntimeJsonMappingException e)
		{
			if (e.getCause() != null && e.getCause() instanceof IOException)
			{
				throw IOException.class.cast(e.getCause());
			}
			throw e;
		}

		return Optional.fromNullable(reader);
	}

	/**
	 * Construct an instance of {@link Errors} from the supplied {@code response}.
	 * 
	 * @param response An instance of {@link Response} that contains a response of the Instacount API.
	 * @return
	 */
	protected Errors constructErrorsResponse(final Response response) throws InstacountClientException
	{
		try
		{
			final Errors errorsWithoutHttpCode = objectMapper.readValue(response.body().asInputStream(), Errors.class);
			return new Errors(response.status(), errorsWithoutHttpCode.getErrors());
		}
		catch (Exception e)
		{
			throw new InstacountClientException(e, Errors.empty(response.status()));
		}
	}

}
