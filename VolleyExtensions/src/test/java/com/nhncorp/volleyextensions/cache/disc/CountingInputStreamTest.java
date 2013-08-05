package com.nhncorp.volleyextensions.cache.disc;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class CountingInputStreamTest {

	@Test
	public void bytesReadShouldBeSameAsContentLength() throws IOException {
		// Given
		InputStream is = openFromClassPath("test.txt");
		CountingInputStream cis = new CountingInputStream(is);
		//When
		String content = IOUtils.toString(cis);
		IOUtils.closeQuietly(cis);
		//Then
		assertThat(content, is("Cached Data"));
		assertThat(cis.bytesRead, is(11));
	}

	private InputStream openFromClassPath(String testFile) throws IOException {
		ClassLoader classLoader = this.getClass().getClassLoader();
		return classLoader.getResource(testFile).openStream();
	}
}
