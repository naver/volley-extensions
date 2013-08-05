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
public class UniversalLruMemoryCacheTest {
	
	@Test
	public void bitmapShouldBeCached(){
    	//Given
		String url = "http://me.do/test1.jpg";
		Bitmap image = ShadowBitmap.createBitmap(10, 10, Config.ALPHA_8);	
		ImageCache cache = new UniversalLruMemoryCache(100);
		//When
		cache.putBitmap(url, image);
		//Then
		Bitmap hit = cache.getBitmap(url);
		assertNotNull(hit);
	}
	
	@Test
	public void bitmapShouldNotBeCachedWhenExceedLimitSize(){
    	//Given
		String url = "http://me.do/test1.jpg";
		Bitmap image = ShadowBitmap.createBitmap(100, 100, Config.ALPHA_8);	
		ImageCache cache = new UniversalLruMemoryCache(10);
		//When
		cache.putBitmap(url, image);
		//Then
		Bitmap hit = cache.getBitmap(url);
		assertNull(hit);
	}
	
	@Test
	public void lruBitmapShouldNotBeRemovedWhenExceedLimitSize(){
    	//Given
		String url1 = "http://me.do/test1.jpg";
		Bitmap image1 = ShadowBitmap.createBitmap(1, 10, Config.ALPHA_8);	
		String url2 = "http://me.do/test2.jpg";
		Bitmap image2 = ShadowBitmap.createBitmap(1, 20, Config.ALPHA_8);
		String url3 = "http://me.do/test3.jpg";
		Bitmap image3 = ShadowBitmap.createBitmap(1, 30, Config.ALPHA_8);		
		ImageCache cache = new UniversalLruMemoryCache(50);
		//When
		cache.putBitmap(url1, image1);
		cache.putBitmap(url2, image2);
		cache.getBitmap(url1);
		cache.putBitmap(url3, image3);
		
		//Then
		assertNotNull(cache.getBitmap(url1));
		assertNull(cache.getBitmap(url2));
		assertNotNull(cache.getBitmap(url3));
	}
}
