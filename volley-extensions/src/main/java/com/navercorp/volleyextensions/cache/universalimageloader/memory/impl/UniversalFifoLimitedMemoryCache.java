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
package com.navercorp.volleyextensions.cache.universalimageloader.memory.impl;

import com.navercorp.volleyextensions.cache.universalimageloader.memory.UniversalImageCache;
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
