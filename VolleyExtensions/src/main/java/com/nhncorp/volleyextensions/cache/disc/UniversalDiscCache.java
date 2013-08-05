package com.nhncorp.volleyextensions.cache.disc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.VolleyLog;
import com.nhncorp.volleyextensions.util.Assert;
import com.nhncorp.volleyextensions.util.IoUtils;
import com.nostra13.universalimageloader.cache.disc.DiscCacheAware;

/**
 * <pre>
 * A wrapper class for {@link DiscCacheAware}.
 * 
 * When you try to use this class, you need to check whether concrete class of
 * discCacheAware which will be wrapped by this class can be shared.
 * 
 * But, don't care about it, because built-in caches in AUIL are all safe to be
 * shared.
 * </pre>
 * @see Cache
 * @see DiscCacheAware
 */
public class UniversalDiscCache implements Cache {
	private static final String TAG = UniversalDiscCache.class.getSimpleName();

	private final DiscCacheAware delegate;
	/** @param delegate Wrapped DiscCacheAware */
	public UniversalDiscCache(DiscCacheAware delegate) {
		Assert.notNull(delegate, "delegate");		
		this.delegate = delegate;
	}

	@Override
	public void clear() {
		this.delegate.clear();
	}

	@Override
	public Entry get(String key) {
		if (key == null) {
			return null;
		}

		File file = this.delegate.get(key);
		if (file == null || !file.exists()) {
			return null;
		}
		CountingInputStream cis = null;
		try {
			cis = new CountingInputStream(new FileInputStream(file));

			CacheHeader.readHeader(cis); // eat header
			byte[] data = streamToBytes(cis,
					(int) (file.length() - cis.bytesRead));

			CacheHeader header = new CacheHeader();
			return header.toCacheEntry(data);
		} catch (IOException e) {
			VolleyLog.e(e, "Exception in file path %s", file.getAbsolutePath());
			remove(key);
			return null;
		} finally {
			IoUtils.closeQuietly(cis);
		}
	}

	@Override
	public void put(String key, Entry entry) {
		if (key == null) {
			return;
		}
		FileOutputStream fos = null;
		try {
			File file = this.delegate.get(key);
			fos = new FileOutputStream(file);
			CacheHeader header = new CacheHeader(key, entry);
			header.writeHeader(fos);
			fos.write(entry.data);
			this.delegate.put(key, file);
			return;
		} catch (IOException e) {
			Log.e(TAG, "fail to put :" + key, e);
		} finally {
			IoUtils.closeQuietly(fos);
		}
	}

	@Override
	public void initialize() {
		// do nothing
	}

	@Override
	public void invalidate(String key, boolean arg1) {
		// do nothing
	}

	@Override
	public void remove(String key) {
		// do nothing
	}

	/**
	 * Reads the contents of an InputStream into a byte[].
	 * */
	private byte[] streamToBytes(InputStream in, int length)
			throws IOException {
		byte[] bytes = new byte[length];
		int count;
		int pos = 0;
		while (pos < length
				&& ((count = in.read(bytes, pos, length - pos)) != -1)) {
			pos += count;
		}
		if (pos != length) {
			throw new IOException("Expected " + length + " bytes, read " + pos
					+ " bytes");
		}
		return bytes;
	}
}
