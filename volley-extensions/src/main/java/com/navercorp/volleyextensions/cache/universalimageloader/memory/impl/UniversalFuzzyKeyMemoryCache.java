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

import java.util.Comparator;

import android.graphics.Bitmap;

import com.navercorp.volleyextensions.cache.universalimageloader.memory.UniversalImageCache;
import com.nostra13.universalimageloader.cache.memory.MemoryCacheAware;
import com.nostra13.universalimageloader.cache.memory.impl.FuzzyKeyMemoryCache;
/**
 * A wrapper class for {@link FuzzyKeyMemoryCache}
 * 
 * @see UniversalImageCache
 * @see FuzzyKeyMemoryCache
 */
public class UniversalFuzzyKeyMemoryCache extends UniversalImageCache {
	/**
	 * @param delegate Wrapped memory cache
	 * @param keyComparator 
	 */
	public UniversalFuzzyKeyMemoryCache(MemoryCacheAware<String, Bitmap> delegate,
			Comparator<String> keyComparator) {
		super(new FuzzyKeyMemoryCache<String, Bitmap>(delegate, keyComparator));
	}

}
