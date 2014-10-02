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

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * This class is copied from com.android.volley.toolbox.DiskBasedCache.CouningInputStream
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
class CountingInputStream extends FilterInputStream {
    int bytesRead = 0;

    public CountingInputStream(InputStream in) {
        super(in);
    }

    @Override
    public int read() throws IOException {
        int result = super.read();
        if (result != -1) {
            bytesRead++;
        }
        return result;
    }

    @Override
    public int read(byte[] buffer, int offset, int len) throws IOException {
        int result = super.read(buffer, offset, len);
        if (result != -1) {
            bytesRead += result;
        }
        return result;
    }
}