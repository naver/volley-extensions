package com.navercorp.volleyextensions.volleyer.multipart;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import com.navercorp.volleyextensions.volleyer.http.ContentType;
import com.navercorp.volleyextensions.volleyer.util.Assert;

public class StringPart extends AbstractPart {

	private static final String DEFAULT_CHARSET = "utf-8";
	private static final ContentType PLAIN_TEXT = ContentType.CONTENT_TYPE_TEXT_PLAIN;
	private final String content;
	private final String charset;
	private ContentType contentType;

	public StringPart(String name, String content) {
		this(name, content, DEFAULT_CHARSET);
	}

	public StringPart(String name, String content, String charset) {
		this(name, content, DEFAULT_CHARSET, PLAIN_TEXT);
	}

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
