package com.nhncorp.volleyextensions.cache.disc;

import java.io.File;

import com.android.volley.VolleyLog;
import com.nhncorp.volleyextensions.util.Assert;
import com.nostra13.universalimageloader.cache.disc.DiscCacheAware;

/**
 * <pre>
 * A sub class of UniversalDiscCache. 
 * This class makes a cache directory automatically( by being called {@link #initialize}() from {@link CacheDispatcher}).
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
	 * This method is basically being called from {@link CacheDispatcher}.
	 * </pre>
	 * @see DiskBasedCache#initialize
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
