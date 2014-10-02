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
package com.navercorp.volleyextensions.volleyer.factory;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.navercorp.volleyextensions.volleyer.MyShadowSystemClock;
import com.navercorp.volleyextensions.volleyer.multipart.stack.MultipartHttpStackWrapper;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, shadows = { MyShadowSystemClock.class })
public class HttpStackFactoryTest {

	@Test
	public void shouldReturnMultipartHttpStackWrapper() {
		// When
		HttpStack httpStack = HttpStackFactory.createDefaultHttpStack();
		// Then
		assertThat(httpStack, is(instanceOf(MultipartHttpStackWrapper.class)));
	}

	@Test
	public void shouldCreateNotNullHurlStack() {
		// When
		HurlStack httpStack = HttpStackFactory.createHurlStack();
		// Then
		assertTrue(httpStack != null);
	}

	@Test
	public void shouldCreateNotNullHttpClientStack() {
		// Given
		HttpClient httpClient = new DefaultHttpClient();
		// When
		HttpClientStack httpStack = HttpStackFactory.createHttpClientStack(httpClient);
		// Then
		assertTrue(httpStack != null);
	}
}
