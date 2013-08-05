package com.nhncorp.volleyextensions.cache.memory;

import java.util.Collection;

import android.graphics.Bitmap;

import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.nhncorp.volleyextensions.util.Assert;
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
