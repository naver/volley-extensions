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
package com.navercorp.volleyextensions.volleyer.response.parser;

import java.io.UnsupportedEncodingException;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.navercorp.volleyextensions.volleyer.util.Assert;
/**
 * A parser class which converts data to string.
 *
 */
public class StringNetworkResponseParser implements NetworkResponseParser {

	public StringNetworkResponseParser() {
	}

	protected final String getBodyString(NetworkResponse response) throws UnsupportedEncodingException {
		return new String(response.data, HttpHeaderParser.parseCharset(response.headers));
	}

	@Override
	public <T> Response<T> parseNetworkResponse(NetworkResponse response, Class<T> clazz) {
		Assert.notNull(response, "The response");
		Assert.notNull(clazz, "The class token");
		try {
			String result = getBodyString(response);
			@SuppressWarnings("unchecked")
			Response<T> successResponse = (Response<T>) Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
			return successResponse;
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		}
	}
}
