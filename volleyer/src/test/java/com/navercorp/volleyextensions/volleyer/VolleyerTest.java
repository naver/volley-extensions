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
package com.navercorp.volleyextensions.volleyer;

import static org.hamcrest.CoreMatchers.*;

import org.junit.*;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import com.android.volley.RequestQueue;
import com.navercorp.volleyextensions.volleyer.builder.*;

import static com.navercorp.volleyextensions.volleyer.Volleyer.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class VolleyerTest {
	RequestQueue requestQueue;

	@Before
	public void setUp() {
		requestQueue = mock(RequestQueue.class);
	}

	@Test(expected = NullPointerException.class)
	public void volleyerShouldThrowNpeWhenRequestQueueIsNull() {
		volleyer(null);
	}

	@Test
	public void volleyerShouldReturnVolleyerSameInstanceBySameRequestQueue() {
		assertThat(volleyer(requestQueue), is(volleyer(requestQueue)));
	}

	@Test
	public void volleyerShouldReturnInstanceWhenDefaultVolleyerIsSetOrReturnDummyIfNot() {
		// When default volleyer is not set yet
		// Then
		assertEquals(volleyer().getClass(), DummyVolleyer.class);

		// When default volleyer is set
		volleyer(requestQueue).settings().setAsDefault().done();
		// Then
		assertNotNull(volleyer());
		assertThat(volleyer(), is(volleyer(requestQueue)));
	}

	@Test
	public void volleyerShouldReturnBuildersCorrectly() {
		// Given
		String url = "http://test";
		// When
		GetBuilder get = volleyer(requestQueue).get(url);
		PostBuilder post = volleyer(requestQueue).post(url);
		PutBuilder put = volleyer(requestQueue).put(url);
		DeleteBuilder delete = volleyer(requestQueue).delete(url);
		// Then
		assertNotNull(get);
		assertNotNull(post);
		assertNotNull(put);
		assertNotNull(delete);
	}
}
