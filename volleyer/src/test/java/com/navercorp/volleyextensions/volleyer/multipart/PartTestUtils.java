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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.navercorp.volleyextensions.volleyer.multipart.Multipart;
import com.navercorp.volleyextensions.volleyer.multipart.MultipartContainer;
import com.navercorp.volleyextensions.volleyer.multipart.Part;
import com.navercorp.volleyextensions.volleyer.multipart.StringPart;
import com.navercorp.volleyextensions.volleyer.util.StringUtils;

public class PartTestUtils {

	static final Part SAMPLE_STRING_PART = new StringPart("some_string", "Hello World");
	static final int DEFAULT_BUFFER_LENGTH = 1024;

	static void readStream(InputStream is, OutputStream out) throws IOException {
		byte[] buffer = new byte[DEFAULT_BUFFER_LENGTH];
		int readLength = 0;
		while((readLength = is.read(buffer)) != -1) {
			int length = determineLength(readLength);
			out.write(buffer, 0, length);
		}
	}

	private static int determineLength(int readLength) {
		int length;
		if (readLength < DEFAULT_BUFFER_LENGTH) {
			length = readLength;
		} else {
			length = DEFAULT_BUFFER_LENGTH;
		}
		return length;
	}

	public static MultipartContainer createMultipartContainerMock() {
		return createMultipartContainerMock(true);
	}

	public static MultipartContainer createMultipartContainerMock(boolean hasMultipart) {
		return createMultipartContainerMock(hasMultipart, createSampleMultipart());
	}

	public static MultipartContainer createMultipartContainerMock(boolean hasMultipart, Multipart multipart) {
		MultipartContainer mock = mock(MultipartContainer.class);
		when(mock.hasMultipart()).thenReturn(hasMultipart);
		when(mock.getMultipart()).thenReturn(multipart);
		return mock;
	}

	public static Multipart createSampleMultipart() {
		Multipart multipart = new Multipart();
		multipart.add(SAMPLE_STRING_PART);
		return multipart;
	}

	public static File createTestFile(String content) {
		String filename = "test_" + StringUtils.generateRandom(20) + ".txt";
		File testFile = new File(filename);
		try {
			testFile.createNewFile();
			testFile.deleteOnExit();
			writeTestFileContent(testFile, content);
		} catch (IOException e) {
			// Do nothing
		}
		return testFile;
	}

	private static void writeTestFileContent(File file, String content) throws IOException {
		FileWriter writer = null;
		try {
			writer = new FileWriter(file);
			writer.append(content);
		} catch (IOException e) {
			// do nothing
		} finally {
			writer.close();
		}
	}
}
