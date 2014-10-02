/*
 * Copyright (C) 2014 Naver Corp.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.navercorp.volleyextensions.volleyer.factory;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.navercorp.volleyextensions.volleyer.VolleyerConfiguration;
import com.navercorp.volleyextensions.volleyer.factory.DefaultVolleyerConfigurationFactory;
import com.navercorp.volleyextensions.volleyer.request.creator.DefaultRequestCreator;
import com.navercorp.volleyextensions.volleyer.request.executor.DefaultRequestExecutor;
import com.navercorp.volleyextensions.volleyer.response.parser.IntegratedNetworkResponseParser;

public class DefaultVolleyerConfigurationFactoryTest {

	@Test
	public void volleyerConfigurationShouldSameInstance() {
		// When
		VolleyerConfiguration configuration = DefaultVolleyerConfigurationFactory.create();
		VolleyerConfiguration otherConfiguration = DefaultVolleyerConfigurationFactory.create();
		// Then
		assertThat(configuration, is(otherConfiguration));
	}

	@Test
	public void volleyerConfigurationShouldContain() {
		// When
		VolleyerConfiguration configuration = DefaultVolleyerConfigurationFactory.create();
		// Then
		assertEquals(configuration.getFallbackNetworkResponseParser().getClass(), IntegratedNetworkResponseParser.class);
		assertEquals(configuration.getFallbackErrorListener(), DefaultVolleyerConfigurationFactory.createErrorListener());
		assertEquals(configuration.getRequestCreator().getClass(), DefaultRequestCreator.class);
		assertEquals(configuration.getRequestExecutor().getClass(), DefaultRequestExecutor.class);
	}
}
