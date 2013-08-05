package com.nhncorp.volleyextensions.cache.memory.impl;

import com.nhncorp.volleyextensions.cache.memory.UniversalImageCache;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;

/**
 * A wrapper class for {@link UsingFreqLimitedMemoryCache}
 * 
 * @see UniversalImageCache
 * @see UsingFreqLimitedMemoryCache
 * 
 */
public class UniversalUsingFreqLimitedMemoryCache extends UniversalImageCache {
	/**
	 * @param sizeLimit
	 *            Maximum size for cache (in bytes)
	 */
	public UniversalUsingFreqLimitedMemoryCache(int sizeLimit) {
		super(new UsingFreqLimitedMemoryCache(sizeLimit));
	}
}
