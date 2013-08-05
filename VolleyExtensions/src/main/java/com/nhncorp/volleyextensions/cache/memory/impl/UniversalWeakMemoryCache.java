package com.nhncorp.volleyextensions.cache.memory.impl;

import com.nhncorp.volleyextensions.cache.memory.UniversalImageCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
/**
 * A wrapper class for {@link WeakMemoryCache}
 * @see UniversalImageCache
 * @see WeakMemoryCache
 */
public class UniversalWeakMemoryCache extends UniversalImageCache {
	public UniversalWeakMemoryCache() {
		super(new WeakMemoryCache());
	}
}
