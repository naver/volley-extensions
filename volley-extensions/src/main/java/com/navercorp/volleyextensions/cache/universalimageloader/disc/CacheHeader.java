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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import com.android.volley.Cache.Entry;
import com.android.volley.VolleyLog;

/**
 * Handles holding onto the cache headers for an entry.
 * 
 * This class is copied from com.android.volley.toolbox.DiskBasedCache.CacheHeader
 * because the original class is private.
 * <pre>
 * <b>NOTE</b>
 * Codes may need to be updated when {@code DiskBasedCache} on "master" branch of aosp volley is modified.
 * Currently, the last commit I have seen is 
 * "Port CacheHeader away from ObjectOutputStream. by Ficus Kirkpatrick - 9 months ago" 
 * (https://android.googlesource.com/platform/frameworks/volley/+/b33d0d6651b0b31e965839211d410136db2dcb5b)
 * </pre>
 * @author wonjun.kim 
 * @author sanghyuk.jung
 */
class CacheHeader {
	/**
	 * The size of the data identified by this CacheHeader. (This is not
	 * serialized to disk.
	 */
	public long size;

	/** The key that identifies the cache entry. */
	public String key;

	/** ETag for cache coherence. */
	public String etag;

	/** Date of this response as reported by the server. */
	public long serverDate;

	/** TTL for this record. */
	public long ttl;

	/** Soft TTL for this record. */
	public long softTtl;

	/** Headers from the response resulting in this cache entry. */
	public Map<String, String> responseHeaders;

	/** Magic number for current version of cache file format. */
	private static final int CACHE_MAGIC = 0x20120504;

	CacheHeader() {
	}

	/**
	 * Instantiates a new CacheHeader object
	 * 
	 * @param key
	 *            The key that identifies the cache entry
	 * @param entry
	 *            The cache entry.
	 */
	public CacheHeader(String key, Entry entry) {
		this.key = key;
		this.size = entry.data.length;
		this.etag = entry.etag;
		this.serverDate = entry.serverDate;
		this.ttl = entry.ttl;
		this.softTtl = entry.softTtl;
		this.responseHeaders = entry.responseHeaders;
	}

	/**
	 * Reads the header off of an InputStream and returns a CacheHeader object.
	 * 
	 * @param is
	 *            The InputStream to read from.
	 * @throws IOException
	 */
	public static CacheHeader readHeader(InputStream is) throws IOException {
		CacheHeader entry = new CacheHeader();
		int magic = StreamUtils.readInt(is);
		if (magic != CACHE_MAGIC) {
			// don't bother deleting, it'll get pruned eventually
			throw new IOException();
		}
		entry.key = StreamUtils.readString(is);
		entry.etag = StreamUtils.readString(is);
		if (entry.etag.equals("")) {
			entry.etag = null;
		}
		entry.serverDate = StreamUtils.readLong(is);
		entry.ttl = StreamUtils.readLong(is);
		entry.softTtl = StreamUtils.readLong(is);
		entry.responseHeaders = StreamUtils.readStringStringMap(is);
		return entry;
	}

	/**
	 * Creates a cache entry for the specified data.
	 */
	public Entry toCacheEntry(byte[] data) {
		Entry entry = new Entry();
		entry.data = data;
		entry.etag = this.etag;
		entry.serverDate = this.serverDate;
		entry.ttl = this.ttl;
		entry.softTtl = this.softTtl;
		entry.responseHeaders = this.responseHeaders;
		return entry;
	}

	/**
	 * Writes the contents of this CacheHeader to the specified OutputStream.
	 */
	public boolean writeHeader(OutputStream os) {
		try {
			StreamUtils.writeInt(os, CACHE_MAGIC);
			StreamUtils.writeString(os, key);
			StreamUtils.writeString(os, etag == null ? "" : etag);
			StreamUtils.writeLong(os, serverDate);
			StreamUtils.writeLong(os, ttl);
			StreamUtils.writeLong(os, softTtl);
			StreamUtils.writeStringStringMap(responseHeaders, os);
			os.flush();
			return true;
		} catch (IOException e) {
			VolleyLog.d("%s", e.toString());
			return false;
		}
	}


}
