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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Test;
import org.mockito.Matchers;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.navercorp.volleyextensions.volleyer.http.ContentType;
import com.navercorp.volleyextensions.volleyer.multipart.AbstractPart;
import com.navercorp.volleyextensions.volleyer.multipart.Part;

public class AbstractPartTest {

	String name = "part";

	@Test(expected = NullPointerException.class)
	public void shouldThrowNpeWhenNameIsNull() {
		// Given
		String nullStr = null;
		// Then
		new TestPart(nullStr);
	}

	@Test
	public void shouldUseOutputStreamWhenWrite() throws IOException {
		// Given
		OutputStream out = mock(OutputStream.class);
		Part part = new TestPart(name);
		// When
		part.write(out);
		// Then
		verify(out, atLeastOnce()).write(Matchers.any(byte[].class));
	}

	@Test
	public void shouldReturnSameName() {
		// Given
		Part part = new TestPart(name);
		// When
		String result = part.getName();
		// Then
		assertThat(result, is(name));
	}

	@Test
	public void shouldCallAllTheAbstractMethodsWhenWrite() throws IOException {
		// Given
		AbstractPart mock = mock(AbstractPart.class);
		OutputStream out = mock(OutputStream.class);
		Part part = new TestPart(name, mock);
		// When
		part.write(out);
		// Then
		verify(mock).getContentType();
		verify(mock).getExtraHeader();
		verify(mock).getContent();
	}

	@Test
	public void shouldCloseInputStream() throws IOException {
		// Given
		final TestInputStream in = new TestInputStream();
		AbstractPart mock = createPartWithInputStream(name, in);
		OutputStream out = mock(OutputStream.class);
		Part part = new TestPart(name, mock);
		// When
		part.write(out);
		// Then
		assertThat(in.isClosed(), is(Boolean.TRUE));
	}

	@Test
	public void shouldWriteExpectedOutput() throws IOException {
		// Given
		String contentString = "test test test";
		byte[] content = contentString.getBytes();
		final InputStream in = new ByteArrayInputStream(content);
		AbstractPart mock = createPartWithInputStream(name, in);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Part part = new TestPart(name, mock);
		String expected = "Content-Disposition: form-data; name=\"" + name + "\"\r\n\r\n" + contentString + "\r\n";
		// When
		part.write(out);
		// Then
		byte[] result = out.toByteArray();
		assertThat(new String(result), is(expected));
	}

	private static AbstractPart createPartWithInputStream(String name, final InputStream in) {
		return new AbstractPart(name){

			@Override
			public InputStream getContent() throws IOException {
				return in;
			}

			@Override
			public ContentType getContentType() {
				return null;
			}

			@Override
			protected byte[] getExtraHeader() {
				return null;
			}
		};
	}

	/**
	 * InputStream class as mock
	 *
	 */
	static class TestInputStream extends InputStream {

		private boolean closed = false;

		@Override
		public int read() throws IOException {
			return -1;
		}

		public void close() {
			this.closed  = true;
		}

		public boolean isClosed() {
			return closed;
		}
	}

	/**
	 * Implementation of AbstractPart for testing
	 *
	 */
	static class TestPart extends AbstractPart {

		private AbstractPart mock;

		public TestPart(String name) {
			this(name, null);
		}

		public TestPart(String name, AbstractPart mock) {
			super(name);
			this.mock = mock;
		}

		@Override
		public ContentType getContentType() {
			if (mock == null) {
				return null;
			}
			return mock.getContentType();
		}

		@Override
		public InputStream getContent() throws IOException {
			if (mock == null) {
				return null;
			}
			return mock.getContent();
		}

		@Override
		protected byte[] getExtraHeader() {
			if (mock == null) {
				return null;
			}
			return mock.getExtraHeader();
		}
	}
}
