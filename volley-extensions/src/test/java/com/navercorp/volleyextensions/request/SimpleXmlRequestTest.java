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

import javax.xml.stream.XMLStreamException;

import org.apache.http.protocol.HTTP;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;
import org.simpleframework.xml.core.ElementException;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.navercorp.volleyextensions.mock.ErrorResponseHoldListener;
import com.navercorp.volleyextensions.mock.ResponseHoldListener;
import com.navercorp.volleyextensions.request.SimpleXmlRequest;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class SimpleXmlRequestTest {
	String url = "http://test";
	ResponseHoldListener<News> listener = new ResponseHoldListener<News>();
	ErrorResponseHoldListener errorListener = new ErrorResponseHoldListener();

	@BeforeClass
	public static void setUpOnce() throws Exception {
		ShadowLog.stream = System.out;
	}
	
	@Test
	public void networkResponseShouldBeParsed() throws Exception {
		// Given
		String content =
		"<news>\n" + 
		 "   <imageUrl>http://static.naver.com/volley-ext.jpg</imageUrl>\n" + 
		 "   <title>Volley extention has released</title>\n" + 
		 "</news>";
		SimpleXmlRequest<News> request = new SimpleXmlRequest<News>(url, News.class,listener);
		NetworkResponse networkResponse = new NetworkResponse(content.getBytes());
		// When
		Response<News> response = request.parseNetworkResponse(networkResponse);
		// Then
		News news = response.result;
		assertThat(news.imageUrl, is("http://static.naver.com/volley-ext.jpg"));
		assertThat(news.title, is("Volley extention has released"));
	}

	@Test
	public void networkResponseShouldNotBeParsedWithUnsupportedException() throws Exception {
		// Given
		String content =
		"<news>\n" + 
		 "   <imageUrl>http://static.naver.com/volley-ext.jpg</imageUrl>\n" + 
		 "   <title>Volley extention has released</title>\n" + 
		 "</news>";
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put(HTTP.CONTENT_TYPE, "text/html;charset=UTF-14");
		SimpleXmlRequest<News> request = new SimpleXmlRequest<News>(url, News.class,listener);
		NetworkResponse networkResponse = new NetworkResponse(content.getBytes(), headers);
		// When
		Response<News> response = request.parseNetworkResponse(networkResponse);
		// Then
		assertNull(response.result);
		assertThat(response.error, is(instanceOf(ParseError.class)));
		assertThat(response.error.getCause(), is(instanceOf(UnsupportedEncodingException.class)));
	}
	
	@Ignore
	@Test
	public void networkResponseShouldNotBeParsedWithInvalidFormat() throws JsonProcessingException {
		// Given
		String content =
		"<news>\n" + 
		 "   <imageUrl>http://static.naver.com/volley-ext.jpg</imageUrl>\n" + 
		 "   <title>Volley extention has released</title>\n" + 
		 "</news----->";
		NetworkResponse networkResponse = new NetworkResponse(content.getBytes());
		SimpleXmlRequest<News> request = new SimpleXmlRequest<News>(url, News.class,listener);
		// When
		Response<News> response = request.parseNetworkResponse(networkResponse);
		// Then
		assertNull(response.result);
		assertThat(response.error, is(instanceOf(ParseError.class)));
		assertThat(response.error.getCause(), is(instanceOf(XMLStreamException.class)));
	}
	
	@Test
	public void networkResponseShouldNotBeParsedWithAddtionalTag() throws JsonProcessingException {
		// Given
		String content =
		"<news>\n" + 
		 "   <imageUrl>http://static.naver.com/volley-ext.jpg</imageUrl>\n" + 
		 "   <title>Volley extention has released</title>\n" + 
		 "   <summary>Good news</summary>\n" +		 
		 "</news>";
		NetworkResponse networkResponse = new NetworkResponse(content.getBytes());
		SimpleXmlRequest<News> request = new SimpleXmlRequest<News>(url, News.class,listener);
		// When
		Response<News> response = request.parseNetworkResponse(networkResponse);
		// Then
		assertNull(response.result);
		assertThat(response.error, is(instanceOf(ParseError.class)));
		assertThat(response.error.getCause(), is(instanceOf(ElementException.class)));
	}
	
	@Test(expected = NullPointerException.class)
	public void requestShouldThrowNpeWhenPersisterIsNull() {
		new SimpleXmlRequest<News>(url, News.class, null, listener);
	}

	@Test(expected = NullPointerException.class)
	public void requestShouldThrowNpeWhenPersisterIsNullWithErrorListener() {
		new SimpleXmlRequest<News>(url,	News.class, null, listener, errorListener);
	}

	@Test(expected = NullPointerException.class)
	public void requestShouldThrowNpeWhenPersisterIsNullWithErrorListenerAndMethod() {
		new SimpleXmlRequest<News>(Method.GET, url, News.class, null, listener, errorListener);
	}

	@Test(expected = NullPointerException.class)
	public void requestShouldThrowNpeWhenListenerIsNull() {
		new SimpleXmlRequest<News>(url, News.class, null);
	}

	@Test(expected = NullPointerException.class)
	public void requestShouldThrowNpeWhenListenerIsNullWithErrorListener() {
		new SimpleXmlRequest<News>(url,	News.class, null, errorListener);
	}

	@Test(expected = NullPointerException.class)
	public void requestShouldThrowNpeWhenListenerIsNullWithErrorListenerAndMethod() {
		new SimpleXmlRequest<News>(Method.GET, url, News.class, null, errorListener);
	}

	/** just for test */
	private static class News {
		public String imageUrl;
		public String title;		
	}
}
