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

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.navercorp.volleyextensions.volleyer.http.ContentType;
import com.navercorp.volleyextensions.volleyer.http.ContentTypes;
import com.navercorp.volleyextensions.volleyer.util.Assert;
/**
 * <pre>
 * A parser class which converts json data to T object.
 *
 * NOTE : If this class is added into {@link IntegratedNetworkResponseParser},
 * and the content type of a response is "application/json" or "text/json" type,
 * integrated parser automatically delegates to this class.
 *
 * WARN : You have to import jackson 2.x library to use this class.
 * If not, this class throws an error when initializing.
 * </pre>
 */
public class Jackson2NetworkResponseParser implements TypedNetworkResponseParser {
	/** Default {@link ObjectMapper} is singleton. */
	private static class ObjectMapperHolder {
		private final static ObjectMapper objectMapper;
		static {
			objectMapper = new ObjectMapper();
			// ignore unknown json properties
			objectMapper.configure(
					DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			// allow unquoted control characters
			objectMapper.configure(
					JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
			// ObjectMapper is thread-safe after configuration. ( )
		}
		
		private static ObjectMapper getObjectMapper() {
			return objectMapper;
		}
	}
	
	/** {@code objectMapper} is immutable(but not severely). */
	private final ObjectMapper objectMapper;
	
	public Jackson2NetworkResponseParser() {
		this(ObjectMapperHolder.getObjectMapper());
	}
	public Jackson2NetworkResponseParser(ObjectMapper objectMapper) {
		Assert.notNull(objectMapper, "ObjectMapper");
		this.objectMapper = objectMapper;
	}
	
	protected final String getBodyString(NetworkResponse response) throws UnsupportedEncodingException {
		return new String(response.data, HttpHeaderParser.parseCharset(response.headers));
	}
	
	@Override
	public <T> Response<T> parseNetworkResponse(NetworkResponse response, Class<T> clazz) {
		Assert.notNull(response, "Response");
		Assert.notNull(clazz, "Class token");
		
		try {
			T result = objectMapper.readValue(getBodyString(response), clazz);
			return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
		} catch (JsonParseException e) {
			return Response.error(new ParseError(e));
		} catch (JsonMappingException e) {
			return Response.error(new ParseError(e));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (IOException e) {
			return Response.error(new VolleyError(e));
		} catch (Exception e) {
			return Response.error(new VolleyError(e));
		} 
	}
	@Override
	public ContentTypes getContentTypes() {
		return new ContentTypes(ContentType.CONTENT_TYPE_APPLICATION_JSON, ContentType.CONTENT_TYPE_TEXT_JSON);
	}

}
