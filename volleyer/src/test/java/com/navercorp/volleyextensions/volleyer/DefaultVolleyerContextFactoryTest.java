package com.navercorp.volleyextensions.volleyer;

import static org.junit.Assert.*;

import org.junit.Test;

import com.navercorp.volleyextensions.volleyer.DefaultVolleyerContextFactory;
import com.navercorp.volleyextensions.volleyer.VolleyerContext;
import com.navercorp.volleyextensions.volleyer.request.creator.DefaultRequestCreator;
import com.navercorp.volleyextensions.volleyer.request.executor.DefaultRequestExecutor;
import com.navercorp.volleyextensions.volleyer.response.parser.IntegratedNetworkResponseParser;

public class DefaultVolleyerContextFactoryTest {

	@Test
	public void volleyerContextShouldContain() {
		// When
		VolleyerContext volleyerContext = DefaultVolleyerContextFactory.create();
		// Then
		assertEquals(volleyerContext.getDefaultNetworkResponseParser().getClass(), IntegratedNetworkResponseParser.class);
		assertEquals(volleyerContext.getDefaultListener(), DefaultVolleyerContextFactory.createListener());
		assertEquals(volleyerContext.getDefaultErrorListener(), DefaultVolleyerContextFactory.createErrorListener());
		assertEquals(volleyerContext.getRequestCreator().getClass(), DefaultRequestCreator.class);
		assertEquals(volleyerContext.getRequestExecutor().getClass(), DefaultRequestExecutor.class);
	}
}
