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
package com.navercorp.volleyextensions.request;

import java.io.UnsupportedEncodingException;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.navercorp.volleyextensions.util.Assert;
/**
 * <pre>
 * A Request{@literal <T>} for which make a request and convert response data by an converter.  
 * For converting response data, you have to override this class and fill the codes of type conversion in {@link #parseNetworkResponse}().
 * </pre>
 * @param <T> Specific type of an converted object from response data
 */
public abstract class AbstractConverterRequest<T> extends Request<T> {
	private final Class<T> clazz;
	private final Listener<T> listener;
	
	private static final int DEFAULT_METHOD = Method.GET;
	private static final ErrorListener EMPTY_ERROR_LISTENER = new ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError error) {
		}
	};

	/**
	 * 
	 * @param method HTTP method. See here : {@link com.android.volley.Request.Method}
	 * @param url URL of the request to make
	 * @param clazz Specific type object of an converted object from response data
	 * @param listener listener for response
	 * @param errorListener listener for errors
	 * @throws NullPointerException if {@code clazz} is null or {@code listener} is null
	 */
	public AbstractConverterRequest(int method, String url, Class<T> clazz,
			Listener<T> listener, ErrorListener errorListener) {
		super(method, url, errorListener);
		Assert.notNull(clazz, "class");
		Assert.notNull(listener, "listener");
		this.clazz = clazz;
		this.listener = listener;
	}
	
	/**
	 * 
	 * @param url URL of the request to make
	 * @param clazz Specific type object of an converted object from response data
	 * @param listener listener for response
	 * @param errorListener listener for errors
	 */
	public AbstractConverterRequest(String url, Class<T> clazz,
			Listener<T> listener, ErrorListener errorListener) {
		this(DEFAULT_METHOD, url, clazz, listener, errorListener);
	}
	
	/**
	 * 
	 * @param url URL of the request to make
	 * @param clazz Specific type object of an converted object from response data
	 * @param listener listener for response
	 */
	public AbstractConverterRequest(String url, Class<T> clazz,
			Listener<T> listener) {
		this(url, clazz, listener, EMPTY_ERROR_LISTENER);
	}



	@Override
	protected void deliverResponse(T result) {
		listener.onResponse(result);
	}
	/**
	 * @return Specific type object of an converted object from response data
	 */
	protected Class<T> getTargetClass() {
		return this.clazz;
	}
	/**
	 * Convert response data to {@code String}
	 * @param response
	 * @return String which has been converted
	 * @throws UnsupportedEncodingException
	 */
	protected final String getBodyString(NetworkResponse response) throws UnsupportedEncodingException {
		return new String(response.data, HttpHeaderParser.parseCharset(response.headers));
	}

	abstract protected Response<T> parseNetworkResponse(NetworkResponse response);
}
