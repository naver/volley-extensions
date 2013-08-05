package com.nhncorp.volleyextensions.cache.memory.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowBitmap;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.nostra13.universalimageloader.cache.memory.impl.FIFOLimitedMemoryCache;
import com.nostra13.universalimageloader.core.assist.MemoryCacheUtil;


@RunWith(RobolectricTestRunner.class)
@org.robolectric.annotation.Config(manifest=org.robolectric.annotation.Config.NONE)
public class UniversalFuzzyKeyMemoryCacheTest {
	@Test
	public void cacheShouldBeRemovedByComparisionPolicy(){
    	//Given
		String url1 = "http://me.do/test1.jpg_small";
		Bitmap image1 = ShadowBitmap.createBitmap(10, 10, Config.ALPHA_8);
		String url2 = "http://me.do/test1.jpg_large";
		Bitmap image2 = ShadowBitmap.createBitmap(20, 20, Config.ALPHA_8);		
		ImageCache cache = new UniversalFuzzyKeyMemoryCache(new FIFOLimitedMemoryCache(100000), 
															MemoryCacheUtil.createFuzzyKeyComparator());
		//When
		cache.putBitmap(url1, image1);
		cache.putBitmap(url2, image2);
		
		//Then
		Bitmap hit = cache.getBitmap(url1);
		assertNull(hit);
	}
}
