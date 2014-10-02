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
 */package com.navercorp.volleyextensions.volleyer.multipart.stack;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Map;

import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.HttpStack;
import com.github.kristofa.test.http.MockHttpServer;
import com.github.kristofa.test.http.SimpleHttpResponseProvider;
import com.navercorp.volleyextensions.volleyer.MyShadowSystemClock;
import com.navercorp.volleyextensions.volleyer.multipart.Multipart;
import com.navercorp.volleyextensions.volleyer.multipart.MultipartContainer;
import com.navercorp.volleyextensions.volleyer.multipart.PartTestUtils;
import com.navercorp.volleyextensions.volleyer.multipart.TestMultipartRequest;
import com.navercorp.volleyextensions.volleyer.multipart.stack.MultipartHttpClientStack;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, shadows = { MyShadowSystemClock.class })
public class MultipartHttpClientStackTest {

	static final Map<String, String> EMPTY_MAP = Collections.emptyMap();

	private static final int PORT = 51234;
	private static final String url = "http://localhost:" + PORT;

	private SimpleHttpResponseProvider responseProvider;
	private MockHttpServer server;

	@Before
	public void setUp() throws Exception {
		// Disable fake http layer
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);

		// init mock http server
		responseProvider = new SimpleHttpResponseProvider();
		server = new MockHttpServer(PORT, responseProvider);
		server.start();
	}

	@After
	public void tearDown() throws Exception {
		// stop the mock http server of running
		server.stop();
	}

	@Test
	public void shouldSkipMultipartIfNotExist() throws AuthFailureError, IOException {
		MultipartContainer mock = givenAndWhen(false /* hasMultipart */);
		// Then
		verify(mock, never()).getMultipart();
	}

	@Test
	public void shouldCallMultipartWriteWhenRequestHasMultipart() throws Exception {
		Multipart multipart = mock(Multipart.class);
		MultipartContainer mock = givenAndWhen(true /* hasMultipart*/, multipart);
		// Then
		verify(mock).getMultipart();
		verify(multipart).write(Matchers.any(OutputStream.class));
	}

	private MultipartContainer givenAndWhen(boolean hasMultipart) throws IOException, AuthFailureError {
		return givenAndWhen(hasMultipart, PartTestUtils.createSampleMultipart());
	}

	private MultipartContainer givenAndWhen(boolean hasMultipart, Multipart multipart) throws IOException, AuthFailureError {
		// Given
        DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpStack stack = new MultipartHttpClientStack(httpClient);
		MultipartContainer mock = PartTestUtils.createMultipartContainerMock(hasMultipart, multipart);
		Request<?> request = new TestMultipartRequest(url, Method.POST, mock);
		// When
		stack.performRequest(request, EMPTY_MAP);
		return mock;
	}

}
