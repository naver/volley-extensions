package com.nhncorp.volleyextensions.cache.disc.impl;

import java.io.File;

import com.nhncorp.volleyextensions.cache.disc.UniversalBaseDiscCache;
import com.nostra13.universalimageloader.cache.disc.impl.TotalSizeLimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;
/**
 * A wrapper class for {@link TotalSizeLimitedDiscCache}
 * @see UniversalBaseDiscCache
 * @see TotalSizeLimitedDiscCache
 * 
 */
public class UniversalTotalSizeLimitedDiscCache extends UniversalBaseDiscCache {
	/**
	 * @param cacheDir     Directory for file caching. <b>Important:</b> Specify separate folder for cached files. It's
	 *                     needed for right cache limit work.
	 * @param maxCacheSize Maximum cache directory size (in bytes). If cache size exceeds this limit then file with the
	 *                     most oldest last usage date will be deleted.
	 */
	public UniversalTotalSizeLimitedDiscCache(File cacheDir,
			int maxCacheSize) {
		super(cacheDir, new TotalSizeLimitedDiscCache(cacheDir, maxCacheSize));
	}
	
	/**
	 * @param cacheDir          Directory for file caching. <b>Important:</b> Specify separate folder for cached files. It's
	 *                          needed for right cache limit work.
	 * @param fileNameGenerator Name generator for cached files
	 * @param maxCacheSize      Maximum cache directory size (in bytes). If cache size exceeds this limit then file with the
	 *                          most oldest last usage date will be deleted.
	 */	
	public UniversalTotalSizeLimitedDiscCache(File cacheDir,
			FileNameGenerator fileNameGenerator, int maxCacheSize) {
		super(cacheDir, new TotalSizeLimitedDiscCache(cacheDir, fileNameGenerator, maxCacheSize));
	}
	
}
