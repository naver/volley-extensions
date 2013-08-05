package com.nhncorp.volleyextensions.cache.memory.impl;

import com.nhncorp.volleyextensions.cache.memory.UniversalImageCache;
import com.nostra13.universalimageloader.cache.memory.impl.LargestLimitedMemoryCache;
/**
 * A wrapper class for {@link LargestLimitedMemoryCache}
 * @see UniversalImageCache
 * @see LargestLimitedMemoryCache
 *
 */
public class UniversalLargestLimitedMemoryCache extends UniversalImageCache {
	/** @param sizeLimit Maximum size for cache (in bytes) */
	public UniversalLargestLimitedMemoryCache(int sizeLimit) {
		super(new LargestLimitedMemoryCache(sizeLimit));
	}
}
