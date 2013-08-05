package com.nhncorp.volleyextensions.cache.memory.impl;

import com.nhncorp.volleyextensions.cache.memory.UniversalImageCache;
import com.nostra13.universalimageloader.cache.memory.impl.FIFOLimitedMemoryCache;
/**
 * <pre>
 * A wrapper class for {@link FIFOLimitedMemoryCache}
 * 
 * Note that the name of this class is not Universal'FIFO...' but Universal'Fifo...'. 
 * The reason why the name is was just for keeping the naming style, which is like 'Lru'MemoryCache.
 * </pre> 
 * @see UniversalImageCache
 * @see FIFOLimitedMemoryCache
 *
 */
public class UniversalFifoLimitedMemoryCache extends UniversalImageCache {
	/** @param sizeLimit Maximum size for cache (in bytes) */
	public UniversalFifoLimitedMemoryCache(int sizeLimit) {
		super(new FIFOLimitedMemoryCache(sizeLimit));
	}
}
