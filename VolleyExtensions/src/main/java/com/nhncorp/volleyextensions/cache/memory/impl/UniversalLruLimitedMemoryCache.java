package com.nhncorp.volleyextensions.cache.memory.impl;


import com.nhncorp.volleyextensions.cache.memory.UniversalImageCache;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
/**
 * <pre>
 * A wrapper class for {@link LRULimitedMemoryCache}
 * 
 * Note that the name of this class is not Universal'LRU...' but Universal'Lru...'. 
 * The reason why the name is was just for keeping the naming style, which is like 'Lru'MemoryCache. 
 * </pre>
 * @see UniversalImageCache
 * @see LRULimitedMemoryCache
 *
 */
public class UniversalLruLimitedMemoryCache extends UniversalImageCache {
	/** @param maxSize Maximum sum of the sizes of the Bitmaps in this cache */
	public UniversalLruLimitedMemoryCache(int maxSize) {
		super(new LRULimitedMemoryCache(maxSize));
	}
}
