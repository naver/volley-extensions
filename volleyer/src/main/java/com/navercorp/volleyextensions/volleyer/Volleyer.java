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
package com.navercorp.volleyextensions.volleyer;

import java.util.Map;
import java.util.WeakHashMap;

import com.android.volley.RequestQueue;
import com.navercorp.volleyextensions.volleyer.builder.*;
import com.navercorp.volleyextensions.volleyer.factory.DefaultVolleyerConfigurationFactory;
import com.navercorp.volleyextensions.volleyer.util.Assert;
import com.navercorp.volleyextensions.volleyer.util.VolleyerLog;

/**
 * Volleyer helps you to create a request and execute it.
 *
 * <pre>
 * You can use this class like below,
 *
 *
 * {@code import static com.navercorp.volleyextensions.volleyer.Volleyer.*;
 * ...
 * volleyer(requestQueue).get(url).execute();
 * }
 *
 *
 * Also you can omit requestQueue parameter when calling {@code volleyer()} method
 * if this property is set by adding code like below,
 *
 *
 * {@code volleyer(requestQueue).settings()
 * 			.setAsDefault()
 * 			.done();
 * ...
 * volleyer().get(url).execute();
 * }
 *
 *
 * <b>NOTE</b> : RequestQueue must be already started before using volleyer. Volleyer doesn't control RequestQueue.
 * <b>WARNING</b> : Don't make a direct reference to a volleyer instance. ex) <b>{@code Volleyer someVolleyer = volleyer(requestQueue);}</b>
 * 	It may causes that RequestQueue remains on memory even if it isn't necessary any more.
 * 	This is because each volleyer contains a reference to {@code RequestQueue}.
 * </pre>
 * @author Wonjun Kim
 *
 */
public class Volleyer {
	/**
	 * A property of default volleyer.
	 */
	private static volatile Volleyer defaultVolleyer = new DummyVolleyer();
	/**
	 * <pre>
	 * A container which contains {@code Volleyer}s that each {@code Volleyer} uses {@link RequestQueue}.
	 * If there is no references to RequestQueue, a volleyer of it will be removed from the container.
	 * The container must be used with thread safety.
	 * </pre>
	 */
	private static final Map<RequestQueue, Volleyer> volleyers = new WeakHashMap<RequestQueue, Volleyer>();
	/**
	 * A RequestQueue instance which is used by {@code Volleyer}.
	 */
	private final RequestQueue requestQueue;
	/**
	 * A VolleyerConfiguration instance which is used by {@code Volleyer}.
	 */
	private volatile VolleyerConfiguration configuration;

	/**
	 * Default Constructor
	 * @param requestQueue
	 */
	Volleyer(RequestQueue requestQueue) {
		this.requestQueue = requestQueue;
		this.configuration = DefaultVolleyerConfigurationFactory.create();
	}

	/**
	 * Return a volleyer instance which uses default RequestQueue.
	 * @return volleyer instance which can make a request and execute it,
	 * 			or dummy instance which throws exception when calling methods
	 * 			if default volleyer instance is not set.
	 *
	 */
	public static Volleyer volleyer() {
		logErrorIfDefaultNotSet();
		return defaultVolleyer;
	}

	private static void logErrorIfDefaultNotSet() {
		if(isDefaultVolleyerDummy()) {
			VolleyerLog.error("Default volleyer is not set. You have to set the default volleyer as first like this code. : "
					+ "\n"
					+ "volleyer(requestQueue).settings().setAsDefault().done(); ");
		}
	}

	private static boolean isDefaultVolleyerDummy() {
		return DummyVolleyer.class == defaultVolleyer.getClass();
	}

	/**
	 * Return a volleyer instance which uses {@code RequestQueue}.
	 * @param requestQueue instance which will execute a request.
	 * @return volleyer instance which can make a request and execute it.
	 */
	public static synchronized Volleyer volleyer(RequestQueue requestQueue) {
		Assert.notNull(requestQueue, "RequestQueue");

		Volleyer volleyer = volleyers.get(requestQueue);

		if(volleyer != null) {
			return volleyer;
		}

		// Create new Volleyer if it doesn't exist yet.
		volleyer = new Volleyer(requestQueue);
		volleyers.put(requestQueue, volleyer);
		return volleyer;
	}

	/**
	 * Start to make a GET request!
	 * @param url Url string. ex) "http://google.co.kr/"
	 * @return Builder which can make a request by using method chaining style.
	 */
	public GetBuilder get(String url) {
		return new GetBuilder(requestQueue, configuration, url);
	}

	/**
	 * Start to make a POST request!
	 * @param url Url string. ex) "http://google.co.kr/"
	 * @return Builder which can make a request by using method chaining style.
	 */
	public PostBuilder post(String url) {
		return new PostBuilder(requestQueue, configuration, url);
	}

	/**
	 * Start to make a PUT request!
	 * @param url Url string. ex) "http://google.co.kr/"
	 * @return Builder which can make a request by using method chaining style.
	 */
	public PutBuilder put(String url) {
		return new PutBuilder(requestQueue, configuration, url);
	}

	/**
	 * Start to make a DELETE request!
	 * @param url Url string. ex) "http://google.co.kr/"
	 * @return Builder which can make a request by using method chaining style.
	 */
	public DeleteBuilder delete(String url) {
		return new DeleteBuilder(requestQueue, configuration, url);
	}

	/**
	 * Provides settings of this volleyer
	 * @return New {@link Settings} instance
	 */
	public Settings settings() {
		return new Settings();
	}

	/**
	 * <pre>
	 * Helper class which apply new settings to the volleyer instance.
	 * Users can use this class like below.
	 * {@code volleyer(requestQueue).settings()
	 * 			.setConfiguration(configuration)
	 * 			.setAsDefault()
	 * 			.done(); // Apply the settings to the volleyer of requestQueue.
	 * }
	 * </pre>
	 *
	 * @author Wonjun Kim
	 *
	 */
	public class Settings {
		private Settings() {}
		private VolleyerConfiguration configuration;
		private boolean isDefaultVolleyer = false;

		/**
		 * Set a custom VolleyerConfiguration.
		 * @param configuration Custom VolleyerConfiguration
		 * @return this Settings instance.
		 */
		public Settings setConfiguration(VolleyerConfiguration configuration) {
			Assert.notNull(configuration, "VolleyerConfiguration");
			this.configuration = configuration;
			return this;
		}

		/**
		 * <pre>
		 * Set this volleyer as default.
		 * After this is set, 'volleyer()' method returns this volleyer instance.
		 * </pre>
		 * @return this Settings instance.
		 */
		public Settings setAsDefault() {
			isDefaultVolleyer = true;
			return this;
		}

		/**
		 * Apply new settings!
		 */
		public void done() {
			setConfigurationToVolleyerIfNotNull();
			setDefaultVolleyerIfTrue();
		}

		private void setConfigurationToVolleyerIfNotNull() {
			if(configuration == null) {
				return;
			}

			Volleyer.this.configuration = configuration;
		}

		private void setDefaultVolleyerIfTrue() {
			if(isDefaultVolleyer == false) {
				return;
			}

			Volleyer.defaultVolleyer = Volleyer.this;
		}

	}

}
