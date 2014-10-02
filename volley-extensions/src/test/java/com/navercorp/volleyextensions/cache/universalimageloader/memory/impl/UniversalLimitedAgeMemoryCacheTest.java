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
package com.navercorp.volleyextensions.cache.universalimageloader.memory.impl;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowBitmap;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.navercorp.volleyextensions.cache.universalimageloader.memory.impl.UniversalFifoLimitedMemoryCache;

@RunWith(RobolectricTestRunner.class)
@org.robolectric.annotation.Config(manifest=org.robolectric.annotation.Config.NONE)
public class UniversalLimitedAgeMemoryCacheTest {
	@Test
    public void bitmapShouldBeCached() throws IOException, InterruptedException {
    	// Given
		String url = "http://me.do/test1.jpg";
		Bitmap image = ShadowBitmap.createBitmap(100, 100, Config.ALPHA_8);	
		ImageCache cache = new UniversalFifoLimitedMemoryCache(1);

		// When
		cache.putBitmap(url, image);
		// Then
		Bitmap hit = cache.getBitmap(url);
		assertThat(hit, is(image));
		
		TimeUnit.SECONDS.sleep(2);
		hit = cache.getBitmap(url);
		assertThat("Bitmap should be cached by WeakReference event if it exceeds size limit of  UniversalFifoLimitedMemoryCache",
				hit, is(image));    
	}
}
