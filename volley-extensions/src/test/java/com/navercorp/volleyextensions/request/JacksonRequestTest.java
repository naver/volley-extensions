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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.apache.http.protocol.HTTP;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.exc.UnrecognizedPropertyException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.navercorp.volleyextensions.mock.ErrorResponseHoldListener;
import com.navercorp.volleyextensions.mock.ResponseHoldListener;
import com.navercorp.volleyextensions.request.JacksonRequest;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class JacksonRequestTest {
	String url = "http://localhost";
	ResponseHoldListener<News> listener = new ResponseHoldListener<News>();
	ErrorResponseHoldListener errorListener = new ErrorResponseHoldListener();

	@BeforeClass
	public static void setUpOnce() throws Exception {
		ShadowLog.stream = System.out;
	}

	@Test
	public void networkResponseShouldBeParsed() throws JsonProcessingException {
		// Given
		String content = "{\"imageUrl\":\"http://static.naver.com/volley-ext.jpg\"," +
						   "\"title\":\"Volley extention has released\"}";
		NetworkResponse networkResponse = new NetworkResponse(content.getBytes());
		JacksonRequest<News> request = new JacksonRequest<News>(url, News.class, listener);
		// When
		Response<News> response = request.parseNetworkResponse(networkResponse);
		// Then
		News news = response.result;
		assertThat(news.imageUrl, is("http://static.naver.com/volley-ext.jpg"));
		assertThat(news.title, is("Volley extention has released"));
	}
	
	@Test
	public void networkResponseShouldBeParsedWithAddtionalAttribute() throws JsonProcessingException {
		// Given
		String content = "{\"imageUrl\":\"http://static.naver.com/volley-ext.jpg\"," +
						   "\"title\":\"Volley extention has released\"," +
						   "\"content\":\"Very good News\"}";
		NetworkResponse networkResponse = new NetworkResponse(content.getBytes());
		JacksonRequest<News> request = new JacksonRequest<News>(url, News.class, listener);
		// When
		Response<News> response = request.parseNetworkResponse(networkResponse);
		// Then
		News news = response.result;
		assertThat(news.imageUrl, is("http://static.naver.com/volley-ext.jpg"));
		assertThat(news.title, is("Volley extention has released"));
	}
	
	@Test
	public void networkResponseShouldReturnErrorWithAddtionalAttribute() throws JsonProcessingException {
		// Given
		String content = "{\"imageUrl\":\"http://static.naver.com/volley-ext.jpg\"," +
						   "\"title\":\"Volley extention has released\"," +
						   "\"content\":\"Very good News\"}";
		NetworkResponse networkResponse = new NetworkResponse(content.getBytes());
		ObjectMapper objectMapperWithDefaultOption = new ObjectMapper();
		JacksonRequest<News> request = new JacksonRequest<News>(url, News.class, objectMapperWithDefaultOption,  listener);
		// When
		Response<News> response = request.parseNetworkResponse(networkResponse);
		// Then
		assertNull(response.result);
		assertThat(response.error, is(instanceOf(ParseError.class)));
		assertThat(response.error.getCause(), is(instanceOf(UnrecognizedPropertyException.class)));
	}
	
	@Test
	public void networkResponseShouldNotBeParsedWithInvalidFormat() throws JsonProcessingException {
		// Given
		String content = "{\"imageUrl\":\"http://static.nav";
		NetworkResponse networkResponse = new NetworkResponse(content.getBytes());
		JacksonRequest<News> request = new JacksonRequest<News>(url, News.class, listener);
		// When
		Response<News> response = request.parseNetworkResponse(networkResponse);
		// Then
		assertNull(response.result);
		assertThat(response.error, is(instanceOf(ParseError.class)));
		assertThat(response.error.getCause(), is(instanceOf(JsonParseException.class)));
	}

	@Test
	public void networkResponseShouldBeParsedWithSpecialChars() throws JsonProcessingException {
		// Given
		String content = "{\"title\":\"å &acirc;\"}";
		NetworkResponse networkResponse = new NetworkResponse(content.getBytes());
		ObjectMapper objectMapperWithDefaultOption = new ObjectMapper();
		JacksonRequest<News> request = new JacksonRequest<News>(url, News.class, objectMapperWithDefaultOption, listener);
		// When
		Response<News> response = request.parseNetworkResponse(networkResponse);
		// Then
		assertNotNull(response.result);
		assertNull(response.error);
	}

	@Test
	public void networkResponseShouldNotBeParsedWithUnsupportedException() throws JsonProcessingException {
		// Given
		String content = "{\"imageUrl\":\"http://static.naver.com\"}";
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put(HTTP.CONTENT_TYPE, "text/html;charset=UTF-14");
		NetworkResponse networkResponse = new NetworkResponse(content.getBytes(), headers);
		JacksonRequest<News> request = new JacksonRequest<News>(url, News.class, listener);
		// When
		Response<News> response = request.parseNetworkResponse(networkResponse);
		// Then
		assertNull(response.result);
		assertThat(response.error, is(instanceOf(ParseError.class)));
		assertThat(response.error.getCause(), is(instanceOf(UnsupportedEncodingException.class)));
	}	
	
	
	@Test(expected = NullPointerException.class)
	public void requestShouldThrowNpeWhenObectMapperIsNull() {
		new JacksonRequest<News>(url, News.class, null, listener);
	}

	@Test(expected = NullPointerException.class)
	public void requestShouldThrowNpeWhenObectMapperIsNullWithErrorListener() {
		new JacksonRequest<News>(url, News.class, null, listener, errorListener);
	}

	@Test(expected = NullPointerException.class)
	public void requestShouldThrowNpeWhenObectMapperIsNullWithErrorListenerAndMethod() {
		new JacksonRequest<News>(Method.GET, url, News.class, null, listener, errorListener);
	}

	@Test(expected = NullPointerException.class)
	public void requestShouldThrowNpeWhenListenerIsNull() {
		new JacksonRequest<News>(url, News.class, null);
	}

	@Test(expected = NullPointerException.class)
	public void testWhenListenerIsNullWithErrorListener() {
		new JacksonRequest<News>(url, News.class, null, errorListener);
	}

	@Test(expected = NullPointerException.class)
	public void testWhenListenerIsNullWithErrorListenerAndMethod() {
		new JacksonRequest<News>(Method.GET, url, News.class, null, errorListener);
	}

	/** just for test */
	private static class News {
		public String imageUrl;
		public String title;		
	}
}
