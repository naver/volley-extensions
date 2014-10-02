/*
 * Copyright (C) 2011 The Android Open Source Project
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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.VolleyLog;
import com.navercorp.volleyextensions.util.Assert;
import com.navercorp.volleyextensions.util.IoUtils;
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
 * 
 * <b>NOTE</b>
 * Codes may need to be updated when {@code DiskBasedCache} on "master" branch of aosp volley is modified.
 * Currently, the last commit I have seen is 
 * "Port CacheHeader away from ObjectOutputStream. by Ficus Kirkpatrick - 9 months ago" 
 * (https://android.googlesource.com/platform/frameworks/volley/+/b33d0d6651b0b31e965839211d410136db2dcb5b)
 * </pre>
 * @see Cache
 * @see DiscCacheAware
 */
class UniversalDiscCache implements Cache {
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

			CacheHeader header = CacheHeader.readHeader(cis); // eat header
			byte[] data = StreamUtils.streamToBytes(cis,
					(int) (file.length() - cis.bytesRead));

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
	/* (non-Javadoc)
	 * 
	 * Don't anything on an initialization process in contrast with DiskBasedCache.
	 * 
	 * There are two reasons why it doesn't.
	 * 
	 * (1) UniversalDiscCache class is a adapter of disc caches of Android Universal Image Loader(UIL).
	 *     This class just delegate responsibilities to the disc caches. And that's it. 
	 * 
	 * (2) To implement initialization stuff like DiskBasedCache does, some data structure should be made for which cache entries are maintained. 
	 *     But the UIL disc caches already maintain those entries. Unavoidably, duplicate maintenance will happen between the disc cahces and this class after implementing.
	 * 
	 * @see com.android.volley.Cache#initialize()
	 */
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

}
