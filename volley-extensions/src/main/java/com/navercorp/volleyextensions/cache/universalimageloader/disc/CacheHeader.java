/*
 * Copyright (C) 2011 The Android Open Source Project
 * Copyright (C) 2014 Naver Business Platform Corp.
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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.Cache.Entry;
import com.android.volley.VolleyLog;

/**
 * Handles holding onto the cache headers for an entry.
 * 
 * This class is copied from com.android.volley.toolbox.DiskBasedCache.CacheHeader
 * because the original class is private.
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

	/** Current cache version */
	private static final int CACHE_VERSION = 2;

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
		ObjectInputStream ois = new ObjectInputStream(is);
		int version = ois.readByte();
		if (version != CACHE_VERSION) {
			// don't bother deleting, it'll get pruned eventually
			throw new IOException();
		}
		entry.key = ois.readUTF();
		entry.etag = ois.readUTF();
		if (entry.etag.equals("")) {
			entry.etag = null;
		}
		entry.serverDate = ois.readLong();
		entry.ttl = ois.readLong();
		entry.softTtl = ois.readLong();
		entry.responseHeaders = readStringStringMap(ois);
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
			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.writeByte(CACHE_VERSION);
			oos.writeUTF(key);
			oos.writeUTF(etag == null ? "" : etag);
			oos.writeLong(serverDate);
			oos.writeLong(ttl);
			oos.writeLong(softTtl);
			writeStringStringMap(responseHeaders, oos);
			oos.flush();
			return true;
		} catch (IOException e) {
			VolleyLog.d("%s", e.toString());
			return false;
		}
	}

	/**
	 * Writes all entries of {@code map} into {@code oos}.
	 */
	private static void writeStringStringMap(Map<String, String> map,
			ObjectOutputStream oos) throws IOException {
		if (map != null) {
			oos.writeInt(map.size());
			for (Map.Entry<String, String> entry : map.entrySet()) {
				oos.writeUTF(entry.getKey());
				oos.writeUTF(entry.getValue());
			}
		} else {
			oos.writeInt(0);
		}
	}

	/**
	 * @return a string to string map which contains the entries read from
	 *         {@code ois} previously written by {@link #writeStringStringMap}
	 */
	private static Map<String, String> readStringStringMap(ObjectInputStream ois)
			throws IOException {
		int size = ois.readInt();
		Map<String, String> result = (size == 0) ? Collections
				.<String, String> emptyMap()
				: new HashMap<String, String>(size);
		for (int i = 0; i < size; i++) {
			String key = ois.readUTF().intern();
			String value = ois.readUTF().intern();
			result.put(key, value);
		}
		return result;
	}
}
