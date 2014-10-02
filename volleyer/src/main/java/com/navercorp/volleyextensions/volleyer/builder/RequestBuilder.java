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
import com.navercorp.volleyextensions.volleyer.VolleyerConfiguration;
import com.navercorp.volleyextensions.volleyer.http.HttpContent;
import com.navercorp.volleyextensions.volleyer.http.HttpMethod;
import com.navercorp.volleyextensions.volleyer.util.Assert;
/**
 * <pre>
 * Builder class which can set request headers or a Listener, a ErrorListener of Volley.
 * NOTE : You have to override this class for extending a builder, or for customizing some operations of a builder.
 * For seeing example, see {@link BodyBuilder}.
 * 
 * </pre>  
 * @author Wonjun Kim
 *
 * @param <B> Sub builder of RequestBuilder 
 */
abstract class RequestBuilder<B extends RequestBuilder<B>> {
	private RequestQueue requestQueue;
	private final VolleyerConfiguration configuration;
	protected final HttpContent httpContent;

	protected boolean isDoneToBuild = false;
	/**
	 * Default constructor for a new request to be created.
	 *
	 * @param requestQueue running RequestQueue instance which will executes a request
	 * @param configuration VolleyerConfiguration instance. See {@link VolleyerConfiguration} 
	 * @param url Url string like as 'https://...'
	 * @param method Http method of a request
	 */
	public RequestBuilder(RequestQueue requestQueue, VolleyerConfiguration configuration, String url, HttpMethod method) {
		Assert.notNull(requestQueue, "RequestQueue");
		Assert.notNull(configuration, "VolleyerConfiguration");

		this.requestQueue = requestQueue;
		this.configuration = configuration;
		httpContent = new HttpContent(url, method);
	}
	/**
	 * <pre>
	 * Add a request header.
	 * </pre>
	 * @param key key string of a header 
	 * @param value value string of a header
	 */
	@SuppressWarnings("unchecked")
	public B addHeader(String key, String value) {
		assertFinishState();

		httpContent.addHeader(key, value);
		return (B) this;
	}

	/**
	 * <pre>
	 * Throws error when the object is used again.
	 * </pre>
	 */
	protected final void assertFinishState() {
		if (isDoneToBuild == true) {
			throw new IllegalStateException("RequestBuilder should not be used any more. Because afterRequest() has been called.");
		}
	}
	/**
	 * Set a target class that content of a response will be parsed to.
	 * @param clazz Target class, must not be null.
	 * @return ResponseBuilder for response settings
	 */
	public <T> ResponseBuilder<T> withTargetClass(Class<T> clazz) {
		Assert.notNull(clazz, "Target Class token");

		assertFinishState();

		ResponseBuilder<T> builder = new ResponseBuilder<T>(requestQueue, configuration, httpContent, clazz);
		markFinishState();
		return builder;
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
	 * Set a listener and go to response settings without any request settings.
	 * @param listener volley listener
	 * @return ResponseBuilder for response settings
	 */
	public ResponseBuilder<String> withListener(Listener<String> listener) {
		assertFinishState();
		ResponseBuilder<String> builder = withTargetClass(String.class);
		builder.withListener(listener);
		return builder;
	}
	/**
	 * Set a error listener and go to settings for response({@link @ResponseBuilder}) without any request settings.
	 * @param errorListener volley error listener
	 * @return ResponseBuilder for response settings
	 */
	public ResponseBuilder<String> withErrorListener(ErrorListener errorListener) {
		assertFinishState();
		ResponseBuilder<String> builder = withTargetClass(String.class);
		builder.withErrorListener(errorListener);
		return builder;
	}
	/**
	 * Execute a request immediately without any settings.
	 * @return Request instance being executed
	 */
	public Request<Void> execute() {
		assertFinishState();
		ResponseBuilder<Void> builder = withTargetClass(Void.class);
		return builder.execute();
	}

}
