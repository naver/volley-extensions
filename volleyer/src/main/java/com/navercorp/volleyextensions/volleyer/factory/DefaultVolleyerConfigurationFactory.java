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

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.navercorp.volleyextensions.volleyer.VolleyerConfiguration;
import com.navercorp.volleyextensions.volleyer.request.creator.DefaultRequestCreator;
import com.navercorp.volleyextensions.volleyer.request.creator.RequestCreator;
import com.navercorp.volleyextensions.volleyer.request.executor.DefaultRequestExecutor;
import com.navercorp.volleyextensions.volleyer.request.executor.RequestExecutor;
import com.navercorp.volleyextensions.volleyer.response.parser.*;
import com.navercorp.volleyextensions.volleyer.util.VolleyerLog;
/**
 * A factory which creates a {@code VolleyerConfiguration} instance without any settings.
 */
public class DefaultVolleyerConfigurationFactory {
	/**
	 * Default VolleyerConfiguration is immutable and properties of it are immutable too.
	 * Internally, DefaultVolleyerConfigurationFactory keeps default VolleyerConfiguration as singleton.
	 * 
	 * @author Wonjun Kim
	 *
	 */
	private static class ConfigurationHolder {
		private final static VolleyerConfiguration configuration;
		static {
			RequestCreator requestCreator = createRequestCreator();
			RequestExecutor requestExecutor = createRequestExecutor();
			NetworkResponseParser networkResponseParser = createNetworkResponseParser();
			ErrorListener errorListener = createErrorListener();
			configuration = new VolleyerConfiguration(requestCreator, requestExecutor, networkResponseParser, errorListener);
		}

		private static VolleyerConfiguration getConfiguration() {
			return configuration;
		}
	}
	private static ErrorListener defaultErrorListener = new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			VolleyerLog.debug(error, "ERROR : ");
		}};

	public static NetworkResponseParser createNetworkResponseParser() {
		return DefaultNetworkResponseParserFactory.create();
	}

	public static RequestCreator createRequestCreator() {
		return new DefaultRequestCreator();
	}

	public static RequestExecutor createRequestExecutor() {
		return new DefaultRequestExecutor();
	}

	/**
	 * Create a default error listener.
	 * (But, This method just returns the same instance actually.)
	 * @return default error listener
	 */
	public static ErrorListener createErrorListener() {
		return defaultErrorListener;
	}

	/**
	 * Create a VolleyerConfiguration instance which includes default implementations.
	 * @return VolleyerConfiguration instance
	 */
	public static VolleyerConfiguration create() {
		return ConfigurationHolder.getConfiguration();
	}

}
