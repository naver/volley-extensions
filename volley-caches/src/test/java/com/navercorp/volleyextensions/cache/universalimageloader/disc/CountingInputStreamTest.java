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
package com.navercorp.volleyextensions.cache.universalimageloader.disc;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.navercorp.volleyextensions.cache.universalimageloader.disc.CountingInputStream;

public class CountingInputStreamTest {

	@Test
	public void bytesReadShouldBeSameAsContentLength() throws IOException {
		// Given
		InputStream is = openFromClassPath("test.txt");
		CountingInputStream cis = new CountingInputStream(is);
		// When
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
