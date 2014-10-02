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

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.DeserializationConfig;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.navercorp.volleyextensions.util.Assert;

/**
 * A Request{@literal <T>} for which make a request and convert response data by Jackson 1.9.x ObjectMapper.   
 * @param <T> Specific type of an converted object from response data
 * 
 * @see AbstractConverterRequest
 * @see Request
 */
public class JacksonRequest<T> extends AbstractConverterRequest<T> {
	/** Default {@link ObjectMapper} is singleton. */
	private static class ObjectMapperHolder {
		private static final ObjectMapper objectMapper;
		static {
			objectMapper = new ObjectMapper();
			// ignore unknown json properties
			objectMapper.configure(
					DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);
			// allow unquoted control characters
			objectMapper.configure(
					JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
		}

		private static ObjectMapper defaultObjectMapper() {
			return objectMapper;
		}
	}
	/** {@code objectMapper} is immutable(but not severely). */
	private final ObjectMapper objectMapper;
	
	/**
	 * Make a GET request 
	 * @param url URL of the request to make
	 * @param clazz Specific type object of an converted object from response data
	 * @param listener listener for response
	 */
	public JacksonRequest(String url, Class<T> clazz, Listener<T> listener) {
		this(url, clazz, ObjectMapperHolder.defaultObjectMapper(), listener);
	}
	/**
	 * Make a GET request with custom {@code objectMapper}
	 * @param url URL of the request to make
	 * @param clazz Specific type object of an converted object from response data
	 * @param objectMapper {@link ObjectMapper} to convert
	 * @param listener listener for response
	 */
	public JacksonRequest(String url, Class<T> clazz, ObjectMapper objectMapper, Listener<T> listener) {
		super(url, clazz, listener);
		assertObjectMapper(objectMapper);
		this.objectMapper = objectMapper;
	}
	/**
	 * Make a GET request with {@code errorListener}
	 * @param url URL of the request to make
	 * @param clazz Specific type object of an converted object from response data
	 * @param listener listener for response
	 * @param errorListener listener for errors
	 */
	public JacksonRequest(String url, Class<T> clazz, Listener<T> listener,
			ErrorListener errorListener) {
		this(url, clazz, ObjectMapperHolder.defaultObjectMapper(), listener, errorListener);
	}
	/**
	 * Make a request with custom {@code objectMapper} and {@code errorListener}
	 * @param url URL of the request to make
	 * @param clazz Specific type object of an converted object from response data
	 * @param objectMapper {@link ObjectMapper} to convert
	 * @param listener listener for response
	 * @param errorListener listener for errors
	 */
	public JacksonRequest(String url, Class<T> clazz, ObjectMapper objectMapper, Listener<T> listener, ErrorListener errorListener) {
		super(url, clazz, listener, errorListener);
		assertObjectMapper(objectMapper);
		this.objectMapper = objectMapper;
	}
	/**
	 * Make a request with {@code errorListener}
	 * @param method HTTP method. See here : {@link com.android.volley.Request.Method} 
	 * @param url URL of the request to make
	 * @param clazz Specific type object of an converted object from response data
	 * @param listener listener for response
	 * @param errorListener listener for errors
	 */
	public JacksonRequest(int method, String url, Class<T> clazz, Listener<T> listener, ErrorListener errorListener) {
		this(method, url, clazz, ObjectMapperHolder.defaultObjectMapper(), listener, errorListener);
	}	
	/**
	 * Make a request with custom {@code objectMapper} and {@code errorListener}
	 * @param method HTTP method. See here : {@link com.android.volley.Request.Method} 
	 * @param url URL of the request to make
	 * @param clazz Specific type object of an converted object from response data
	 * @param objectMapper {@link ObjectMapper} to convert
	 * @param listener listener for response
	 * @param errorListener listener for errors
	 */
	public JacksonRequest(int method, String url, Class<T> clazz, ObjectMapper objectMapper, Listener<T> listener, ErrorListener errorListener) {
		super(method, url, clazz, listener, errorListener);
		assertObjectMapper(objectMapper);
		this.objectMapper = objectMapper;
	}	

	private final void assertObjectMapper(ObjectMapper objectMapper) {
		Assert.notNull(objectMapper, "objectMapper");
	}

	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {
		try {
			T result = objectMapper.readValue(getBodyString(response),
					getTargetClass());
			return Response.success(result,
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (JsonParseException e) {
			return Response.error(new ParseError(e));
		} catch (JsonMappingException e) {
			return Response.error(new ParseError(e));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (IOException e) {
			return Response.error(new VolleyError(e));
		} catch (Exception e) {
			return Response.error(new VolleyError(e));
		}
	}
}
