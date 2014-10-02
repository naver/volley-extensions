package com.navercorp.volleyextensions.volleyer.multipart;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import com.navercorp.volleyextensions.volleyer.http.ContentType;
import com.navercorp.volleyextensions.volleyer.multipart.Part;
import com.navercorp.volleyextensions.volleyer.multipart.StringPart;

public class StringPartTest {

	String name = "stringpart";
	String content = "content is here.";
	String charset = "utf-8";

	@Test(expected = NullPointerException.class)
	public void shouldThrowNpeWhenNameIsNull() {
		new StringPart(null /* name */, content);
	}

	@Test(expected = NullPointerException.class)
	public void shouldThrowNpeWhenContentIsNull() {
		new StringPart(name, null /* content */);
	}

	@Test
	public void shouldReturnSameContentType() {
		// Given
		ContentType contentType = ContentType.CONTENT_TYPE_TEXT_JSON;
		Part part = new StringPart(name, content, charset, contentType);
		// When
		ContentType returnContentType = part.getContentType();
		// Then
		assertThat(contentType, is(returnContentType));
	}

	@Test
	public void shouldReturnPlainContentTypeIfDefault() {
		// Given
		ContentType contentType = ContentType.CONTENT_TYPE_TEXT_PLAIN;
		Part part = new StringPart(name, content, charset);
		// When
		ContentType returnContentType = part.getContentType();
		// Then
		assertThat(contentType, is(returnContentType));
	}

	@Test(expected = IOException.class)
	public void shouldThrowIOExceptionWhenInvalidCharset() throws IOException {
		// Given
		String invalidCharset = "@MSADG";
		Part part = new StringPart(name, content, invalidCharset, ContentType.CONTENT_TYPE_TEXT_PLAIN);
		// When
		part.getContent();
	}

	@Test
	public void shouldReturnContentOfString() throws IOException {
		// Given
		Part part = new StringPart(name, content);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		// When
		InputStream is = part.getContent();
		PartTestUtils.readStream(is, out);
		String result = new String(out.toByteArray());
		// Then
		assertThat(result, is(content));
	}
}
