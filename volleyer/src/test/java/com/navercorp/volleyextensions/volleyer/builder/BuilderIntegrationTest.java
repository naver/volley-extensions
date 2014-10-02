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
import static org.mockito.BDDMockito.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import com.android.volley.AuthFailureError;
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
public class BuilderIntegrationTest {
	static VolleyerConfiguration configuration = DefaultVolleyerConfigurationFactory.create();
	static String url = "http://test";
	static String body = "Test body";

	static Listener<String> listener = new Listener<String>() {

		@Override
		public void onResponse(String response) {
			ShadowLog.d("TestClass", "Result : " + response);
		}
	};

	static ErrorListener errorListener = new ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError error) {
			ShadowLog.d("TestClass", "Error : " + error);
		}
	};

	RequestQueue requestQueue = mock(RequestQueue.class);

	@Test
	public void getBuilderChainShouldMakeRequestInstanceFinally() throws AuthFailureError {
		GetBuilder getBuilder = new GetBuilder(requestQueue, configuration, url);
		Request<String> request = createRequest(url, getBuilder);
		assertRequest(url, HttpMethod.GET, request);
	}

	@Test
	public void postBuilderChainShouldMakeRequestInstanceFinally() throws AuthFailureError {
		PostBuilder postBuilder = new PostBuilder(requestQueue, configuration, url);
		buildBodyOptionFor(postBuilder);
		Request<String> request = createRequest(url, postBuilder);
		assertRequest(url, HttpMethod.POST, request);
		assertBodyOption(request);
	}

	private static void buildBodyOptionFor(BodyBuilder<?> builder) {
		builder.withBody(body);
	}

	private static void assertBodyOption(Request<String> request) throws AuthFailureError {
		assertThat(request.getBody(), is(body.getBytes()));
	}

	@Test
	public void putBuilderChainShouldMakeRequestInstanceFinally() throws AuthFailureError {
		PutBuilder putBuilder = new PutBuilder(requestQueue, configuration, url);
		buildBodyOptionFor(putBuilder);
		Request<String> request = createRequest(url, putBuilder);
		assertRequest(url, HttpMethod.PUT, request);
		assertBodyOption(request);
	}

	@Test
	public void deleteBuilderChainShouldMakeRequestInstanceFinally() throws AuthFailureError {
		DeleteBuilder deleteBuilder = new DeleteBuilder(requestQueue, configuration, url);
		Request<String> request = createRequest(url, deleteBuilder);
		assertRequest(url, HttpMethod.DELETE, request);
	}

	private static <B extends RequestBuilder<B>> Request<String> createRequest(String url, RequestBuilder<B> builder) throws AuthFailureError {
		Request<String> request = 
				builder
				.addHeader("name", "JohnDoe")
				.addHeader("age", "23")
				.addHeader("job", "student")
				.withTargetClass(String.class)
					.withListener(listener)
					.withErrorListener(errorListener)
					.execute();

		return request;
	}

	private static void assertRequest(String url, HttpMethod method, Request<String> request) throws AuthFailureError {
		// Then
		assertTrue(request != null);
		assertThat(request.getUrl(), is(url));
		assertThat(request.getMethod(), is(method.getMethodCode()));
		assertThat(request.getHeaders().get("name"), is("JohnDoe"));
		assertThat(request.getHeaders().get("age"), is("23"));
		assertThat(request.getHeaders().get("job"), is("student"));
	}
}
