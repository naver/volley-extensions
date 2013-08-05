package com.nhncorp.volleyextensions.cache.disc;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * This class is copied from com.android.volley.toolbox.DiskBasedCache.CouningInputStream
 * because the original class is private.
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