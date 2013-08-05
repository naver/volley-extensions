package com.nhncorp.volleyextensions.cache.memory.impl;

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
public class UniversalUsingFreqLimitedMemoryCacheTest {
	@Test
	public void bitmapShouldBeCached(){
    	//Given
		String url = "http://me.do/test1.jpg";
		Bitmap image = ShadowBitmap.createBitmap(10, 10, Config.ALPHA_8);	
		ImageCache cache = new UniversalUsingFreqLimitedMemoryCache(100);
		//When
		cache.putBitmap(url, image);
		//Then
		Bitmap hit = cache.getBitmap(url);
		assertNotNull(hit);
	}
}
