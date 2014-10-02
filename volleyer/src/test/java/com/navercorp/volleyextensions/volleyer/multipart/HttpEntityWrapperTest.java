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
import java.io.OutputStream;

import org.junit.Test;

import com.navercorp.volleyextensions.volleyer.multipart.HttpEntityWrapper;
import com.navercorp.volleyextensions.volleyer.multipart.Writable;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class HttpEntityWrapperTest {

	@Test(expected = NullPointerException.class)
	public void shouldThrowNpeWhenWritableIsNull() {
		new HttpEntityWrapper(null);
	}

	@Test
	public void shouldCallWriteMethod() throws IOException {
		// Given
		Writable writable = mock(Writable.class);
		HttpEntityWrapper wrapper = given(writable);
		OutputStream out = mock(OutputStream.class);
		// When
		wrapper.writeTo(out);
		// Then
		verify(writable).write(out);
	}

	@Test
	public void shouldBeSetAsStreaming() {
		// Given
		HttpEntityWrapper wrapper = given();
		// Then
		assertTrue(wrapper.getContentLength() == -1);
		assertTrue(wrapper.isChunked());
		assertTrue(wrapper.isStreaming());
	}

	@Test
	public void shouldBeRepeatable() {
		// Given
		HttpEntityWrapper wrapper = given();
		// Then
		assertTrue(wrapper.isRepeatable());
	}

	private static HttpEntityWrapper given() {
		Writable writable = mock(Writable.class);
		return given(writable);
	}

	private static HttpEntityWrapper given(Writable writable) {
		HttpEntityWrapper wrapper = new HttpEntityWrapper(writable);
		return wrapper;
	}
}
