package com.nhncorp.volleyextensions.cache.memory;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowBitmap;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

import com.nostra13.universalimageloader.cache.memory.MemoryCacheAware;

@RunWith(RobolectricTestRunner.class)
public class UniversalImageCacheTest {
	@Mock MemoryCacheAware<String, Bitmap> delegate;
	@InjectMocks UniversalImageCache imageCache;
	String key = "test";
	Bitmap value = ShadowBitmap.createBitmap(100, 200, Config.ALPHA_8);

	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
	}
	
	@Test(expected = NullPointerException.class)
	public void constructorShouldThrowNpeWhenCacheDirIsNull() {
		new UniversalImageCache(null);
	}

	@Test
	public void putShouldBeCalled() {
		//When
		imageCache.put(key, value);
		//Then
		verify(delegate).put(key, value);
	}

	@Test
	public void putShouldBeCalledWhenPutBitmap() {
		// When
		imageCache.putBitmap(key, value);
		// Then
		verify(delegate).put(key, value);
	}
	
	@Test
	public void getShouldBeCalled() {
		// Given
		given(delegate.get(key)).willReturn(value);
		// When
		Bitmap hit = imageCache.get(key);
		// Then
		assertThat(hit, is(value));
	}
	
	@Test
	public void getShouldBeCalledWhenGetBitmap() {
		// Given
		given(delegate.get(key)).willReturn(value);
		// When
		Bitmap hit = imageCache.getBitmap(key);
		// Then
		assertThat(hit, is(value));
	}
	
	@Test
	public void clearShouldBeCalled() {
		// When
		imageCache.clear();
		// Then
		verify(delegate).clear();
	}
	
	@Test
	public void removedShouldBeCalled() {
		// When
		imageCache.remove(key);
		// Then
		verify(delegate).remove(key);
	}
	
	@Test
	public void keysShouldContainCachedKey() {
		//Given
		given(delegate.keys()).willReturn(Arrays.asList(key));
		//When
		Collection<String> keys = imageCache.keys();
		//Then
		assertTrue(keys.contains(key));
	}
}
