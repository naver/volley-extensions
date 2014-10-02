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

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.navercorp.volleyextensions.volleyer.VolleyerConfiguration;
import com.navercorp.volleyextensions.volleyer.factory.DefaultVolleyerConfigurationFactory;
import com.navercorp.volleyextensions.volleyer.http.HttpMethod;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class RequestBuilderTest {
	RequestQueue requestQueue;

	@Before
	public void setUp() {
		requestQueue = mock(RequestQueue.class);
	}

	@Test(expected=NullPointerException.class)
	public void requestBuilderConstructorShouldThrowNpeWhenVolleyerConfigurationIsNull() {
		// Given
		String url = "test";
		HttpMethod method = HttpMethod.GET;
		VolleyerConfiguration nullVolleyerConfiguration = null;
		
		// When & Then
		new TestPurposeRequestBuilder(requestQueue, nullVolleyerConfiguration, url, method);
	}

	@Test(expected=NullPointerException.class)
	public void requestBuilderConstructorShouldThrowNpeWhenUrlIsNull() {
		// Given
		String nullUrl = null;
		HttpMethod method = HttpMethod.GET;
		VolleyerConfiguration configuration = DefaultVolleyerConfigurationFactory.create();
		
		// When & Then
		new TestPurposeRequestBuilder(requestQueue, configuration, nullUrl, method);
	}

	@Test(expected=NullPointerException.class)
	public void requestBuilderConstructorShouldThrowNpeWhenHttpMethodIsNull() {
		// Given
		String url = "test";
		HttpMethod nullMethod = null;
		VolleyerConfiguration configuration = DefaultVolleyerConfigurationFactory.create();
		
		// When & Then
		new TestPurposeRequestBuilder(requestQueue, configuration, url, nullMethod);
	}

	@Test(expected=NullPointerException.class)
	public void addHeaderMethodShouldThrowNpeWhenKeyIsNull() {
		// Given
		String nullKey = null;
		String value = "test";
		
		String url = "test";
		HttpMethod method = HttpMethod.GET;
		VolleyerConfiguration configuration = DefaultVolleyerConfigurationFactory.create();
		TestPurposeRequestBuilder builder = new TestPurposeRequestBuilder(requestQueue, configuration, url, method);
		
		// When & Then
		builder.addHeader(nullKey, value);
	}

	@Test(expected=NullPointerException.class)
	public void addHeaderMethodShouldThrowNpeWhenValueIsNull() {
		// Given
		String key = "test";
		String nullValue = null;
		
		String url = "test";
		HttpMethod method = HttpMethod.GET;
		VolleyerConfiguration configuration = DefaultVolleyerConfigurationFactory.create();
		TestPurposeRequestBuilder builder = new TestPurposeRequestBuilder(requestQueue, configuration, url, method);
		
		// When & Then
		builder.addHeader(key, nullValue);
	}

	@Test
	public void addHeaderMethodShouldReturnSameInstanceOfBuilder() {
		// Given
		String key = "testKey";
		String value = "testValue";
		
		String url = "test";
		HttpMethod method = HttpMethod.GET;
		VolleyerConfiguration configuration = DefaultVolleyerConfigurationFactory.create();
		TestPurposeRequestBuilder builder = new TestPurposeRequestBuilder(requestQueue, configuration, url, method);
		
		// When
		TestPurposeRequestBuilder newBuilder = builder.addHeader(key, value);
		// Then
		assertTrue(builder == newBuilder);
	}

	@Test
	public void afterRequestMethodShouldReturnAnActualInstance() {
		// Given
		String url = "test";
		HttpMethod method = HttpMethod.GET;
		VolleyerConfiguration configuration = DefaultVolleyerConfigurationFactory.create();
		TestPurposeRequestBuilder builder = new TestPurposeRequestBuilder(requestQueue, configuration, url, method);
		
		// When
		ResponseBuilder<String> responseBuilder = builder.withTargetClass(String.class);
		// Then
		assertNotNull(responseBuilder);
	}

	@Test(expected=IllegalStateException.class)
	public void addHeaderMethodShouldThrowIllegalStateExceptionWhenAfterRequestMethodIsAlreadyCalled() {
		// Given
		String key = "testKey";
		String value = "testValue";
		
		String url = "test";
		HttpMethod method = HttpMethod.GET;
		VolleyerConfiguration configuration = DefaultVolleyerConfigurationFactory.create();
		TestPurposeRequestBuilder builder = new TestPurposeRequestBuilder(requestQueue, configuration, url, method);
		
		// When & Then
		builder.withTargetClass(String.class);
		builder.addHeader(key, value);
	}
	@Test(expected = NullPointerException.class)
	public void withTargetClassMethodShouldThrowNpeWhenTargetClassIsNull() {
		// Given
		String key = "testKey";
		String value = "testValue";
		
		String url = "test";
		HttpMethod method = HttpMethod.GET;
		VolleyerConfiguration configuration = DefaultVolleyerConfigurationFactory.create();
		TestPurposeRequestBuilder builder = new TestPurposeRequestBuilder(requestQueue, configuration, url, method);
		Class<?> clazz = null;
		// When & Then
		builder.withTargetClass(clazz);
	}

	@Test
	public void withTargetClassMethodShouldReturnAnActualInstanceWhenTargetClassIsNotNull() {
		// Given
		String key = "testKey";
		String value = "testValue";
		
		String url = "test";
		HttpMethod method = HttpMethod.GET;
		VolleyerConfiguration configuration = DefaultVolleyerConfigurationFactory.create();
		TestPurposeRequestBuilder builder = new TestPurposeRequestBuilder(requestQueue, configuration, url, method);
		Class<String> clazz = String.class;
		// When
		ResponseBuilder<String> responseBuilder = builder.withTargetClass(clazz);
		// Then
		assertNotNull(responseBuilder);
	}

	@Test(expected = IllegalStateException.class)
	public void withTargetClassMethodShouldThrowIllegalStateExceptionWhenSetTargetClassMethodIsCalledAgain() {
		// Given
		String url = "test";
		HttpMethod method = HttpMethod.GET;
		VolleyerConfiguration configuration = DefaultVolleyerConfigurationFactory.create();
		TestPurposeRequestBuilder builder = new TestPurposeRequestBuilder(requestQueue, configuration, url, method);
		Class<String> clazz = String.class;
		// When
		builder.withTargetClass(clazz);
		// Then
		builder.withTargetClass(clazz);
	}	

	@Test()
	public void requestQueueShouldBeExecutedWhenExecuteMethodIsCalled() {
		// Given
		String url = "http://test";
		HttpMethod method = HttpMethod.GET;
		VolleyerConfiguration configuration = DefaultVolleyerConfigurationFactory.create();
		TestPurposeRequestBuilder builder = new TestPurposeRequestBuilder(requestQueue, configuration, url, method);
		// When
		Request<Void> request = builder.execute();
		// Then
		verify(requestQueue).add(request);
	}

	@Test(expected = IllegalStateException.class)
	public void requestBuilderShouldThrowIllegalStateExceptionWhenExecuteMethodIsCalledAgain() {
		// Given
		String url = "http://test";
		HttpMethod method = HttpMethod.GET;
		VolleyerConfiguration configuration = DefaultVolleyerConfigurationFactory.create();
		TestPurposeRequestBuilder builder = new TestPurposeRequestBuilder(requestQueue, configuration, url, method);
		// When
		builder.execute();
		// Then
		builder.execute();
	}

	@Test(expected = IllegalStateException.class)
	public void requestBuilderShouldThrowIllegalStateExceptionWhensetListenerMethodIsCalledAgain() {
		// Given
		String url = "http://test";
		HttpMethod method = HttpMethod.GET;
		VolleyerConfiguration configuration = DefaultVolleyerConfigurationFactory.create();
		TestPurposeRequestBuilder builder = new TestPurposeRequestBuilder(requestQueue, configuration, url, method);
		Listener<String> listener = new Listener<String>(){

			@Override
			public void onResponse(String response) {
			}};
		// When
		builder.withListener(listener);
		// Then
		builder.withListener(listener);
	}

	@Test(expected = IllegalStateException.class)
	public void requestBuilderShouldThrowIllegalStateExceptionWhensetErrorListenerMethodIsCalledAgain() {
		// Given
		String url = "http://test";
		HttpMethod method = HttpMethod.GET;
		VolleyerConfiguration configuration = DefaultVolleyerConfigurationFactory.create();
		TestPurposeRequestBuilder builder = new TestPurposeRequestBuilder(requestQueue, configuration, url, method);
		ErrorListener errorListener = new ErrorListener(){

			@Override
			public void onErrorResponse(VolleyError error) {
			}};
		// When
		builder.withErrorListener(errorListener);
		// Then
		builder.withErrorListener(errorListener);
	}
}
