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
package com.navercorp.volleyextensions.cache.universalimageloader.memory;

import java.util.Collection;

import android.graphics.Bitmap;

import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.navercorp.volleyextensions.util.Assert;
import com.nostra13.universalimageloader.cache.memory.MemoryCacheAware;

/**
 * <pre>
 * A wrapper class for {@link MemoryCacheAware}{@literal <String, Bitmap>}.
 * This class can be used as {@link ImageCache} or {@link MemoryCacheAware}{@literal <String, Bitmap>}.
 * </pre>
 * @see ImageCache
 * @see MemoryCacheAware
 * 
 */
public class UniversalImageCache implements ImageCache,
		MemoryCacheAware<String, Bitmap> {

	private final MemoryCacheAware<String, Bitmap> delegate;
	/**
	 * @param delegate Wrapped Memory Cache
	 * @throws NullPointerException if {@code cache} is null
	 */
	public UniversalImageCache(MemoryCacheAware<String, Bitmap> delegate) {
		Assert.notNull(delegate, "delegate");		
		this.delegate = delegate;
	}

	@Override
	public boolean put(String key, Bitmap value) {
		return this.delegate.put(key, value);
	}

	@Override
	public Bitmap get(String key) {
		return this.delegate.get(key);
	}

	@Override
	public void remove(String key) {
		this.delegate.remove(key);
	}

	@Override
	public Collection<String> keys() {
		return this.delegate.keys();
	}

	@Override
	public void clear() {
		this.delegate.clear();
	}

	@Override
	public Bitmap getBitmap(String key) {
		return get(key);
	}

	@Override
	public void putBitmap(String key, Bitmap bitmap) {
		put(key, bitmap);
	}

}
