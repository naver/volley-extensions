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
package com.navercorp.volleyextensions.volleyer.multipart;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;

import com.navercorp.volleyextensions.volleyer.util.Assert;
/**
 * {@code HttpEntity} wrapper for sending {@code Multipart}.
 *
 * @see MultipartHttpClientStack
 */
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
	/**
	 * Set a chunked encoding as a default.
	 */
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
