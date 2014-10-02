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
package com.navercorp.volleyextensions.sample.demos.application.volley;

import java.io.File;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.NoCache;
import com.navercorp.volleyextensions.cache.universalimageloader.disc.impl.UniversalUnlimitedDiscCache;
import com.navercorp.volleyextensions.cache.universalimageloader.memory.impl.UniversalLimitedAgeMemoryCache;
import com.navercorp.volleyextensions.cache.universalimageloader.memory.impl.UniversalLruMemoryCache;

public class MyVolley {
	private static final int DEFAULT_CACHE_SIZE = 10 * 1024 * 1024;
	private static final long DEFAULT_MAX_AGE = 60;
	private static RequestQueue requestQueue;
	private static ImageLoader imageLoader;

	public static void init(Context context) {
		if (context == null)
			throw new NullPointerException("context must not be null.");

		Cache diskCache = getDefaultDiskCache(context);
		ImageCache memoryCache = getDefaultImageCache(context);
		requestQueue = new RequestQueue(diskCache, new BasicNetwork(
				new HurlStack()));

		imageLoader = new ImageLoader(requestQueue, memoryCache);

		requestQueue.start();
	}


	public static RequestQueue getRequestQueue() {
		if (requestQueue == null)
			throw new IllegalStateException("RequestQueue is not initialized.");
		return requestQueue;
	}

	public static ImageLoader getImageLoader() {
		if (imageLoader == null)
			throw new IllegalStateException("ImageLoader is not initialized.");
		return imageLoader;
	}

	private static ImageCache getDefaultImageCache(Context context) {
		return new UniversalLimitedAgeMemoryCache(new UniversalLruMemoryCache(
				DEFAULT_CACHE_SIZE), DEFAULT_MAX_AGE);
	}

	private static Cache getDefaultDiskCache(Context context) {
		File cacheDir = getCacheDir(context);
		if (cacheDir == null) {
			return new NoCache();
		}

		return new UniversalUnlimitedDiscCache(cacheDir);
	}

	private static File getCacheDir(Context context) {
		File file = new File(context.getCacheDir().getPath()
				+ "/test-universal");
		return file;
	}
}
