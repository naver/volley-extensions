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
package com.navercorp.volleyextensions.volleyer.factory;

import com.navercorp.volleyextensions.volleyer.response.parser.IntegratedNetworkResponseParser;
import com.navercorp.volleyextensions.volleyer.response.parser.Jackson2NetworkResponseParser;
import com.navercorp.volleyextensions.volleyer.response.parser.JacksonNetworkResponseParser;
import com.navercorp.volleyextensions.volleyer.response.parser.NetworkResponseParser;
import com.navercorp.volleyextensions.volleyer.response.parser.SimpleXmlNetworkResponseParser;
import com.navercorp.volleyextensions.volleyer.util.ClassUtils;
/**
 * A factory which creates a {@code NetworkResponseParser} instance without any settings.
 *
 * NOTE : This creates a json parser and a xml parser only if the library is imported.
 *        If you want this to create a json parser,
 *        you should import Jackson 1.x library or import Jackson 2.x library.
 *        Or if you want this to create a xml parser, you should import Simple Xml library.
 */
public class DefaultNetworkResponseParserFactory {

	private static final String JACKSON_2_X_CLASS_NAME = "com.fasterxml.jackson.databind.ObjectMapper";
	private static final String JACKSON_1_X_CLASS_NAME = "org.codehaus.jackson.map.ObjectMapper";
	private static final String SIMPLE_XML_CLASS_NAME = "org.simpleframework.xml.core.Persister";
	/**
	 * <pre>
	 * Create a response parser supporting json & xml types.
	 * NOTE : You must import jackson 1.x or 2.x library for supporting json type, or import simple xml library for supporting xml type.  
	 * </pre>
	 * @return {@link IntegratedNetworkResponseParser} instance containing xml or json parser (if each library is present). 
	 */
	public static NetworkResponseParser create() {
		IntegratedNetworkResponseParser.Builder builder = new IntegratedNetworkResponseParser.Builder();
		addJacksonParserIfPresent(builder);
		addSimpleXmlParserIfPresent(builder);
		return builder.build();
	}
	/**
	 * <pre>
	 * Add simple jackson 2.x or 1.x parser if the library is present.
	 * If both of two libraries are present, then jackson 2.x is added instead of 1.x.
	 * NOTE : You can call this method when adding parsers for {@link IntegratedNetworkResponseParser}.
	 * </pre> 
	 * @param builder must not be null
	 */
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
	/**
	 * <pre>
	 * Add simple xml parser if the library is present.
	 * NOTE : You can call this method when adding parsers for {@link IntegratedNetworkResponseParser}.
	 * </pre> 
	 * @param builder must not be null
	 */
	public static void addSimpleXmlParserIfPresent(IntegratedNetworkResponseParser.Builder builder) {
		if(ClassUtils.isPresent(SIMPLE_XML_CLASS_NAME)) {
			builder.addParser(new SimpleXmlNetworkResponseParser());
		}

		// Do nothing if SimpleXml doesn't exist.
	}
}
