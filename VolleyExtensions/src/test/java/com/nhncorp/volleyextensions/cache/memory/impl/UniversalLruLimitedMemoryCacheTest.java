package com.nhncorp.volleyextensions.cache.memory.impl;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowBitmap;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

import com.android.volley.toolbox.ImageLoader.ImageCache;


@RunWith(RobolectricTestRunner.class)
@org.robolectric.annotation.Config(manifest=org.robolectric.annotation.Config.NONE)
public class UniversalLruLimitedMemoryCacheTest {
	@Test
	public void bitmapShouldBeCached(){
    	//Given
		String url = "http://me.do/test1.jpg";
		Bitmap image = ShadowBitmap.createBitmap(100, 100, Config.ALPHA_8);	
		ImageCache cache = new UniversalLruLimitedMemoryCache(10);
		//When
		cache.putBitmap(url, image);
		//Then
		Bitmap hit = cache.getBitmap(url);
		assertThat("Bitmap should be cached by WeakReference event if it exceeds size limit of  UniversalFifoLimitedMemoryCache",
				hit, is(image));
	}
}
