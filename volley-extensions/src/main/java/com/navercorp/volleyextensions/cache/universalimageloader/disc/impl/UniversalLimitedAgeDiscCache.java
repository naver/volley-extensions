/*
 * Copyright (C) 2014 Naver Corp.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.navercorp.volleyextensions.cache.universalimageloader.disc.impl;

import java.io.File;

import com.navercorp.volleyextensions.cache.universalimageloader.disc.UniversalBaseDiscCache;
import com.navercorp.volleyextensions.cache.universalimageloader.disc.naming.CustomizedFileNameGeneratorFactory;
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
		super(cacheDir, new LimitedAgeDiscCache(cacheDir, CustomizedFileNameGeneratorFactory.createFileNameGenerator(), maxAge));
	}
	
	/**
	 * @param cacheDir          Directory for file caching
	 * @param fileNameGenerator Name generator for cached files
	 * @param maxAge            Max file age (in seconds). If file age will exceed this value then it'll be removed on next
	 *                          treatment (and therefore be reloaded).
	 */	
	public UniversalLimitedAgeDiscCache(File cacheDir,
			FileNameGenerator fileNameGenerator, long maxAge) {
		super(cacheDir, new LimitedAgeDiscCache(cacheDir, CustomizedFileNameGeneratorFactory.createFileNameGenerator(fileNameGenerator), maxAge));
	}
}
