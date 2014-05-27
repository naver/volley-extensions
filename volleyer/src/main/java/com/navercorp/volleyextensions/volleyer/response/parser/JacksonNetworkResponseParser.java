package com.navercorp.volleyextensions.volleyer.response.parser;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.navercorp.volleyextensions.volleyer.util.Assert;

public class JacksonNetworkResponseParser implements NetworkResponseParser {
	/** Default {@link ObjectMapper} is singleton. */
	private static class ObjectMapperHolder {
		private static final ObjectMapper objectMapper;
		static {
			objectMapper = new ObjectMapper();
			// ignore unknown json properties
			objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			// allow unquoted control characters
			objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
		}

		private static ObjectMapper defaultObjectMapper() {
			return objectMapper;
		}
	}
	/** {@code objectMapper} is immutable(but not severely). */
	private final ObjectMapper objectMapper;

	public JacksonNetworkResponseParser() {
		this(ObjectMapperHolder.defaultObjectMapper());
	}

	public JacksonNetworkResponseParser(ObjectMapper objectMapper) {
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
}
