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
package com.navercorp.volleyextensions.volleyer.builder;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.navercorp.volleyextensions.volleyer.VolleyerConfiguration;
import com.navercorp.volleyextensions.volleyer.http.HttpContent;
import com.navercorp.volleyextensions.volleyer.request.creator.RequestCreator;
import com.navercorp.volleyextensions.volleyer.request.executor.RequestExecutor;
import com.navercorp.volleyextensions.volleyer.response.parser.NetworkResponseParser;
import com.navercorp.volleyextensions.volleyer.util.Assert;
/**
 * A builder class that enables settings for response and that executes a request.
 * @author Wonjun Kim
 *
 * @param <T> Target class that content of a response will be parsed to
 */
public class ResponseBuilder<T> {
	
	private RequestQueue requestQueue;
	private VolleyerConfiguration configuration;
	private HttpContent httpContent;
	private Class<T> clazz;
	private Listener<T> listener;
	private ErrorListener errorListener;
	private NetworkResponseParser responseParser;

	private boolean isDoneToBuild = false;
	/**
	 * Default constructor for ResponseBuilder
	 * @param requestQueue running RequestQueue instance which will executes a request
	 * @param configuration VolleyerConfiguration instance. See {@link VolleyerConfiguration}.
	 * @param httpContent HttpContent instance which is previously set from {@code RequestBuilder}
	 * @param clazz Target class that content of a response will be parsed to.
	 */
	ResponseBuilder(RequestQueue requestQueue, VolleyerConfiguration configuration, HttpContent httpContent, Class<T> clazz) {
		Assert.notNull(requestQueue, "RequestQueue");
		Assert.notNull(configuration, "VolleyerConfiguration");
		Assert.notNull(httpContent, "HttpContent");
		Assert.notNull(clazz, "Target class token");

		this.requestQueue = requestQueue;
		this.configuration = configuration;
		this.httpContent = httpContent;
		this.clazz = clazz;
	}

	/**
	 * Set a listener for a response.
	 * @param listener volley listener
	 */
	public ResponseBuilder<T> withListener(Listener<T> listener) {
		assertFinishState();
		this.listener = listener;
		return this;
	}

	/**
	 * <pre>
	 * Set an error listener for a response.
	 * NOTE : If this is not set, volleyer sets up an ErrorListener instance
	 *        of {@code VolleyerConfiguration} as a fallback for a Request.
	 * </pre>
	 * @param errorListener volley error listener
	 */
	public ResponseBuilder<T> withErrorListener(ErrorListener errorListener) {
		assertFinishState();
		this.errorListener = errorListener;
		return this;
	}

	/**
	 * Check whether execution has been done. 
	 */
	private void assertFinishState() {
		if (isDoneToBuild == true) {
			throw new IllegalStateException("ResponseBuilder should not be used any more. Because execute() has been called.");
		}
	}

	/**
	 * Set a parser for which content of a response is converted to a target class.
	 * <pre>
	 * NOTE : If this is not set, volleyer sets up an NetworkResponseParser instance
	 *        of {@code VolleyerConfiguration} as a fallback for a Request.
	 * </pre>
	 * @param responseParser
	 */
	public ResponseBuilder<T> withResponseParser(NetworkResponseParser responseParser) {
		Assert.notNull(responseParser, "Response Parser");
		assertFinishState();
		this.responseParser = responseParser;
		return this;
	}

	/**
	 * Execute a request finally on a running RequestQueue.
	 * @return Request instance being executed
	 */
	public Request<T> execute() {
		setFallbackListenerIfNull();
		setFallbackErrorListenerIfNull();
		setFallbackResponseParserIfNull();

		Request<T> request = buildRequest();
		// Do not execute if request is null
		if (request == null) {
			return request;
		}
		executeRequest(request);
		markFinishState();
		return request;
	}

	/**
	 * Make this builder being disabled settings.
	 */
	protected final void markFinishState() {
		isDoneToBuild = true;
		// Let requestQueue be null for avoiding memory leak when this builder is referenced by some variable.
		requestQueue = null;
	}

	/**
	 * Set a empty listener if listener is not set.
	 */
	private void setFallbackListenerIfNull() {
		if (listener != null) {
			return;
		}
		listener = createEmptyListener();
	}

	private Listener<T> createEmptyListener() {
		return new Listener<T>() {

			@Override
			public void onResponse(Object response) {
			}
		};
	}

	private void setFallbackErrorListenerIfNull() {
		if (errorListener != null) {
			return;
		}
		errorListener = configuration.getFallbackErrorListener();
	}

	private void setFallbackResponseParserIfNull() {
		if (responseParser != null) {
			return;
		}

		responseParser = configuration.getFallbackNetworkResponseParser();
	}

	/**
	 * Create a {@code Request} object by {@link RequestCreator} of {@code VolleyerConfiguration}.
	 * @return Newly built Request object
	 */
	private Request<T> buildRequest() {
		RequestCreator requestCreator = configuration.getRequestCreator();
		return requestCreator.createRequest(httpContent, clazz, responseParser, listener, errorListener);
	}

	/**
	 * Execute a given request by {@link RequestExecutor} of {@code VolleyerConfiguration}.
	 */
	private void executeRequest(Request<T> request) {
		RequestExecutor executor = configuration.getRequestExecutor();
		executor.executeRequest(requestQueue, request);
	}
}
