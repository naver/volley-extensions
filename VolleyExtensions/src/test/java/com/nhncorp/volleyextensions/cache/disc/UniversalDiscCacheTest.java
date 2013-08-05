package com.nhncorp.volleyextensions.cache.disc;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.io.File;

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

}
