package com.nhncorp.volleyextensions.cache.memory.impl;

import java.util.Comparator;

import android.graphics.Bitmap;

import com.nhncorp.volleyextensions.cache.memory.UniversalImageCache;
import com.nostra13.universalimageloader.cache.memory.MemoryCacheAware;
import com.nostra13.universalimageloader.cache.memory.impl.FuzzyKeyMemoryCache;
//import com.nostra13.universalimageloader.core.assist.MemoryCacheUtil;
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
	 * @see MemoryCacheUtil#createFuzzyKeyComparator
	 */
	public UniversalFuzzyKeyMemoryCache(MemoryCacheAware<String, Bitmap> delegate,
			Comparator<String> keyComparator) {
		super(new FuzzyKeyMemoryCache<String, Bitmap>(delegate, keyComparator));
	}

}
