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
