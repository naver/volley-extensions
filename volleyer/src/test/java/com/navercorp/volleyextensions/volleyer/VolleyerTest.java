package com.navercorp.volleyextensions.volleyer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import android.content.Context;

import com.android.volley.RequestQueue;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class VolleyerTest {
	RequestQueue requestQueue;
	Context context;
	private VolleyerContext volleyerContext;

	@Before
	public void setUp() {
		Volleyer.clear();
		requestQueue = mock(RequestQueue.class);
		context = Robolectric.application.getApplicationContext();
		volleyerContext = mock(VolleyerContext.class);
	}

	@Test
	public void volleyerShouldInitializeWithoutAnyCustomStuffsSuccessfully() {
		// When
		Volleyer.init(context);
		// Then
		assertNotNull(Volleyer.getRequestQueue());
	}

	@Test
	public void volleyerShouldInitializeWithCustomRequestQueueSuccessfully() {
		// When
		Volleyer.init(requestQueue);
		// Then
		verify(requestQueue).start();
	}

	@Test
	public void volleyerShouldInitializeWithCustomRequestQueueAndVolleyerContextSuccessfully() {
		// When
		Volleyer.init(requestQueue, volleyerContext);
		// Then
		verify(requestQueue).start();
	}

	@Test
	public void volleyerShouldIgnoreTheCallWhenItIsAlreadyInitialized() {
		// Given
		RequestQueue otherRequestQueue = mock(RequestQueue.class);
		// When
		Volleyer.init(requestQueue, volleyerContext);
		Volleyer.init(otherRequestQueue, volleyerContext);
		// Then
		verify(otherRequestQueue, never()).start();
	}

	@Test(expected = IllegalStateException.class)
	public void volleyerShouldThrowIllegalStateExceptionWhenNotInitialized() {
		// When & Then
		Volleyer.getRequestQueue();
	}

	@Test(expected = NullPointerException.class)
	public void volleyerShouldThrowNpeWhenContextIsNull() {
		// Given
		Context nullContext = null;
		// When & Then
		Volleyer.init(nullContext);
	}

	@Test(expected = NullPointerException.class)
	public void volleyerShouldThrowNpeWhenRequestQueueIsNull() {
		// Given
		RequestQueue nullRequestQueue = null;
		// When & Then
		Volleyer.init(nullRequestQueue);
	}

	@Test(expected = NullPointerException.class)
	public void volleyerShouldThrowNpeWhenVolleyerContextIsNull() {
		// Given
		VolleyerContext nullVolleyerContext = null;
		// When & Then
		Volleyer.init(requestQueue, nullVolleyerContext);
	}
}
