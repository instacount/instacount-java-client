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
package io.instacount.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import feign.Feign;
import feign.Logger.JavaLogger;
import feign.Logger.Level;
import feign.Param;
import feign.RequestInterceptor;
import feign.RequestLine;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import io.instacount.client.decoders.InstacountErrorDecoder;
import io.instacount.client.decoders.InstacountJacksonDecoder;
import io.instacount.client.exceptions.InstacountClientException;
import io.instacount.client.interceptors.InstacountVersionRequestInterceptor;
import io.instacount.client.jackson.InstacountClientObjectMapper;
import io.instacount.client.model.shardedcounters.ShardedCounterOperation;
import io.instacount.client.model.shardedcounters.inputs.CreateShardedCounterInput;
import io.instacount.client.model.shardedcounters.inputs.DecrementShardedCounterInput;
import io.instacount.client.model.shardedcounters.inputs.IncrementShardedCounterInput;
import io.instacount.client.model.shardedcounters.inputs.UpdateShardedCounterInput;
import io.instacount.client.model.shardedcounters.responses.CreateShardedCounterResponse;
import io.instacount.client.model.shardedcounters.responses.DecrementShardedCounterResponse;
import io.instacount.client.model.shardedcounters.responses.DeleteShardedCounterResponse;
import io.instacount.client.model.shardedcounters.responses.GetShardedCounterOperationResponse;
import io.instacount.client.model.shardedcounters.responses.GetShardedCounterResponse;
import io.instacount.client.model.shardedcounters.responses.IncrementShardedCounterResponse;
import io.instacount.client.model.shardedcounters.responses.UpdateShardedCounterResponse;

/**
 * A feign client for interacting with the Instacount API.
 *
 * @see "https://www.instacount.io"
 * @see "https://instacount.readme.io/docs/api-clients"
 */
public interface Instacount
{
	// TODO: Application Access?

	/**
	 * Creates a new counter with a count of zero (0) and a unique "name" provided by Instacount.
	 */
	CreateShardedCounterResponse createShardedCounter() throws InstacountClientException;

	/**
	 * Creates a new counter using data from the specified {@code createCounterInput}.
	 *
	 * @param createCounterInput An instance of {@link CreateShardedCounterInput}.
	 */
	CreateShardedCounterResponse createShardedCounter(CreateShardedCounterInput createCounterInput)
			throws InstacountClientException;

	/**
	 * Gets the counter with the specified {@code counterName} from the Instacount API.
	 *
	 * @param counterName A {@link String} representing the name of the ShardedCounter resource.
	 */
	GetShardedCounterResponse getShardedCounter(String counterName) throws InstacountClientException;

	/**
	 * Creates a new counter using data from the specified {@code createCounterInput}.
	 *
	 * @param counterName A {@link String} representing the name of the ShardedCounter resource.
	 * @param updateCounterInput An instance of {@link UpdateShardedCounterInput}.
	 */
	UpdateShardedCounterResponse updateShardedCounter(String counterName, UpdateShardedCounterInput updateCounterInput)
			throws InstacountClientException;

	/**
	 * Creates a new counter using data from the specified {@code createCounterInput}.
	 *
	 * @param counterName A {@link String} representing the name of the ShardedCounter resource.
	 */
	DeleteShardedCounterResponse deleteShardedCounter(String counterName) throws InstacountClientException;

	/**
	 * Increments the counter named {@code counterName} by 1.
	 *
	 * @param counterName
	 */
	IncrementShardedCounterResponse incrementShardedCounter(String counterName) throws InstacountClientException;

	/**
	 * Decrements the counter named {@code counterName} by 1.
	 *
	 * @param counterName
	 */
	DecrementShardedCounterResponse decrementShardedCounter(String counterName) throws InstacountClientException;

	/**
	 * Increments the counter named {@code counterName} by the amount specified in {@code amount}, using the specified
	 * async characteristics..
	 *
	 * @param counterName A {@link String} representing the name of the counter to incrementShardedCounter.
	 * @param incrementCounterInput An instance of {@link IncrementShardedCounterInput} for shaping the operation.
	 */
	IncrementShardedCounterResponse incrementShardedCounter(String counterName,
			IncrementShardedCounterInput incrementCounterInput) throws InstacountClientException;

	/**
	 * Decrements the counter named {@code counterName} by the amount specified in {@code amount}, using the specified
	 * async characteristics..
	 *
	 * @param counterName A {@link String} representing the name of the counter to decrementShardedCounter.
	 * @param decrementCounterInput An instance of {@link DecrementShardedCounterInput} for shaping the operation.
	 */
	DecrementShardedCounterResponse decrementShardedCounter(String counterName,
			DecrementShardedCounterInput decrementCounterInput) throws InstacountClientException;

	/**
	 * Get the {@link ShardedCounterOperation} for the specified inputs.
	 *
	 * @param counterName A {@link String} representing the name of the counter resource.
	 * @param shardIndex The 0-based index of the shard this operation was performed against.
	 * @param operationId A {@link String} representing the unique identifier of the operation being requested.
	 */
	GetShardedCounterOperationResponse getShardedCounterOperation(String counterName, Integer shardIndex,
			String operationId) throws InstacountClientException;

	/**
	 * The feign interface for the Instacount client.
	 *
	 * @see "https://github.com/Netflix/feign"
	 */
	interface InstacountFeign extends Instacount
	{
		@Override
		@RequestLine("POST /sharded_counters")
		CreateShardedCounterResponse createShardedCounter() throws InstacountClientException;

		@Override
		@RequestLine("POST /sharded_counters")
		CreateShardedCounterResponse createShardedCounter(CreateShardedCounterInput createCounterInput)
				throws InstacountClientException;

		@Override
		@RequestLine("GET /sharded_counters/{counterName}")
		GetShardedCounterResponse getShardedCounter(@Param("counterName") String counterName)
				throws InstacountClientException;

		@Override
		@RequestLine("PUT /sharded_counters/{counterName}")
		UpdateShardedCounterResponse updateShardedCounter(@Param("counterName") String counterName,
				UpdateShardedCounterInput updateCounterInput) throws InstacountClientException;

		@Override
		@RequestLine("DELETE /sharded_counters/{counterName}")
		DeleteShardedCounterResponse deleteShardedCounter(@Param("counterName") String counterName)
				throws InstacountClientException;

		@Override
		@RequestLine("POST /sharded_counters/{counterName}/increments")
		IncrementShardedCounterResponse incrementShardedCounter(@Param("counterName") String counterName)
				throws InstacountClientException;

		@Override
		@RequestLine("POST /sharded_counters/{counterName}/decrements")
		DecrementShardedCounterResponse decrementShardedCounter(@Param("counterName") String counterName)
				throws InstacountClientException;

		@Override
		@RequestLine("POST /sharded_counters/{counterName}/increments")
		IncrementShardedCounterResponse incrementShardedCounter(@Param("counterName") String counterName,
				IncrementShardedCounterInput incrementCounterInput) throws InstacountClientException;

		@Override
		@RequestLine("POST /sharded_counters/{counterName}/decrements")
		DecrementShardedCounterResponse decrementShardedCounter(@Param("counterName") String counterName,
				DecrementShardedCounterInput decrementCounterInput) throws InstacountClientException;

		@Override
		@RequestLine("GET /sharded_counters/{counterName}/shards/{shardIndex}/operations/{operationId}")
		GetShardedCounterOperationResponse getShardedCounterOperation(@Param("counterName") String counterName,
				@Param("shardIndex") Integer shardIndex, @Param("operationId") String operationId)
						throws InstacountClientException;

	}
	/**
	 * A class for building instances of {@link Instacount}.
	 */
	class Builder
	{
		/**
		 * A default builder for creating an instacount client that will interact with the Instacount API using supplied
		 * credentials.
		 * 
		 * @param instacountClientParams An instance of {@link InstacountClientParams} for providing custom
		 *            authentication credentials and other information unique to each requestor.
		 * @return An instance of {@link Instacount} that can be used to make calls to the Instacount API.
		 */
		public static Instacount build(final InstacountClientParams instacountClientParams,
				final RequestInterceptor... additionalRequestInterceptors)
		{
			final ObjectMapper objectMapper = new InstacountClientObjectMapper();

			final ImmutableList.Builder<RequestInterceptor> requestInterceptorsBuilder = ImmutableList.builder();
			requestInterceptorsBuilder.add(new InstacountVersionRequestInterceptor.Impl());
			requestInterceptorsBuilder.add(instacountClientParams);
			requestInterceptorsBuilder.add(additionalRequestInterceptors);

			return new InstacountWrapper(
				Feign.builder().client(new OkHttpClient()).logLevel(Level.FULL).logger(new JavaLogger()
			// .appendToFile("httpLog.txt")
			).errorDecoder(new InstacountErrorDecoder(objectMapper)).encoder(new JacksonEncoder(objectMapper))
					.decoder(new InstacountJacksonDecoder(objectMapper))
					.requestInterceptors(requestInterceptorsBuilder.build())
					.target(InstacountFeign.class, instacountClientParams.getInstacountRootUrl()));
		}

		/**
		 * The default implementation of {@link Instacount} that provides basic null-checking around parameters.
		 */
		private static class InstacountWrapper implements Instacount
		{
			private final Instacount instacount;

			/**
			 * Required-args Constructor.
			 * 
			 * @param instacountFeign An instance of {@link InstacountFeign}.
			 */
			private InstacountWrapper(final InstacountFeign instacountFeign)
			{
				this.instacount = Preconditions.checkNotNull(instacountFeign);
			}

			@Override
			public CreateShardedCounterResponse createShardedCounter() throws InstacountClientException
			{
				return this.instacount.createShardedCounter();
			}

			@Override
			public CreateShardedCounterResponse createShardedCounter(final CreateShardedCounterInput createCounterInput)
					throws InstacountClientException
			{
				Preconditions.checkNotNull(createCounterInput);
				return this.instacount.createShardedCounter(createCounterInput);
			}

			@Override
			public GetShardedCounterResponse getShardedCounter(@Param("counterName") final String counterName)
					throws InstacountClientException
			{
				this.validateCounterName(counterName);
				return this.instacount.getShardedCounter(counterName);
			}

			@Override
			public UpdateShardedCounterResponse updateShardedCounter(@Param("counterName") final String counterName,
					final UpdateShardedCounterInput updateCounterInput) throws InstacountClientException
			{
				this.validateCounterName(counterName);
				Preconditions.checkNotNull(updateCounterInput);
				return this.instacount.updateShardedCounter(counterName, updateCounterInput);
			}

			@Override
			public DeleteShardedCounterResponse deleteShardedCounter(@Param("counterName") final String counterName)
					throws InstacountClientException
			{
				this.validateCounterName(counterName);
				return this.instacount.deleteShardedCounter(counterName);
			}

			@Override
			public IncrementShardedCounterResponse incrementShardedCounter(
					@Param("counterName") final String counterName) throws InstacountClientException
			{
				this.validateCounterName(counterName);
				return this.instacount.incrementShardedCounter(counterName);
			}

			@Override
			public DecrementShardedCounterResponse decrementShardedCounter(
					@Param("counterName") final String counterName) throws InstacountClientException
			{
				this.validateCounterName(counterName);
				return this.instacount.decrementShardedCounter(counterName);
			}

			@Override
			public IncrementShardedCounterResponse incrementShardedCounter(
					@Param("counterName") final String counterName,
					final IncrementShardedCounterInput incrementCounterInput) throws InstacountClientException
			{
				this.validateCounterName(counterName);
				Preconditions.checkNotNull(incrementCounterInput);
				return this.instacount.incrementShardedCounter(counterName, incrementCounterInput);
			}

			@Override
			public DecrementShardedCounterResponse decrementShardedCounter(
					@Param("counterName") final String counterName,
					final DecrementShardedCounterInput decrementCounterInput) throws InstacountClientException
			{
				this.validateCounterName(counterName);
				Preconditions.checkNotNull(decrementCounterInput);
				return this.instacount.decrementShardedCounter(counterName, decrementCounterInput);
			}

			@Override
			public GetShardedCounterOperationResponse getShardedCounterOperation(
					@Param("counterName") final String counterName, @Param("shardIndex") final Integer shardIndex,
					@Param("operationId") final String operationId) throws InstacountClientException
			{
				this.validateCounterName(counterName);
				Preconditions.checkNotNull(shardIndex);
				Preconditions.checkNotNull(operationId);
				this.validateCounterName(operationId);
				return this.instacount.getShardedCounterOperation(counterName, shardIndex, operationId);
			}

			/**
			 * Helper method to validate the name of the counter being operated upon.
			 * 
			 * @param counterName
			 */
			private void validateCounterName(final String counterName)
			{
				Preconditions.checkNotNull(counterName);
				Preconditions.checkArgument(counterName.length() > 0);
			}
		}
	}

}
