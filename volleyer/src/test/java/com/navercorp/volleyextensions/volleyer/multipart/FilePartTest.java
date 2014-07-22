package com.navercorp.volleyextensions.volleyer.multipart;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.junit.BeforeClass;
import org.junit.Test;

import com.navercorp.volleyextensions.volleyer.http.ContentType;
import com.navercorp.volleyextensions.volleyer.multipart.FilePart;
import com.navercorp.volleyextensions.volleyer.multipart.Part;

public class FilePartTest {

	static final String name = "filepart";
	static final String content = "test Test tEst";

	static File file = null;

	@BeforeClass
	public static void setUpOnce() {
		file = PartTestUtils.createTestFile(content);
	}

	@Test(expected = NullPointerException.class)
	public void shouldThrowNpeWhenFileIsNull() {
		new FilePart(null /* file */);
	}

	@Test(expected = NullPointerException.class)
	public void shouldThrowNpeWhenNameIsNull() {
		new FilePart(null /* name */, file);
	}

	@Test
	public void shouldReturnOctetStreamWhenContentTypeIsNull() {
		// Given
		ContentType expected = ContentType.createContentType("application/octet-stream");
		// When
		Part part = new FilePart("name", file, null /* content type */);
		// Then
		assertThat(part.getContentType() , is(expected));
	}

	@Test
	public void nameShouldBeEqualToFilenameIfDefault() {
		// Given
		String filename = "justfortest.txt";
		String path = "/hello/world/" + filename;
		File newFile = new File(path);
		FilePart part = new FilePart(newFile);
		// When
		String result = part.getName();
		// Then
		assertThat(result, is(filename));
	}

	@Test
	public void shouldReturnActualExtraHeader() {
		// Given
		String filename = "justfortest.txt";
		String path = "/hello/world/" + filename;
		File newFile = new File(path);
		FilePart part = new FilePart(newFile);
		String expected = "filename=\"" + filename +"\"";
		// When
		String result = new String(part.getExtraHeader());
		// Then
		assertThat(result, is(expected));
	}

	@Test
	public void shouldReturnSameContentType() {
		// Given
		ContentType contentType = ContentType.CONTENT_TYPE_TEXT_JSON;
		Part part = new FilePart(name, file, contentType);
		// When
		ContentType returnContentType = part.getContentType();
		// Then
		assertThat(contentType, is(returnContentType));
	}

	@Test
	public void shouldReturnSameFilename() {
		// Given
		String filename = "justfortest.txt";
		String path = "/hello/world/" + filename;
		File newFile = new File(path);
		FilePart part = new FilePart(name, newFile);
		// When
		String result = part.getFilename();
		// Then
		assertThat(result, is(filename));
	}

	@Test(expected = IOException.class)
	public void shouldThrowExceptionWhenFileNotExist() throws IOException {
		// Given
		String filename = "justfortest.txt";
		String path = "/hello/world/" + filename;
		File newFile = new File(path);
		Part part = new FilePart(newFile);
		// When
		part.getContent();
	}

	@Test
	public void shouldReturnContentOfFile() throws IOException {
		// Given
		Part part = new FilePart(name, file);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		// When
		InputStream is = part.getContent();
		PartTestUtils.readStream(is, out);
		String result = new String(out.toByteArray());
		// Then
		assertThat(result, is(content));
	}
}
