package com.navercorp.volleyextensions.volleyer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import com.android.volley.RequestQueue;
import com.navercorp.volleyextensions.volleyer.DefaultVolleyerContextFactory;
import com.navercorp.volleyextensions.volleyer.VolleyerContext;
import com.navercorp.volleyextensions.volleyer.request.creator.DefaultRequestCreator;
import com.navercorp.volleyextensions.volleyer.request.executor.DefaultRequestExecutor;
import com.navercorp.volleyextensions.volleyer.response.parser.IntegratedNetworkResponseParser;

public class DefaultVolleyerContextFactoryTest {
	RequestQueue requestQueue = mock(RequestQueue.class);

	@Test(expected = NullPointerException.class)
	public void factoryShouldThrowNpeWhenRequestQueueIsNull() {
		// Given
		RequestQueue nullRequestQueue = null;
		// When & Then
		DefaultVolleyerContextFactory.create(null);
	}

	@Test
	public void volleyerContextShouldContain() {
		// When
		VolleyerContext volleyerContext = DefaultVolleyerContextFactory.create(requestQueue);
		// Then
		assertEquals(volleyerContext.getDefaultNetworkResponseParser().getClass(), IntegratedNetworkResponseParser.class);
		assertEquals(volleyerContext.getDefaultListener(), DefaultVolleyerContextFactory.createListener());
		assertEquals(volleyerContext.getDefaultErrorListener(), DefaultVolleyerContextFactory.createErrorListener());
		assertEquals(volleyerContext.getRequestCreator().getClass(), DefaultRequestCreator.class);
		assertEquals(volleyerContext.getRequestExecutor().getClass(), DefaultRequestExecutor.class);
	}
}
