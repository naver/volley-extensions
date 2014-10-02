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

import android.graphics.Bitmap;

import com.navercorp.volleyextensions.cache.universalimageloader.memory.UniversalImageCache;
import com.nostra13.universalimageloader.cache.memory.MemoryCacheAware;
import com.nostra13.universalimageloader.cache.memory.impl.LimitedAgeMemoryCache;
/**
 * A wrapper class for {@link LimitedAgeMemoryCache}
 * @see UniversalImageCache
 * @see LimitedAgeMemoryCache
 *
 */
public class UniversalLimitedAgeMemoryCache extends UniversalImageCache {
	/**
	 * @param cache  Wrapped memory cache
	 * @param maxAge Max object age <b>(in seconds)</b>. If object age will exceed this value then it'll be removed from
	 *               cache on next treatment (and therefore be reloaded).
	 */
	public UniversalLimitedAgeMemoryCache(MemoryCacheAware<String, Bitmap> cache, long maxAge) {
		super(new LimitedAgeMemoryCache<String, Bitmap>(cache, maxAge) );
	}

}
