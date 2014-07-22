package com.navercorp.volleyextensions.volleyer.multipart;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;

import com.navercorp.volleyextensions.volleyer.util.Assert;

public class HttpEntityWrapper implements HttpEntity {

	private static final int UNKNOWN_LENGTH = -1;

	private Writable writable;

	public HttpEntityWrapper(Writable writable) {
		Assert.notNull(writable, "Writable");
		this.writable = writable;
	}

	@Override
	public boolean isRepeatable() {
		return true;
	}

	@Override
	public boolean isChunked() {
		return true;
	}

	@Override
	public long getContentLength() {
		return UNKNOWN_LENGTH;
	}

	@Override
	public Header getContentType() {
		return null;
	}

	@Override
	public Header getContentEncoding() {
		return null;
	}

	@Override
	public InputStream getContent() throws IOException, IllegalStateException {
		return null;
	}

	@Override
	public void writeTo(OutputStream outstream) throws IOException {
		writable.write(outstream);
	}

	@Override
	public boolean isStreaming() {
		return true;
	}

	@Override
	public void consumeContent() throws IOException {
		// Do nothing
	}

}
