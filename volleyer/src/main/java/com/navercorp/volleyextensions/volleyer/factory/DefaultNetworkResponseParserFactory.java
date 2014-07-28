package com.navercorp.volleyextensions.volleyer.factory;

import com.navercorp.volleyextensions.volleyer.response.parser.IntegratedNetworkResponseParser;
import com.navercorp.volleyextensions.volleyer.response.parser.Jackson2NetworkResponseParser;
import com.navercorp.volleyextensions.volleyer.response.parser.JacksonNetworkResponseParser;
import com.navercorp.volleyextensions.volleyer.response.parser.NetworkResponseParser;
import com.navercorp.volleyextensions.volleyer.response.parser.SimpleXmlNetworkResponseParser;
import com.navercorp.volleyextensions.volleyer.util.ClassUtils;

public class DefaultNetworkResponseParserFactory {

	private static final String JACKSON_2_X_CLASS_NAME = "com.fasterxml.jackson.databind.ObjectMapper";
	private static final String JACKSON_1_X_CLASS_NAME = "org.codehaus.jackson.map.ObjectMapper";
	private static final String SIMPLE_XML_CLASS_NAME = "org.simpleframework.xml.core.Persister";

	public static NetworkResponseParser create() {
		IntegratedNetworkResponseParser.Builder builder = new IntegratedNetworkResponseParser.Builder();
		addJacksonParserIfPresent(builder);
		addSimpleXmlParserIfPresent(builder);
		return builder.build();
	}

	public static void addJacksonParserIfPresent(IntegratedNetworkResponseParser.Builder builder) {
		if(ClassUtils.isPresent(JACKSON_2_X_CLASS_NAME)) {
			builder.addParser(new Jackson2NetworkResponseParser());
			return;
		}

		if(ClassUtils.isPresent(JACKSON_1_X_CLASS_NAME)) {
			builder.addParser(new JacksonNetworkResponseParser());
			return;
		}

		// Do nothing if Jackson doesn't exist.
	}

	public static void addSimpleXmlParserIfPresent(IntegratedNetworkResponseParser.Builder builder) {
		if(ClassUtils.isPresent(SIMPLE_XML_CLASS_NAME)) {
			builder.addParser(new SimpleXmlNetworkResponseParser());
		}

		// Do nothing if SimpleXml doesn't exist.
	}
}
