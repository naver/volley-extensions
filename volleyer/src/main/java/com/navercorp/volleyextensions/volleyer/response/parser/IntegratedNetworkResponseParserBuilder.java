package com.navercorp.volleyextensions.volleyer.response.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.navercorp.volleyextensions.volleyer.http.ContentType;
import com.navercorp.volleyextensions.volleyer.http.ContentTypes;
import com.navercorp.volleyextensions.volleyer.util.Assert;

public class IntegratedNetworkResponseParserBuilder {

	private final Map<ContentType, NetworkResponseParser> parsers = new HashMap<ContentType, NetworkResponseParser>();

	public IntegratedNetworkResponseParserBuilder addParser(TypedNetworkResponseParser typedParser) {
		Assert.notNull(typedParser, "TypedNetworkResponseParser");

		ContentTypes contentTypes = typedParser.getContentTypes();
		addParserForContentTypes(typedParser, contentTypes);

		return this;
	}

	private void addParserForContentTypes(TypedNetworkResponseParser typedParser, ContentTypes contentTypes) {
		List<ContentType> contentTypeList = contentTypes.getListOfContentTypes();
		for(ContentType contentType : contentTypeList) {
			addParser(contentType, typedParser);
		}
	}

	public IntegratedNetworkResponseParserBuilder addParser(ContentType contentType, NetworkResponseParser parser) {
		Assert.notNull(contentType, "ContentType");
		Assert.notNull(parser, "NetworkResponseParser");

		parsers.put(contentType, parser);

		return this;
	}

	public IntegratedNetworkResponseParser build() {
		return new IntegratedNetworkResponseParser(parsers);
	}
}
