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
package io.instacount.client.jackson;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.sappenin.utils.json.jackson.mappers.modules.HttpUrlModule;

/**
 * An extension of {@link ObjectMapper} for configuring Jackson with the API.
 */
public class InstacountClientObjectMapper extends ObjectMapper
{
	/**
	 * No-args Constructor.
	 * 
	 * @see "https://github.com/FasterXML/jackson-datatype-joda"
	 */
	public InstacountClientObjectMapper()
	{
		registerModule(new HttpUrlModule());
		registerModule(new JodaModule());
		registerModule(new GuavaModule());
		configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		setDateFormat(new ISO8601DateFormat());
	}
}
