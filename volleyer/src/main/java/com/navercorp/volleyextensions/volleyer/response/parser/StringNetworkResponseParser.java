package com.navercorp.volleyextensions.volleyer.response.parser;

import java.io.UnsupportedEncodingException;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.navercorp.volleyextensions.volleyer.util.Assert;

public class StringNetworkResponseParser implements NetworkResponseParser {

	public StringNetworkResponseParser() {
	}

	protected final String getBodyString(NetworkResponse response) throws UnsupportedEncodingException {
		return new String(response.data, HttpHeaderParser.parseCharset(response.headers));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Response<T> parseNetworkResponse(NetworkResponse response, Class<T> clazz) {
		Assert.notNull(response, "The response");
		Assert.notNull(clazz, "The class token");
		try {
			String result = getBodyString(response);
			return (Response<T>) Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		}
	}
}