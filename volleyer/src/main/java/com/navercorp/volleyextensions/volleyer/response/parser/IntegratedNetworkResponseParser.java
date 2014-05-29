package com.navercorp.volleyextensions.volleyer.response.parser;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.navercorp.volleyextensions.volleyer.exception.UnsupportedContentTypeException;
import com.navercorp.volleyextensions.volleyer.http.ContentType;
import com.navercorp.volleyextensions.volleyer.util.Assert;

public class IntegratedNetworkResponseParser implements NetworkResponseParser {

	private static final String CONTENT_TYPE_HEADER_KEY = "Content-Type";
	private static final NetworkResponseParser STRING_NETWORK_RESPONSE_PARSER = new StringNetworkResponseParser();

	private final Map<ContentType, NetworkResponseParser> parsers = new HashMap<ContentType, NetworkResponseParser>(); 

	IntegratedNetworkResponseParser(Map<ContentType, NetworkResponseParser> parsers) {
		this.parsers.putAll(parsers);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Response<T> parseNetworkResponse(NetworkResponse response, Class<T> clazz) {
		Assert.notNull(response, "NetworkResponse");
		Assert.notNull(clazz, "Target class token");

		// Use StringNetworkResponseParser if target class is String
		if (clazz == String.class) {
			return STRING_NETWORK_RESPONSE_PARSER.parseNetworkResponse(response, clazz);
		}

		// Return the response without using any other parser if target class is NetworkResponse
		if (clazz == NetworkResponse.class) {
			return (Response<T>) Response.success(response, HttpHeaderParser.parseCacheHeaders(response));
		}

		// Get a content type string from the response header
		String contentTypeString = getResponseHeader(response, CONTENT_TYPE_HEADER_KEY);
		// Throw an error if content type is null
		if (contentTypeString == null) {
			return Response.error(new ParseError(new UnsupportedContentTypeException("It cannot find any response parser, "
					+ "because the response content type is null.")));
		}

		// Create a content type instance
		ContentType contentType = ContentType.createContentType(contentTypeString);
		// Get a network response parser from parsers collection
		NetworkResponseParser responseParser = parsers.get(contentType);
		// Throw an error if it cannot find any response parser
		if (responseParser == null) {
			return Response.error(new ParseError(new UnsupportedContentTypeException("It cannot find any response parser "
					+ "for the response content type.")));	
		}

		// Parse the response and return it
		return responseParser.parseNetworkResponse(response, clazz);
	}

	protected String getResponseHeader(NetworkResponse response, String headerKey) {
		return response.headers.get(headerKey);
	}
}
