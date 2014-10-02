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
package com.navercorp.volleyextensions.volleyer.request;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.navercorp.volleyextensions.volleyer.http.HttpContent;
import com.navercorp.volleyextensions.volleyer.http.HttpMethod;
import com.navercorp.volleyextensions.volleyer.response.parser.JacksonNetworkResponseParser;
import com.navercorp.volleyextensions.volleyer.response.parser.NetworkResponseParser;
import com.navercorp.volleyextensions.volleyer.response.parser.StringNetworkResponseParser;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class VolleyerRequestTest {

	final String url = "http://test";
	final HttpMethod httpMethod = HttpMethod.GET;
	final Listener<String> listener = new Listener<String>(){

		@Override
		public void onResponse(String response) {
		}};

	final ErrorListener errorListener = new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
		}};

	final NetworkResponseParser responseParser = new StringNetworkResponseParser();
	final Class<String> clazz = String.class;

	HttpContent httpContent = null;

	@Before
	public void setUp() {
		httpContent = new HttpContent(url , httpMethod);
	}

	@Test(expected = NullPointerException.class)
	public void constructorShouldThrowNpeWhenHttpContentIsNull() {
		// Given
		HttpContent nullHttpContent = null;
		// Then
		new VolleyerRequest<String>(nullHttpContent, clazz, responseParser, listener, errorListener);
	}

	@Test(expected = NullPointerException.class)
	public void constructorShouldThrowNpeWhenTargetClassIsNull() {
		// Given
		Class<String> nullClazz = null;
		// Then
		new VolleyerRequest<String>(httpContent, nullClazz, responseParser, listener, errorListener);
	}

	@Test(expected = NullPointerException.class)
	public void constructorShouldThrowNpeWhenListenerIsNull() {
		// Given
		Listener<String> nullListener = null;
		// Then
		new VolleyerRequest<String>(httpContent, clazz, responseParser, nullListener, errorListener);
	}

	@Test(expected = NullPointerException.class)
	public void constructorShouldThrowNpeWhenResponseParserIsNull() {
		// Given
		NetworkResponseParser nullResponseParser = null;
		// Then
		new VolleyerRequest<String>(httpContent, clazz, nullResponseParser, listener, errorListener);
	}

	@Test
	public void httpMethodShouldBeSameWithThatOfHttpContent() {
		// When
		Request<String> request = new VolleyerRequest<String>(httpContent, clazz, responseParser, listener, errorListener);
		// Then
		assertEquals(httpContent.getMethod().getMethodCode(), request.getMethod());
	}

	@Test
	public void bodyShouldBeSameWithThatOfHttpContent() {
		// Given
		byte[] body = {'h', 'e', 'l', 'l', 'o'};
		httpContent.setBody(body);
		// When
		Request<String> request = new VolleyerRequest<String>(httpContent, clazz, responseParser, listener, errorListener);
		// Then
		try {
			assertEquals(httpContent.getBody(), request.getBody());
		} catch (AuthFailureError e) {
			e.printStackTrace();
		}
	}

	@Test
	public void headersShouldBeSameWithThatOfHttpContent() {
		// Given
		httpContent.addHeader("hello", "world");
		httpContent.addHeader("volley", "extensions");
		// When
		Request<String> request = new VolleyerRequest<String>(httpContent, clazz, responseParser, listener, errorListener);
		// Then
		try {
			assertEquals(httpContent.getHeaders(), request.getHeaders());
		} catch (AuthFailureError e) {
			e.printStackTrace();
		}
	}

	@Test
	public void targetClassShouldBeSameWithParameterOfConstructor() {
		// When
		VolleyerRequest<String> request = new VolleyerRequest<String>(httpContent, clazz, responseParser, listener, errorListener);
		// Then
		assertEquals(clazz, request.getTargetClass());
	}

	@Test
	public void responseShouldBeSuccessOnNormal() {
		// Given
		String content = "test response";
		NetworkResponse networkResponse = new NetworkResponse(content.getBytes());
		// When
		VolleyerRequest<String> request = new VolleyerRequest<String>(httpContent, clazz, responseParser, listener, errorListener);
		Response<String> response = request.parseNetworkResponse(networkResponse);
		// Then
		assertThat(response.isSuccess(), is(true));
		assertThat(response.result, is(content));
	}

	@Test
	public void responseShouldFailWhenNetworkResponseParserIsWrong() {
		// Given
		NetworkResponseParser otherResponseParser = new JacksonNetworkResponseParser();
		String content = "test response";
		NetworkResponse networkResponse = new NetworkResponse(content.getBytes());
		// When
		VolleyerRequest<String> request = new VolleyerRequest<String>(httpContent, clazz, otherResponseParser, listener, errorListener);
		Response<String> response = request.parseNetworkResponse(networkResponse);
		// Then
		assertThat(response.isSuccess(), is(false));
	}

	@Test
	public void responseShouldBeDeliveredWhenDeliveredResponseIsCalled() {
		// Given
		String content = "test response";
		TestListener<String> testListener = new TestListener<String>(content);
		// When
		VolleyerRequest<String> request = new VolleyerRequest<String>(httpContent, clazz, responseParser, testListener, errorListener);
		request.deliverResponse(content);
		// Then
		assertThat(testListener.isDelivered(), is(true));
	}

	/**
	 * This class is just for tests.
	 */
	class TestListener<T> implements Listener<T> {

		boolean isDelivered = false;
		private T content;

		public TestListener(T content) {
			this.content = content;
		}

		@Override
		public void onResponse(T response) {
			isDelivered = content.equals(response);
		}

		public boolean isDelivered() {
			return isDelivered;
		}
	}
}
