package com.navercorp.volleyextensions.volleyer;

import static org.junit.Assert.*;

import org.junit.Test;

import com.navercorp.volleyextensions.volleyer.DefaultVolleyerConfigurationFactory;
import com.navercorp.volleyextensions.volleyer.VolleyerConfiguration;
import com.navercorp.volleyextensions.volleyer.request.creator.DefaultRequestCreator;
import com.navercorp.volleyextensions.volleyer.request.executor.DefaultRequestExecutor;
import com.navercorp.volleyextensions.volleyer.response.parser.IntegratedNetworkResponseParser;

public class DefaultVolleyerConfigurationFactoryTest {

	@Test
	public void volleyerConfigurationShouldContain() {
		// When
		VolleyerConfiguration configuration = DefaultVolleyerConfigurationFactory.create();
		// Then
		assertEquals(configuration.getDefaultNetworkResponseParser().getClass(), IntegratedNetworkResponseParser.class);
		assertEquals(configuration.getDefaultErrorListener(), DefaultVolleyerConfigurationFactory.createErrorListener());
		assertEquals(configuration.getRequestCreator().getClass(), DefaultRequestCreator.class);
		assertEquals(configuration.getRequestExecutor().getClass(), DefaultRequestExecutor.class);
	}
}
