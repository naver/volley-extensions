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
package com.navercorp.volleyextensions.cache.universalimageloader.disc;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.*;

import java.io.File;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import com.android.volley.Cache.Entry;
import com.navercorp.volleyextensions.cache.universalimageloader.disc.UniversalDiscCache;
import com.nostra13.universalimageloader.cache.disc.DiscCacheAware;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class UniversalDiscCacheTest {
	@Mock DiscCacheAware delegate;
	@InjectMocks UniversalDiscCache discCache;

	@Before
	public void setUp() {
		ShadowLog.stream = System.out;
		MockitoAnnotations.initMocks(this);
	}
	
	@Test(expected = NullPointerException.class)
	public void cacheShouldThrowNpe() {
		new UniversalDiscCache(null);
	}

	@Test
	public void clearShouldCalled() {
		// When
		discCache.clear();
		// Then
		verify(delegate).clear();
	}

	@Test
	public void entryShouldBeNull() {
		// When
		Entry entry = discCache.get(null);
		// Then
		assertNull(entry);
	}

	@Test
	public void testHasCalledGet() {
		// Given
		String keyForTest = "test";
		// When
		discCache.get(keyForTest);
		// Then
		verify(delegate).get(keyForTest);
	}

	@Test
	public void testKeyIsNullWhenCalledPut() {
		// Given
		Entry entry = new Entry();
		// When
		discCache.put(null, entry);
		// Then
		verify(delegate, never()).put(null, new File(""));

	}

	@Test
	public void testHasCalledPut() {
		// Given
		Entry entry = new Entry();
		entry.data = new byte[] { 0x01, 0x01, 0x02 };
		entry.etag = "tag";

		String keyForTest = "test";
		// make a file for real 
		File file = new File("realfile");
		given(delegate.get(keyForTest)).willReturn(file);
		inOrder(delegate);
		// When
		discCache.put(keyForTest, entry);
		file.delete();
		// Then
		verify(delegate).get(keyForTest);
		verify(delegate).put(keyForTest, file);
	}

	@Test
	public void shouldContainEntryValuesFromCache() {
		// Given
		Entry entry = new Entry();
		entry.data = new byte[] { 0x01, 0x01, 0x02 };
		entry.etag = "tag";
		entry.ttl = 1000;
		entry.softTtl = 5321;
		entry.serverDate = 1234;
		entry.responseHeaders = new HashMap<String, String>(){{
			put("header1", "value1");
			put("header2", "value2");
		}};

		String keyForTest = "test";
		// make a file for real
		File file = new File("realfile");
		given(delegate.get(keyForTest)).willReturn(file);
		inOrder(delegate);
		// When
		discCache.put(keyForTest, entry);
		Entry newEntry = discCache.get(keyForTest);
		file.delete();
		// Then
		assertThat(newEntry.data, is(entry.data));
		assertThat(newEntry.etag, is(entry.etag));
		assertThat(newEntry.ttl, is(entry.ttl));
		assertThat(newEntry.softTtl, is(entry.softTtl));
		assertThat(newEntry.serverDate, is(entry.serverDate));
		assertThat(newEntry.responseHeaders, is(entry.responseHeaders));
	}
}
