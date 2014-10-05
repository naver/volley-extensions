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
		super(cacheDir, new FileCountLimitedDiscCache(cacheDir, CustomizedFileNameGeneratorFactory.createFileNameGenerator(), maxFileCount));
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
		super(cacheDir, new FileCountLimitedDiscCache(cacheDir, CustomizedFileNameGeneratorFactory.createFileNameGenerator(fileNameGenerator), maxFileCount));
	}

}
