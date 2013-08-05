package com.nhncorp.volleyextensions.cache.memory.impl;

import android.graphics.Bitmap;

import com.nhncorp.volleyextensions.cache.memory.UniversalImageCache;
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
