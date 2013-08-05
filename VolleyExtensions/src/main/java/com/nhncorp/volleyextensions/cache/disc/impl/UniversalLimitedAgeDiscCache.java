package com.nhncorp.volleyextensions.cache.disc.impl;

import java.io.File;

import com.nhncorp.volleyextensions.cache.disc.UniversalBaseDiscCache;
import com.nostra13.universalimageloader.cache.disc.impl.LimitedAgeDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;
/**
 * A wrapper class for {@link LimitedAgeDiscCache}
 * @see UniversalBaseDiscCache
 * @see LimitedAgeDiscCache
 */
public class UniversalLimitedAgeDiscCache extends UniversalBaseDiscCache {
	/**
	 * @param cacheDir Directory for file caching
	 * @param maxAge   Max file age (in seconds). If file age will exceed this value then it'll be removed on next
	 *                 treatment (and therefore be reloaded).
	 */	
	public UniversalLimitedAgeDiscCache(File cacheDir, int maxAge) {
		super(cacheDir, new LimitedAgeDiscCache(cacheDir, maxAge));
	}
	
	/**
	 * @param cacheDir          Directory for file caching
	 * @param fileNameGenerator Name generator for cached files
	 * @param maxAge            Max file age (in seconds). If file age will exceed this value then it'll be removed on next
	 *                          treatment (and therefore be reloaded).
	 */	
	public UniversalLimitedAgeDiscCache(File cacheDir,
			FileNameGenerator fileNameGenerator, long maxAge) {
		super(cacheDir, new LimitedAgeDiscCache(cacheDir, fileNameGenerator, maxAge));
	}
}
