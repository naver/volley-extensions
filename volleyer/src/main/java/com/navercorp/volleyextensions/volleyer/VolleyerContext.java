package com.navercorp.volleyextensions.volleyer;

import com.navercorp.volleyextensions.volleyer.request.creator.DefaultRequestCreator;
import com.navercorp.volleyextensions.volleyer.request.creator.RequestCreator;
import com.navercorp.volleyextensions.volleyer.request.executor.DefaultRequestExecutor;
import com.navercorp.volleyextensions.volleyer.request.executor.RequestExecutor;
import com.navercorp.volleyextensions.volleyer.response.parser.IntegratedNetworkResponseParserBuilder;
import com.navercorp.volleyextensions.volleyer.response.parser.Jackson2NetworkResponseParser;
import com.navercorp.volleyextensions.volleyer.response.parser.NetworkResponseParser;
import com.navercorp.volleyextensions.volleyer.response.parser.SimpleXmlNetworkResponseParser;

public class VolleyerContext {
	private RequestCreator requestCreator = new DefaultRequestCreator();
	private RequestExecutor requestExecutor = new DefaultRequestExecutor();
	private NetworkResponseParser defaultNetworkResponseParser = new IntegratedNetworkResponseParserBuilder()
																	.addParser(new Jackson2NetworkResponseParser())
																	.addParser(new SimpleXmlNetworkResponseParser())
																	.build(); 
	public RequestCreator getRequestCreator() {
		return requestCreator;
	}

	public RequestExecutor getRequestExecutor() {
		return requestExecutor;
	}

	public NetworkResponseParser getDefaultNetworkResponseParser() {
		return defaultNetworkResponseParser;
	}
}
