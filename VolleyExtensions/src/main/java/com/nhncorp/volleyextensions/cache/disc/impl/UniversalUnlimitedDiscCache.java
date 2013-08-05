package com.nhncorp.volleyextensions.cache.disc.impl;

import java.io.File;

import com.nhncorp.volleyextensions.cache.disc.UniversalBaseDiscCache;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;
/**
 * A wrapper class for {@link UnlimitedDiscCache}
 * @see UniversalBaseDiscCache
 * @see UnlimitedDiscCache
 *
 */
public class UniversalUnlimitedDiscCache extends UniversalBaseDiscCache {
	/** @param cacheDir Directory for file caching */
	public UniversalUnlimitedDiscCache(File cacheDir) {
		super(cacheDir, new UnlimitedDiscCache(cacheDir));
	}
	/**
	 * @param cacheDir          Directory for file caching
	 * @param fileNameGenerator Name generator for cached files
	 */
	public UniversalUnlimitedDiscCache(File cacheDir,
			FileNameGenerator fileNameGenerator) {
		super(cacheDir, new UnlimitedDiscCache(cacheDir, fileNameGenerator));
	}
}
