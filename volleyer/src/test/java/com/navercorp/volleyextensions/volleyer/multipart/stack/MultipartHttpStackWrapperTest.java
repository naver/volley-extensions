package com.navercorp.volleyextensions.volleyer.multipart.stack;

import java.io.IOException;

import org.junit.Test;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.HttpStack;
import com.navercorp.volleyextensions.volleyer.multipart.stack.MultipartHttpStack;
import com.navercorp.volleyextensions.volleyer.multipart.stack.MultipartHttpStackWrapper;
import com.navercorp.volleyextensions.volleyer.request.VolleyerRequest;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MultipartHttpStackWrapperTest {

	static final MultipartHttpStack mockMultipartHttpStack = mock(MultipartHttpStack.class);
	static final HttpStack mockHttpStack = mock(HttpStack.class);

	@Test(expected = NullPointerException.class)
	public void shouldThrowNpeWhenHttpStackIsNull() {
		new MultipartHttpStackWrapper(null /* httpstack */, mockMultipartHttpStack);
	}

	@Test(expected = NullPointerException.class)
	public void shouldThrowNpeWhenMultipartHttpStackIsNull() {
		new MultipartHttpStackWrapper(mockHttpStack  , null /* multipart httpstack */);
	}

	@Test
	public void shouldCallMultipartHttpStackWhenRequestHasMultipart() throws AuthFailureError, IOException {
		// Given
		VolleyerRequest<?> request = mock(VolleyerRequest.class);
		MultipartHttpStackWrapper wrapper = new MultipartHttpStackWrapper(mockHttpStack  , mockMultipartHttpStack);
		// When
		when(request.hasMultipart()).thenReturn(true);
		wrapper.performRequest(request, null);
		// Then
		verify(mockHttpStack, never()).performRequest(request, null);
		verify(mockMultipartHttpStack).performRequest(request, null);
	}

	@Test
	public void shouldCallHttpStackWhenRequestHasNotMultipart() throws AuthFailureError, IOException {
		// Given
		VolleyerRequest<?> request = mock(VolleyerRequest.class);
		MultipartHttpStackWrapper wrapper = new MultipartHttpStackWrapper(mockHttpStack, mockMultipartHttpStack);
		// When
		when(request.hasMultipart()).thenReturn(false);
		wrapper.performRequest(request, null);
		// Then
		verify(mockHttpStack).performRequest(request, null);
		verify(mockMultipartHttpStack, never()).performRequest(request, null);
	}

	@Test
	public void shouldCallHttpStackWhenRequestIsNotContainer() throws AuthFailureError, IOException {
		// Given
		Request<?> request = mock(Request.class);
		MultipartHttpStackWrapper wrapper = new MultipartHttpStackWrapper(mockHttpStack, mockMultipartHttpStack);
		// When
		wrapper.performRequest(request, null);
		// Then
		verify(mockHttpStack).performRequest(request, null);
		verify(mockMultipartHttpStack, never()).performRequest(request, null);
	}
}
