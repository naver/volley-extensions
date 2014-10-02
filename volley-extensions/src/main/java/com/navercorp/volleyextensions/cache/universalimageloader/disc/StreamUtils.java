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

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
/**
 * <pre>
 * A util class providing stream handling methods
 * 
 * For internal use only.
 * 
 * <b>NOTE</b>
 * The methods are extracted from {@code CacheHeader} and {@code UniversalDiscCache}.
 * 
 * Codes may need to be updated when {@code DiskBasedCache} on "master" branch of aosp volley is modified.
 * Currently, the last commit I have seen is 
 * "Port CacheHeader away from ObjectOutputStream. by Ficus Kirkpatrick - 9 months ago" 
 * (https://android.googlesource.com/platform/frameworks/volley/+/b33d0d6651b0b31e965839211d410136db2dcb5b)
 * 
 * Methods from {@code CacheHeader} : {@link #read(InputStream)}, {@link #writeInt(OutputStream, int)}, {@link #readInt(InputStream)}, {@link #writeLong(OutputStream, long)}, 
 * {@link #readLong(InputStream)},{@link #writeString(OutputStream, String)}, {@link #readString(InputStream)}, {@link #writeStringStringMap(Map, OutputStream)},
 * {@link #readStringStringMap(InputStream)}
 * 
 * Methods from {@code UniversalDiscCache} : {@link #streamToBytes(InputStream, int)}
 * </pre>
 * @author Wonjun Kim
 *
 */
class StreamUtils {
	/*
	 * Homebrewed simple serialization system used for reading and writing cache
	 * headers on disk. Once upon a time, this used the standard Java
	 * Object{Input,Output}Stream, but the default implementation relies heavily
	 * on reflection (even for standard types) and generates a ton of garbage.
	 */

	/**
	 * Simple wrapper around {@link InputStream#read()} that throws EOFException
	 * instead of returning -1.
	 */
	static int read(InputStream is) throws IOException {
		int b = is.read();
		if (b == -1) {
			throw new EOFException();
		}
		return b;
	}

	static void writeInt(OutputStream os, int n) throws IOException {
		os.write((n >> 0) & 0xff);
		os.write((n >> 8) & 0xff);
		os.write((n >> 16) & 0xff);
		os.write((n >> 24) & 0xff);
	}

	static int readInt(InputStream is) throws IOException {
		int n = 0;
		n |= (read(is) << 0);
		n |= (read(is) << 8);
		n |= (read(is) << 16);
		n |= (read(is) << 24);
		return n;
	}

	static void writeLong(OutputStream os, long n) throws IOException {
		os.write((byte)(n >>> 0));
		os.write((byte)(n >>> 8));
		os.write((byte)(n >>> 16));
		os.write((byte)(n >>> 24));
		os.write((byte)(n >>> 32));
		os.write((byte)(n >>> 40));
		os.write((byte)(n >>> 48));
		os.write((byte)(n >>> 56));
	}

	static long readLong(InputStream is) throws IOException {
		long n = 0;
		n |= ((read(is) & 0xFFL) << 0);
		n |= ((read(is) & 0xFFL) << 8);
		n |= ((read(is) & 0xFFL) << 16);
		n |= ((read(is) & 0xFFL) << 24);
		n |= ((read(is) & 0xFFL) << 32);
		n |= ((read(is) & 0xFFL) << 40);
		n |= ((read(is) & 0xFFL) << 48);
		n |= ((read(is) & 0xFFL) << 56);
		return n;
	}

	static void writeString(OutputStream os, String s) throws IOException {
		byte[] b = s.getBytes("UTF-8");
		writeLong(os, b.length);
		os.write(b, 0, b.length);
	}

	static String readString(InputStream is) throws IOException {
		int n = (int) readLong(is);
		byte[] b = streamToBytes(is, n);
		return new String(b, "UTF-8");
	}

	static void writeStringStringMap(Map<String, String> map, OutputStream os) throws IOException {
		if (map != null) {
			writeInt(os, map.size());
			for (Map.Entry<String, String> entry : map.entrySet()) {
				writeString(os, entry.getKey());
				writeString(os, entry.getValue());
			}
		} else {
			writeInt(os, 0);
		}
	}

	static Map<String, String> readStringStringMap(InputStream is) throws IOException {
		int size = readInt(is);
		Map<String, String> result = (size == 0)
				? Collections.<String, String>emptyMap()
				: new HashMap<String, String>(size);
		for (int i = 0; i < size; i++) {
			String key = readString(is).intern();
			String value = readString(is).intern();
			result.put(key, value);
		}
		return result;
	}

	/**
	 * Reads the contents of an InputStream into a byte[].
	 * 
	 * */
	static byte[] streamToBytes(InputStream in, int length) throws IOException {
		byte[] bytes = new byte[length];
		int count;
		int pos = 0;
		while (pos < length && ((count = in.read(bytes, pos, length - pos)) != -1)) {
			pos += count;
		}
		if (pos != length) {
			throw new IOException("Expected " + length + " bytes, read " + pos + " bytes");
		}
		return bytes;
	}
}
