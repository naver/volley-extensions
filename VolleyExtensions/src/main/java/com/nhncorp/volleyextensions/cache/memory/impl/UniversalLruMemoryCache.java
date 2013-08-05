package com.nhncorp.volleyextensions.cache.memory.impl;

import com.nhncorp.volleyextensions.cache.memory.UniversalImageCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
/**
 * A wrapper class for {@link LruMemoryCache}
 * @see UniversalImageCache
 * @see LruMemoryCache
 */
public class UniversalLruMemoryCache extends UniversalImageCache {
	/** @param maxSize Maximum sum of the sizes of the Bitmaps in this cache */
	public UniversalLruMemoryCache(int maxSize) {
		super(new LruMemoryCache(maxSize));
	}
}
