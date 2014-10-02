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
package com.navercorp.volleyextensions.cache.universalimageloader.disc;

import java.io.File;

import com.android.volley.VolleyLog;
import com.navercorp.volleyextensions.util.Assert;
import com.nostra13.universalimageloader.cache.disc.DiscCacheAware;

/**
 * <pre>
 * A sub class of UniversalDiscCache. 
 * This class makes a cache directory automatically( by being called {@link #initialize}() from {@link com.android.volley.CacheDispatcher}).
 * 
 * NOTE: This class is abstract. If you want to use this class, 
 * you must override and let {@code cacheDir} be the same with {@link DiscCacheAware}'s {@code cacheDir}.
 * 
 *  ex) class UniversalSubDiscCache extends UniversalBaseDiscCache {
 *        public UniversalSubDiscCache(File cacheDir) {
 *		 super( cacheDir, new SomeDiscCacheFromAUIL(cacheDir) );
 *        }
 *  } 
 *  </pre>
 * 
 * @see UniversalDiscCache
 */
public abstract class UniversalBaseDiscCache extends UniversalDiscCache {

	private File cacheDir;

	/**
	 * Constructor of UniversalBaseDiscCache needs parameters which are cache
	 * directory and DiscCacheAware. if references are missing, this constructor
	 * throws NullPointerException.
	 * 
	 * @param cacheDir Directory for file caching 
	 * @param delegate Wrapped Disc Cache
	 * @throws NullPointerException if the {@code cacheDir} is null
	 * @see DiscCacheAware
	 */
	protected UniversalBaseDiscCache(File cacheDir, DiscCacheAware delegate) {
		super(delegate);
		Assert.notNull(cacheDir, "cacheDir");
		this.cacheDir = cacheDir;
	}

	/**
	 * <pre>
	 * Make a cache directory if it doesn't exist. 
	 * This method is basically being called from {@link com.android.volley.CacheDispatcher}.
	 * </pre>
	 * @see com.android.volley.toolbox.DiskBasedCache#initialize
	 */
	@Override
	public synchronized void initialize() {
		if (cacheDir.exists()) {
			return;
		}
		if (!cacheDir.mkdir()) {
			VolleyLog.e("Unable to create cache dir %s",
					cacheDir.getAbsolutePath());
		}
	}
}
