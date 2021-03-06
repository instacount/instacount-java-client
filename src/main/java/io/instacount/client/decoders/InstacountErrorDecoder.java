/**
 * Copyright (C) 2016 Instacount Inc. (developers@instacount.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.instacount.client.decoders;

import com.fasterxml.jackson.databind.ObjectMapper;

import feign.Response;
import feign.codec.ErrorDecoder;
import io.instacount.client.exceptions.InstacountClientException;
import io.instacount.client.model.Errors;

/**
 * An implementation of {@link ErrorDecoder} for marshaling instacount.io API errors into an instance of {@link Errors}.
 */
public class InstacountErrorDecoder extends AbstractInstacountDecoder implements ErrorDecoder
{
	/**
	 * Required-args Constructor.
	 * 
	 * @param objectMapper An instance of {@link ObjectMapper}.
	 */
	public InstacountErrorDecoder(final ObjectMapper objectMapper)
	{
		super(objectMapper);
	}

	@Override
	public Exception decode(final String methodKey, final Response response)
	{
		final Errors errors = this.constructErrorsResponse(response);
		return new InstacountClientException(errors);
	}

}
