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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import com.navercorp.volleyextensions.volleyer.http.ContentType;
import com.navercorp.volleyextensions.volleyer.util.Assert;
/**
 * A part class for string.
 */
public class StringPart extends AbstractPart {

	private static final String DEFAULT_CHARSET = "utf-8";
	private static final ContentType PLAIN_TEXT = ContentType.CONTENT_TYPE_TEXT_PLAIN;
	private final String content;
	private final String charset;
	private ContentType contentType;
	/**
	 * Constructor with default charset and default content type.
	 * @param name part name
	 * @param content string of a content
	 */
	public StringPart(String name, String content) {
		this(name, content, DEFAULT_CHARSET);
	}
	/**
	 * Constructor with custom charset and default content type.
	 * @param name part name
	 * @param content string of a content
	 * @param charset character set of a content
	 */
	public StringPart(String name, String content, String charset) {
		this(name, content, DEFAULT_CHARSET, PLAIN_TEXT);
	}
	/**
	 * Constructor with custom charset and custom content type
	 * @param name part name
	 * @param content string of a content
	 * @param charset character set of a content
	 * @param contentType content type of a content
	 */
	public StringPart(String name, String content, String charset, ContentType contentType) {
		super(name);
		Assert.notNull(content, "Content");

		if (charset == null) {
			charset = DEFAULT_CHARSET;
		}
		this.content = content;
		this.charset = charset;
		this.contentType = contentType;
	}

	@Override
	public ContentType getContentType() {
		return contentType;
	}

	@Override
	protected byte[] getExtraHeader() {
		// Do nothing
		return null;
	}

	@Override
	public InputStream getContent() throws IOException {
		byte[] bytes = getBytesFromContent();
		if (bytes == null) {
			return null;
		}

		InputStream is = new ByteArrayInputStream(bytes);
		return is;
	}

	private byte[] getBytesFromContent() throws IOException {
		byte[] bytes = null;
		try {
			bytes = content.getBytes(charset);
		} catch (UnsupportedEncodingException e) {
			throw new IOException("The character set, "+ charset + " is not supported.");
		}

		return bytes;
	}
}
