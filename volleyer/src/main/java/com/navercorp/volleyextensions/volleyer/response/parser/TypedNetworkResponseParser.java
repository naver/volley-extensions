package com.navercorp.volleyextensions.volleyer.response.parser;

import com.navercorp.volleyextensions.volleyer.http.ContentTypes;

public interface TypedNetworkResponseParser extends NetworkResponseParser {
	ContentTypes getContentTypes();
}
