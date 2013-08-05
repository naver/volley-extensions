package com.nhncorp.volleyextensions.cache.disc.impl;

import java.io.File;

import com.nhncorp.volleyextensions.cache.disc.UniversalBaseDiscCache;
import com.nostra13.universalimageloader.cache.disc.impl.FileCountLimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;
/**
 * A wrapper class for {@link FileCountLimitedDiscCache}
 * @see UniversalBaseDiscCache
 * @see FileCountLimitedDiscCache 
 */
public class UniversalFileCountLimitedDiscCache extends UniversalBaseDiscCache {
	/**
	 * @param cacheDir     Directory for file caching. <b>Important:</b> Specify separate folder for cached files. It's
	 *                     needed for right cache limit work.
	 * @param maxFileCount Maximum file count for cache. If file count in cache directory exceeds this limit then file
	 *                     with the most oldest last usage date will be deleted.
	 */
	public UniversalFileCountLimitedDiscCache(File cacheDir, int maxFileCount) {
		super(cacheDir, new FileCountLimitedDiscCache(cacheDir, maxFileCount));
	}
	
	/**
	 * @param cacheDir          Directory for file caching. <b>Important:</b> Specify separate folder for cached files. It's
	 *                          needed for right cache limit work.
	 * @param fileNameGenerator Name generator for cached files
	 * @param maxFileCount      Maximum file count for cache. If file count in cache directory exceeds this limit then file
	 *                          with the most oldest last usage date will be deleted.
	 */
	public UniversalFileCountLimitedDiscCache(File cacheDir,
			FileNameGenerator fileNameGenerator, int maxFileCount) {
		super(cacheDir, new FileCountLimitedDiscCache(cacheDir, fileNameGenerator, maxFileCount));
	}

}
