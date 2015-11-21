/**
 * Copyright (C) 2015 Instacount Inc. (developers@instacount.io)
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

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.squareup.okhttp.HttpUrl;

import feign.Response;
import feign.codec.Decoder;
import feign.jackson.JacksonDecoder;
import io.instacount.client.model.headers.Quota;
import io.instacount.client.model.shardedcounters.ShardedCounterOperation;
import io.instacount.client.model.shardedcounters.ShardedCounter;
import io.instacount.client.model.shardedcounters.responses.CounterLocationInfo;
import io.instacount.client.model.shardedcounters.responses.CreateShardedCounterResponse;
import io.instacount.client.model.shardedcounters.responses.DecrementShardedCounterResponse;
import io.instacount.client.model.shardedcounters.responses.GetShardedCounterOperationResponse;
import io.instacount.client.model.shardedcounters.responses.GetShardedCounterResponse;
import io.instacount.client.model.shardedcounters.responses.IncrementShardedCounterResponse;
import io.instacount.client.model.shardedcounters.responses.UpdateShardedCounterResponse;

/**
 * An extension of {@link JacksonDecoder} that captures other information not normally propogated from the super-class.
 */
public class InstacountJacksonDecoder extends AbstractInstacountDecoder implements Decoder
{
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * No-args Constructor.
	 */
	public InstacountJacksonDecoder()
	{
		this(Collections.<Module> emptyList());
	}

	/**
	 * Required-args Constructor.
	 *
	 * @param modules
	 */
	public InstacountJacksonDecoder(final Iterable<Module> modules)
	{
		this(new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
			.registerModules(modules));
	}

	/**
	 * Required-args Constructor.
	 *
	 * @param objectMapper
	 */
	public InstacountJacksonDecoder(final ObjectMapper objectMapper)
	{
		super(objectMapper);
	}

	@Override
	public Object decode(final Response response, final Type type) throws IOException
	{
		Preconditions.checkNotNull(response);
		Preconditions.checkNotNull(type);

		//////////////////
		// Decode the response...
		//////////////////

		if (CreateShardedCounterResponse.class.equals(type))
		{
			return this.constructCreateShardedCounterResponse(response);
		}
		else if (GetShardedCounterResponse.class.equals(type))
		{
			return this.constructGetShardedCounterResponse(response);
		}
		else if (UpdateShardedCounterResponse.class.equals(type))
		{
			return this.constructUpdateShardedCounterResponse(response);
		}
		// else if (DeleteCounterResponse.class.equalsIgnoreCase(type))
		// {
		// return this.constructDeleteCounterResponse(response);
		// }
		else if (IncrementShardedCounterResponse.class.equals(type))
		{
			return this.constructIncrementCounterResponse(response);
		}
		else if (DecrementShardedCounterResponse.class.equals(type))
		{
			return this.constructDecrementCounterResponse(response);
		}
		else if (GetShardedCounterOperationResponse.class.equals(type))
		{
			return this.constructGetCounterOperationResponse(response);
		}
		else
		{
			throw new RuntimeException(String.format("Unable to decode response with type '%s'", type));
		}

		// // Any 201 Created or 204 No Content response...just need to grab the Headers...
		// else if (response.status() == 201 || response.status() == 202 || response.status() == 204)
		// {
		// if (IncrementCounterResponse.class.getName().equalsIgnoreCase(type.toString()))
		// {
		// return this.constructIncrementCounterResponse(response);
		// }
		// if (DecrementCounterResponse.class.getName().equalsIgnoreCase(type.toString()))
		// {
		// return this.constructDecrementCounterResponse(response);
		// }
		//
		//
		// else
		// {
		// return this.constructNoContentResponse(response);
		// }
		// }
		// // Any 3xx response...just need to grab the Headers...
		// else if (response.status() > 299 && response.status() < 400)
		// {
		// return this.constructNoContentResponse(response);
		// }
		// Handle any other responses as an Errors object.
		// else
		// {
		// throw new InstacountClientException(this.constructErrorsResponse(response));
		// }
	}

	//////////////////
	// Private Helpers
	//////////////////

	/**
	 * Helper to construct an instance of {@link Quota} from the Feign {@link Response}.
	 * 
	 * @param response
	 * @return
	 */
	private Quota constructQuota(final Response response)
	{
		final Map<String, Collection<String>> headers = response.headers();
		final Long numAccessRequestsLimit = toLong(
			Optional.fromNullable(headers.get(Quota.X_RATELIMIT_ACCESS_COUNTERS_LIMIT)));
		final Long numAccessRequestsRemaining = toLong(
			Optional.fromNullable(headers.get(Quota.X_RATELIMIT_ACCESS_COUNTERS_REMAINING)));
		final Long numMutationRequestsLimit = toLong(
			Optional.fromNullable(headers.get(Quota.X_RATELIMIT_MUTATE_COUNTERS_LIMIT)));
		final Long numMutationRequestsRemaining = toLong(
			Optional.fromNullable(headers.get(Quota.X_RATELIMIT_MUTATE_COUNTERS_REMAINING)));

		return new Quota(numAccessRequestsLimit, numAccessRequestsRemaining, numMutationRequestsLimit,
			numMutationRequestsRemaining);
	}

	/**
	 * Safely convert a String-based collection of header values into a {@link Long} by attempting to convert the first
	 * header value.
	 *
	 * @param optHeaderValues
	 *
	 * @return
	 */
	private Long toLong(final Optional<Collection<String>> optHeaderValues)
	{
		Preconditions.checkNotNull(optHeaderValues);

		if (optHeaderValues.isPresent())
		{
			final Collection<String> headerValues = optHeaderValues.get();
			if (headerValues.isEmpty())
			{
				return 0L;
			}

			final String firstHeaderValueAsString = headerValues.iterator().next();
			try
			{
				return Long.parseLong(firstHeaderValueAsString);
			}
			catch (NumberFormatException nfe)
			{
				logger.severe(
					String.format("Invalid Header value \"%s\" should have been a Long!", firstHeaderValueAsString));
				return 0L;
			}
		}
		else
		{
			return 0L;
		}
	}

	/**
	 * Helper to construct an instance of {@link GetShardedCounterResponse} from the Feign {@link Response}.
	 * 
	 * @param response
	 * @return
	 * @throws IOException
	 */
	private GetShardedCounterResponse constructGetShardedCounterResponse(final Response response) throws IOException
	{
		final Optional<Reader> optReader = this.constructReader(response);
		if (optReader.isPresent())
		{
			final ShardedCounter shardedCounter = objectMapper.readValue(optReader.get(), ShardedCounter.class);
			return new GetShardedCounterResponse(response, constructQuota(response), shardedCounter);
		}
		else
		{
			throw new RuntimeException(
				String.format("Unable to construct ShardedCounter from Response Body: %s", response.body()));
		}
	}

	/**
	 * Helper to construct an instance of {@link IncrementShardedCounterResponse} from the Feign {@link Response}.
	 * 
	 * @param response
	 * @return
	 * @throws IOException
	 */
	private IncrementShardedCounterResponse constructIncrementCounterResponse(final Response response)
			throws IOException
	{
		if (response.status() == 201)
		{
			final Optional<Reader> optReader = this.constructReader(response);
			if (optReader.isPresent())
			{
				final ShardedCounterOperation counterOperation = objectMapper.readValue(optReader.get(),
					ShardedCounterOperation.class);
				return new IncrementShardedCounterResponse(response, constructQuota(response),
					Optional.of(counterOperation));
			}
			else
			{
				throw new RuntimeException(
					String.format("Unable to construct CounterOperation from Response Body: %s", response.body()));
			}

		}
		else
		{
			return new IncrementShardedCounterResponse(response, constructQuota(response),
				Optional.<ShardedCounterOperation> absent());
		}
	}

	/**
	 * Helper to construct an instance of {@link GetShardedCounterOperationResponse} from the Feign {@link Response}.
	 * 
	 * @param response
	 * @return
	 * @throws IOException
	 */
	private GetShardedCounterOperationResponse constructGetCounterOperationResponse(final Response response)
			throws IOException
	{
		final Optional<Reader> optReader = this.constructReader(response);
		if (optReader.isPresent())
		{
			final ShardedCounterOperation counterOperation = objectMapper.readValue(optReader.get(), ShardedCounterOperation.class);
			return new GetShardedCounterOperationResponse(response, constructQuota(response), counterOperation);
		}
		else
		{
			throw new RuntimeException(
				String.format("Unable to construct CounterOperation from Response Body: %s", response.body()));
		}
	}

	/**
	 * Helper to construct an instance of {@link DecrementShardedCounterResponse} from the Feign {@link Response}.
	 * 
	 * @param response
	 * @return
	 * @throws IOException
	 */
	private DecrementShardedCounterResponse constructDecrementCounterResponse(final Response response)
			throws IOException
	{
		if (response.status() == 201)
		{
			final Optional<Reader> optReader = this.constructReader(response);
			if (optReader.isPresent())
			{
				final ShardedCounterOperation counterOperation = objectMapper.readValue(optReader.get(),
					ShardedCounterOperation.class);
				return new DecrementShardedCounterResponse(response, constructQuota(response),
					Optional.of(counterOperation));
			}
			else
			{
				throw new RuntimeException(
					String.format("Unable to construct CounterOperation from Response Body: %s", response.body()));
			}
		}
		else
		{
			return new DecrementShardedCounterResponse(response, constructQuota(response),
				Optional.<ShardedCounterOperation> absent());
		}
	}

	/**
	 * Helper to construct an instance of {@link CreateShardedCounterResponse} from the Feign {@link Response}.
	 * 
	 * @param response
	 * @return
	 * @throws IOException
	 */
	private CreateShardedCounterResponse constructCreateShardedCounterResponse(final Response response)
			throws IOException
	{
		return new CreateShardedCounterResponse(response, constructQuota(response),
			constructCounterLocationInfo(response));
	}

	/**
	 * Helper to construct an optionally present instance of {@link CounterLocationInfo} from the Feign {@link Response}
	 * .
	 * 
	 * @param response
	 * @return
	 */
	private Optional<CounterLocationInfo> constructCounterLocationInfo(final Response response)
	{
		final Optional<CounterLocationInfo> optCounterInfo;
		final Collection<String> locationHeaders = response.headers().get("Location");
		if (!locationHeaders.isEmpty())
		{
			final String location = locationHeaders.iterator().next();
			final HttpUrl httpUrl = HttpUrl.parse(location);
			Preconditions.checkNotNull(httpUrl);
			final String counterName = httpUrl.pathSegments().get(1);
			final CounterLocationInfo counterLocationHeaderInfo = new CounterLocationInfo(location, counterName);
			optCounterInfo = Optional.fromNullable(counterLocationHeaderInfo);
		}
		else
		{
			optCounterInfo = Optional.absent();
		}
		return optCounterInfo;
	}

	/**
	 * Helper to construct an instance of {@link UpdateShardedCounterResponse} from the Feign {@link Response}.
	 * 
	 * @param response
	 * @return
	 * @throws IOException
	 */
	private UpdateShardedCounterResponse constructUpdateShardedCounterResponse(final Response response)
			throws IOException
	{
		final Optional<Reader> optReader = this.constructReader(response);
		if (optReader.isPresent())
		{
			final ShardedCounter shardedCounter = objectMapper.readValue(optReader.get(), ShardedCounter.class);
			return new UpdateShardedCounterResponse(response, constructQuota(response), shardedCounter);
		}
		else
		{
			throw new RuntimeException(
				String.format("Unable to construct ShardedCounter from Response Body: %s", response.body()));
		}
	}

}
