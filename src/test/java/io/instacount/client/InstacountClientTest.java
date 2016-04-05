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
package io.instacount.client;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;

import java.math.BigInteger;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import io.instacount.client.Constants.Links;
import io.instacount.client.InstacountClientParams.AbstractInstacountClientParams;
import io.instacount.client.exceptions.InstacountClientException;
import io.instacount.client.model.Errors;
import io.instacount.client.model.Errors.Error;
import io.instacount.client.model.InstacountResponse;
import io.instacount.client.model.shardedcounters.ShardedCounter;
import io.instacount.client.model.shardedcounters.ShardedCounter.CounterStatus;
import io.instacount.client.model.shardedcounters.ShardedCounterOperation;
import io.instacount.client.model.shardedcounters.ShardedCounterOperation.CounterOperationType;
import io.instacount.client.model.shardedcounters.inputs.CreateShardedCounterInput;
import io.instacount.client.model.shardedcounters.inputs.DecrementShardedCounterInput;
import io.instacount.client.model.shardedcounters.inputs.IncrementShardedCounterInput;
import io.instacount.client.model.shardedcounters.inputs.ShardedCounterStatusInput;
import io.instacount.client.model.shardedcounters.inputs.UpdateShardedCounterInput;
import io.instacount.client.model.shardedcounters.responses.CounterLocationInfo;
import io.instacount.client.model.shardedcounters.responses.CreateShardedCounterResponse;
import io.instacount.client.model.shardedcounters.responses.DecrementShardedCounterResponse;
import io.instacount.client.model.shardedcounters.responses.GetShardedCounterResponse;
import io.instacount.client.model.shardedcounters.responses.IncrementShardedCounterResponse;
import io.instacount.client.model.shardedcounters.responses.UpdateShardedCounterResponse;

/**
 * A test harness for testing the Instacount Client.
 */
public class InstacountClientTest
{
	private static final boolean SYNC = false;
	private static final boolean ASYNC = true;

	private static Instacount client;

	@BeforeClass
	public static void before()
	{
		final AbstractInstacountClientParams params = new AbstractInstacountClientParams(false)
		{
			@Override
			public String getInstacountApplicationId()
			{
				return Preconditions.checkNotNull(System.getenv("INSTACOUNT_APPLICATION_ID"),
					"System Env variable 'INSTACOUNT_APPLICATION_ID' not specified!");
			}

			@Override
			public String getInstacountReadOnlyApplicationKey()
			{
				return Preconditions.checkNotNull(System.getenv("INSTACOUNT_READ_ONLY_KEY"),
					"System Env variable 'INSTACOUNT_READ_ONLY_KEY' not specified!");
			}

			@Override
			public String getInstacountReadWriteApplicationKey()
			{
				return Preconditions.checkNotNull(System.getenv("INSTACOUNT_READ_WRITE_KEY"),
					"System Env variable 'INSTACOUNT_READ_WRITE_KEY' not specified!");
			}
		};

		final String accessToken = System.getenv("GOOGLE_ACCOUNTS_OAUTH_ACCESS_TOKEN");
		if (!StringUtils.isBlank(accessToken))
		{
			final InstacountOAuth2BearerRequestInterceptor oauth2 = new InstacountOAuth2BearerRequestInterceptor.Impl(
				accessToken);
			client = Instacount.Builder.build(params, oauth2);
		}
		else
		{
			client = Instacount.Builder.build(params);
		}
	}

	/////////////////////////////
	// Sharded Counter Happy Path
	/////////////////////////////

	@Test
	public void testShardedCounterHappyPath() throws InstacountClientException
	{
		try
		{
			final String counterName = UUID.randomUUID().toString();
			{
				/////////
				// Create the Counter
				final CreateShardedCounterInput createCounterInput = new CreateShardedCounterInput(counterName);
				final CreateShardedCounterResponse createdCounterResponse = this.client
					.createShardedCounter(createCounterInput);
				this.doBasicAssertions(createdCounterResponse, 201);
			}
			{
				/////////
				// Get the Counter
				final GetShardedCounterResponse createdShardedCounterResponse = this.client
					.getShardedCounter(counterName);
				this.doBasicAssertions(createdShardedCounterResponse, 200);
				this.doShardedCounterAssertions(createdShardedCounterResponse.getShardedCounter(), counterName,
					Optional.<String> absent(), 3, CounterStatus.AVAILABLE);
			}
			{
				////////////
				// Increment the Counter by 1
				final IncrementShardedCounterResponse incrementShardedCounterResponse = client
					.incrementShardedCounter(counterName);
				this.doBasicAssertions(incrementShardedCounterResponse, 201);
			}
			{
				////////////
				// Increment the Counter by 10
				final IncrementShardedCounterResponse incrementShardedCounterResponse2 = client
					.incrementShardedCounter(counterName, new IncrementShardedCounterInput(BigInteger.TEN, false));
				this.doBasicAssertions(incrementShardedCounterResponse2, 201);
			}
			{
				////////////
				// Decrement the Counter by 1
				final DecrementShardedCounterResponse decrementShardedCounterResponse = client
					.decrementShardedCounter(counterName);
				this.doBasicAssertions(decrementShardedCounterResponse, 201);
			}
			{
				////////////
				// Decrement the Counter by 10
				final DecrementShardedCounterResponse decrementShardedCounterResponse2 = client
					.decrementShardedCounter(counterName, new DecrementShardedCounterInput(BigInteger.TEN, false));
				this.doBasicAssertions(decrementShardedCounterResponse2, 201);
			}
			{
				////////////
				// Get the Counter

				final GetShardedCounterResponse response = client.getShardedCounter(counterName);
				assertThat(response.getHttpResponseCode(), is(200));

				this.doShardedCounterAssertions(response.getShardedCounter(), counterName, Optional.<String> absent(),
					3, CounterStatus.AVAILABLE);

				assertThat(response.getShardedCounter().getCount(), is(BigInteger.ZERO));
			}
		}
		catch (InstacountClientException e)
		{
			assertThat(e.getErrors().getHttpResponseCode(), is(400));
			throw e;
		}
	}

	//////////////////////////
	// Test Create Counter
	//////////////////////////

	@Test(expected = NullPointerException.class)
	public void testCreateCounter_WithPayload_Null() throws InstacountClientException
	{
		this.client.createShardedCounter(null);
	}

	@Test
	public void testCreateCounter() throws InstacountClientException
	{
		final CreateShardedCounterResponse actualResponse = this.client.createShardedCounter();

		this.doBasicAssertions(actualResponse, 201);
		final CounterLocationInfo counterInfo = actualResponse.getOptCounterInfo().get();
		assertThat(counterInfo.getCounterName(), is(not(nullValue())));
		assertThat(counterInfo.getLocationUrl().startsWith(Links.API_URL), is(true));
	}

	@Test
	public void testCreateCounter_WithPayload_WithName() throws InstacountClientException
	{
		final String counterName = UUID.randomUUID().toString();
		final CreateShardedCounterInput createCounterInput = new CreateShardedCounterInput(counterName);
		final CreateShardedCounterResponse actualResponse = this.client.createShardedCounter(createCounterInput);

		this.doBasicAssertions(actualResponse, 201);
		final CounterLocationInfo counterInfo = actualResponse.getOptCounterInfo().get();
		assertThat(counterInfo.getCounterName(), is(counterName));
		assertThat(counterInfo.getLocationUrl(), is(Links.API_URL + "/sharded_counters/" + counterName));

		this.doShardedCounterAssertions(client.getShardedCounter(counterName).getShardedCounter(), counterName,
			Optional.<String> absent(), 3, CounterStatus.AVAILABLE);
	}

	@Test
	public void testCreateCounter_WithPayload_WithNameAndDescription() throws InstacountClientException
	{
		final String counterName = UUID.randomUUID().toString();
		final String counterDescription = "Sample description from ClientTestHarness.java\"";
		final CreateShardedCounterInput createCounterInput = new CreateShardedCounterInput(counterName,
			counterDescription);
		final CreateShardedCounterResponse actualResponse = this.client.createShardedCounter(createCounterInput);

		this.doBasicAssertions(actualResponse, 201);
		final CounterLocationInfo counterInfo = actualResponse.getOptCounterInfo().get();
		assertThat(counterInfo.getCounterName(), is(counterName));
		assertThat(counterInfo.getLocationUrl(), is(Links.API_URL + "/sharded_counters/" + counterName));

		this.doShardedCounterAssertions(client.getShardedCounter(counterName).getShardedCounter(), counterName,
			Optional.of(counterDescription), 3, CounterStatus.AVAILABLE);
	}

	@Test
	public void testCreateCounter_WithPayload_WithExistingCounterName() throws InstacountClientException
	{
		final String counterName = UUID.randomUUID().toString();

		final CreateShardedCounterInput createCounterInput = new CreateShardedCounterInput(counterName);
		final CreateShardedCounterResponse actualResponse = this.client.createShardedCounter(createCounterInput);

		this.doBasicAssertions(actualResponse, 201);

		// Try again, expect an error!
		try
		{
			this.client.createShardedCounter(createCounterInput);
		}
		catch (InstacountClientException e)
		{
			assertThat(e.getErrors(), is(not(nullValue())));
			assertThat(e.getErrors().getHttpResponseCode(), is(409));
			assertThat(e.getErrors().getErrors(), is(not(nullValue())));
			assertThat(e.getErrors().getErrors().size(), is(1));
			final Error error = e.getErrors().getErrors().get(0);
			assertThat(error.getMessage(), is("This counter already exists!"));
			assertThat(error.getDeveloperMessage(), is("A sharded counter with the specified name already exists!"));
			assertThat(error.getMoreInfo(), is("https://instacount.readme.io"));
		}
	}

	@Test
	public void testCreateCounter_WithFullPayload() throws InstacountClientException
	{
		final String counterName = UUID.randomUUID().toString();
		final String counterDescription = "Sample description from ClientTestHarness.java\"";

		final CreateShardedCounterInput createCounterInput = new CreateShardedCounterInput(counterName,
			Optional.of(counterDescription), 3, ShardedCounterStatusInput.READ_ONLY_COUNT);

		final CreateShardedCounterResponse actualResponse = this.client.createShardedCounter(createCounterInput);

		this.doBasicAssertions(actualResponse, 201);
		final CounterLocationInfo counterInfo = actualResponse.getOptCounterInfo().get();
		assertThat(counterInfo.getCounterName(), is(counterName));
		assertThat(counterInfo.getLocationUrl(), is(Links.API_URL + "/sharded_counters/" + counterName));

		final ShardedCounter createdShardedCounter = this.client.getShardedCounter(counterName).getShardedCounter();

		// FIXME ref #143
		this.doShardedCounterAssertions(createdShardedCounter, counterName, Optional.of(counterDescription), 3,
			CounterStatus.READ_ONLY_COUNT);
	}

	//////////////////////////
	// Test Get Counter
	//////////////////////////

	@Test(expected = NullPointerException.class)
	public void testGetCounter_NullCounterName() throws InstacountClientException
	{
		client.getShardedCounter(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetCounter_EmptCounterName() throws InstacountClientException
	{
		client.getShardedCounter("");
	}

	@Test
	public void testGetCounter_NonExistentCounterName() throws InstacountClientException
	{
		try
		{
			final String counterName = UUID.randomUUID().toString();
			final CreateShardedCounterInput createCounterInput = new CreateShardedCounterInput(counterName);
			client.createShardedCounter(createCounterInput);
			client.getShardedCounter(counterName);
		}
		catch (InstacountClientException e)
		{
			this.doNotFoundAssertions(e.getErrors());
		}
	}

	@Test
	public void testGetCounter() throws InstacountClientException
	{
		final String counterName = UUID.randomUUID().toString();
		final CreateShardedCounterInput createCounterInput = new CreateShardedCounterInput(counterName);
		final CreateShardedCounterResponse createResponse = client.createShardedCounter(createCounterInput);

		final GetShardedCounterResponse shardedCounter = client
			.getShardedCounter(createResponse.getOptCounterInfo().get().getCounterName());

		this.doShardedCounterAssertions(shardedCounter.getShardedCounter(), counterName, Optional.<String> absent(), 3,
			CounterStatus.AVAILABLE);
	}

	//////////////////////////
	// Test Update Counter
	//////////////////////////

	@Test(expected = NullPointerException.class)
	public void testUpdateCounter_NullCounterName() throws InstacountClientException
	{
		client.updateShardedCounter(null, mock(UpdateShardedCounterInput.class));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUpdateCounter_EmptyCounterName() throws InstacountClientException
	{
		client.updateShardedCounter("", mock(UpdateShardedCounterInput.class));
	}

	@Test(expected = NullPointerException.class)
	public void testUpdateCounter_NullCounterInput() throws InstacountClientException
	{
		final String counterName = UUID.randomUUID().toString();
		client.updateShardedCounter(counterName, null);
	}

	@Test
	public void testUpdateCounter_NonExistentCounterName() throws InstacountClientException
	{
		try
		{
			final UpdateShardedCounterInput updateCounterInput = new UpdateShardedCounterInput("foo");
			client.updateShardedCounter("foo", updateCounterInput);
		}
		catch (InstacountClientException e)
		{
			this.doNotFoundAssertions(e.getErrors());
		}
	}

	@Test
	public void testUpdateCounter() throws InstacountClientException
	{
		final String counterName = UUID.randomUUID().toString();

		// First,create the counter...
		final CreateShardedCounterInput createCounterInput = new CreateShardedCounterInput(counterName,
			Optional.<String> absent(), 1, ShardedCounterStatusInput.AVAILABLE);
		client.createShardedCounter(createCounterInput);

		// Next, update all values, and assert the differences...
		final UpdateShardedCounterInput updateCounterInput = new UpdateShardedCounterInput(counterName,
			Optional.of("New Description"), 3, ShardedCounterStatusInput.READ_ONLY_COUNT);
		final UpdateShardedCounterResponse updatedShardedCounterResponse = client.updateShardedCounter(counterName,
			updateCounterInput);

		assertThat(updatedShardedCounterResponse.getHttpResponseCode(), is(200));
		final ShardedCounter updatedShardedCounter = updatedShardedCounterResponse.getShardedCounter();
		assertThat(updatedShardedCounter.getName(), is(counterName));
		assertThat(updatedShardedCounter.getCounterStatus(), is(CounterStatus.READ_ONLY_COUNT));
		assertThat(updatedShardedCounter.getOptDescription().get(), is("New Description"));

		// FIXME Ref #152.
		assertThat(updatedShardedCounter.getNumShards(), is(3));
	}

	//////////////////////////
	// Test Increment Counter
	//////////////////////////

	@Test(expected = NullPointerException.class)
	public void testIncrement_NullCounterName() throws InstacountClientException
	{
		client.incrementShardedCounter(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIncrement_EmptyCounterName() throws InstacountClientException
	{
		client.incrementShardedCounter("");
	}

	@Test(expected = NullPointerException.class)
	public void testIncrement_NullPayload() throws InstacountClientException
	{
		final String counterName = UUID.randomUUID().toString();
		client.incrementShardedCounter(counterName, null);
	}

	@Test
	public void testIncrement_NoPayload() throws InstacountClientException
	{
		final String counterName = UUID.randomUUID().toString();
		client.incrementShardedCounter(counterName);
		assertThat(client.getShardedCounter(counterName).getShardedCounter().getCount(), is(BigInteger.valueOf(1L)));
	}

	@Test
	public void testIncrement_Sync() throws InstacountClientException
	{
		final String counterName = UUID.randomUUID().toString();
		final IncrementShardedCounterResponse response = client.incrementShardedCounter(counterName,
			new IncrementShardedCounterInput(BigInteger.TEN, SYNC));
		assertThat(response.getOptCounterOperation().isPresent(), is(true));
		assertThat(response.getOptCounterOperation().get().getAmount(), is(BigInteger.TEN));
		assertThat(response.getOptCounterOperation().get().getCounterOperationType(),
			is(CounterOperationType.INCREMENT));
		assertThat(response.getOptCounterOperation().get().getShardIndex(), anyOf(is(0), is(1), is(2)));
		assertThat(response.getOptCounterOperation().get().getId(), is(not(nullValue())));

		// Get the counter and assert its value!
		assertThat(client.getShardedCounter(counterName).getShardedCounter().getCount(), is(BigInteger.TEN));
	}

	@Test
	public void testIncrement_Async() throws InstacountClientException
	{
		final String counterName = UUID.randomUUID().toString();
		final IncrementShardedCounterResponse response = client.incrementShardedCounter(counterName,
			new IncrementShardedCounterInput(BigInteger.TEN, ASYNC));

		assertThat(response.getHttpResponseCode(), is(202));
		assertThat(response.getOptCounterOperation().isPresent(), is(false));

		// Get the counter and assert its value!
		assertThat(client.getShardedCounter(counterName).getHttpResponseCode(), anyOf(is(200), is(404)));
	}

	//////////////////////////
	// Test Decrement Counter
	//////////////////////////

	@Test(expected = NullPointerException.class)
	public void testDecrement_NullCounterName() throws InstacountClientException
	{
		client.decrementShardedCounter(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDecrement_EmptyCounterName() throws InstacountClientException
	{
		client.decrementShardedCounter("");
	}

	@Test(expected = NullPointerException.class)
	public void testDecrement_NullPayload() throws InstacountClientException
	{
		final String counterName = UUID.randomUUID().toString();
		client.decrementShardedCounter(counterName, null);
	}

	@Test
	public void testDecrement_NoPayload() throws InstacountClientException
	{
		final String counterName = UUID.randomUUID().toString();
		client.decrementShardedCounter(counterName);
		assertThat(client.getShardedCounter(counterName).getShardedCounter().getCount(), is(BigInteger.valueOf(-1L)));
	}

	@Test
	public void testDecrement_Sync() throws InstacountClientException
	{
		final String counterName = UUID.randomUUID().toString();
		final DecrementShardedCounterResponse response = client.decrementShardedCounter(counterName,
			new DecrementShardedCounterInput(BigInteger.TEN, SYNC));
		assertThat(response.getOptCounterOperation().isPresent(), is(true));
		assertThat(response.getOptCounterOperation().get().getAmount(), is(BigInteger.TEN));
		assertThat(response.getOptCounterOperation().get().getCounterOperationType(),
			is(CounterOperationType.DECREMENT));
		assertThat(response.getOptCounterOperation().get().getShardIndex(), anyOf(is(0), is(1), is(2)));
		assertThat(response.getOptCounterOperation().get().getId(), is(not(nullValue())));

		// Get the counter and assert its value!
		assertThat(client.getShardedCounter(counterName).getShardedCounter().getCount(), is(BigInteger.valueOf(-10L)));
	}

	@Test
	public void testDecrement_Async() throws InstacountClientException
	{
		final String counterName = UUID.randomUUID().toString();
		final DecrementShardedCounterResponse response = client.decrementShardedCounter(counterName,
			new DecrementShardedCounterInput(BigInteger.TEN, ASYNC));

		assertThat(response.getHttpResponseCode(), is(202));
		assertThat(response.getOptCounterOperation().isPresent(), is(false));

		// Get the counter and assert its value!
		assertThat(client.getShardedCounter(counterName).getHttpResponseCode(), anyOf(is(200), is(404)));
	}

	//////////////////////////
	// Test Get CounterOperation
	//////////////////////////

	@Test(expected = NullPointerException.class)
	public void testGetCounterOperation_NullCounterName() throws InstacountClientException
	{
		client.getShardedCounterOperation(null, 0, UUID.randomUUID().toString());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetCounterOperation_EmptyCounterName() throws InstacountClientException
	{
		client.getShardedCounterOperation("", 0, UUID.randomUUID().toString());
	}

	@Test(expected = NullPointerException.class)
	public void testGetCounterOperation_NullShardIndex() throws InstacountClientException
	{
		final String counterName = UUID.randomUUID().toString();
		client.getShardedCounterOperation(counterName, null, UUID.randomUUID().toString());
	}

	@Test(expected = NullPointerException.class)
	public void testGetCounterOperation_NullOperationId() throws InstacountClientException
	{
		final String counterName = UUID.randomUUID().toString();
		client.getShardedCounterOperation(counterName, 1, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetCounterOperation_EmptyOperationId() throws InstacountClientException
	{
		final String counterName = UUID.randomUUID().toString();
		client.getShardedCounterOperation(counterName, 1, "");
	}

	@Test
	public void testGetCounterOperationIndex() throws InstacountClientException
	{
		// Create the ShardedCounter
		final CreateShardedCounterResponse result = client.createShardedCounter();
		final String counterName = result.getOptCounterInfo().get().getCounterName();

		// Increment the ShardedCounter
		final ShardedCounterOperation createdCounterOperation = client.incrementShardedCounter(counterName)
			.getOptCounterOperation().get();

		// Get the ShardedCounter from the API.
		final ShardedCounterOperation loadedCounterOperation = client.getShardedCounterOperation(counterName,
			createdCounterOperation.getShardIndex(), createdCounterOperation.getId()).getCounterOperation();

		// Assert that the ShardedCounterOperation returned from the create matches the value returned from the GET.
		this.doCounterOperationAssertions(createdCounterOperation, counterName, loadedCounterOperation.getId(),
			loadedCounterOperation.getShardIndex(), loadedCounterOperation.getCounterOperationType(),
			loadedCounterOperation.getAmount());
	}

	//////////////////
	// Private Helpers
	//////////////////

	private void doShardedCounterAssertions(final ShardedCounter actualShardedCounter, final String expectedCounterName,
			final Optional<String> optExpectedDescription, final int expectedNumShards,
			final CounterStatus expectedCounterStatus)
	{
		assertThat(actualShardedCounter.getMeta().getSelfLink().getHrefUrl().toString(),
			is(Links.API_URL + "/sharded_counters/" + expectedCounterName));
		assertThat(actualShardedCounter.getMeta().getIncrementsLink().getHrefUrl().toString(),
			is(Links.API_URL + "/sharded_counters/" + expectedCounterName + "/increments"));
		assertThat(actualShardedCounter.getMeta().getDecrementsLink().getHrefUrl().toString(),
			is(Links.API_URL + "/sharded_counters/" + expectedCounterName + "/decrements"));

		assertThat(actualShardedCounter.getName(), is(expectedCounterName));

		if (optExpectedDescription.isPresent())
		{
			assertThat(actualShardedCounter.getOptDescription().isPresent(), is(true));
			assertThat(actualShardedCounter.getOptDescription().get(), is(optExpectedDescription.get()));
		}
		else
		{
			assertThat(actualShardedCounter.getOptDescription().isPresent(), is(false));
		}

		assertThat(actualShardedCounter.getNumShards(), is(expectedNumShards));
		assertThat(actualShardedCounter.getCounterStatus(), is(expectedCounterStatus));
		// logger.info("Actual Creation: " + actualCounter.getCreatedDateTime());
		// logger.info("Expect Creation: " + expectedCounter.getCreatedDateTime());
		// assertThat(actualCounter.getCreatedDateTime().isAfter(expectedCounter.getCreatedDateTime()), is(true));
		assertThat(actualShardedCounter.getCreatedDateTime(), is(not(nullValue())));
	}

	private void doCounterOperationAssertions(final ShardedCounterOperation counterOperation,
			final String expectedCounterName, final String expectedOperationId, final int expectedShardIndex,
			final CounterOperationType expectedCounterOperationType, final BigInteger expectedAmount)
	{
		assertThat(counterOperation.getMeta().getSelfLink().getHrefUrl().toString(),
			is(Links.API_URL + "/sharded_counters/" + expectedCounterName + "/shards/" + expectedShardIndex
				+ "/operations/" + expectedOperationId));
		assertThat(counterOperation.getMeta().getCounterLink().getHrefUrl().toString(),
			is(Links.API_URL + "/sharded_counters/" + expectedCounterName));

		assertThat(counterOperation.getId(), is(expectedOperationId));
		assertThat(counterOperation.getShardIndex(), is(expectedShardIndex));
		assertThat(counterOperation.getCounterOperationType(), is(expectedCounterOperationType));
		assertThat(counterOperation.getAmount(), is(expectedAmount));
		assertThat(counterOperation.getCreatedDateTime(), is(not(nullValue())));
	}

	private void doBasicAssertions(final InstacountResponse instacountResponse, final int expectedHttpResponseCode)
	{
		assertThat(instacountResponse.getHttpResponseCode(), is(expectedHttpResponseCode));
		assertThat(instacountResponse.getQuota(), is(not(nullValue())));
		assertThat(instacountResponse.getQuota().getNumAccessRequestsLimit() > 0, is(true));
		assertThat(instacountResponse.getQuota().getNumMutationRequestsLimit() > 0, is(true));
		assertThat(instacountResponse.getQuota().getNumAccessRequestsRemaining() > 0, is(true));
		assertThat(instacountResponse.getQuota().getNumAccessRequestsRemaining() > 0, is(true));
		assertThat(instacountResponse.getResponse(), is(not(nullValue())));
	}

	private void doNotFoundAssertions(final Errors errors)
	{
		assertThat(errors, is(not(nullValue())));
		assertThat(errors.getHttpResponseCode(), is(404));
		assertThat(errors.getErrors(), is(not(nullValue())));
		assertThat(errors.getErrors().size(), is(1));
		final Error error = errors.getErrors().get(0);
		assertThat(error.getMessage(), is("What you requested does not exist."));
		assertThat(error.getDeveloperMessage(), is("The requested resource was not found!"));
		assertThat(error.getMoreInfo(), is("https://instacount.readme.io"));
	}
}
