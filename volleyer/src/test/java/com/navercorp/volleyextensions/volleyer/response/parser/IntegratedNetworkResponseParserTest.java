package com.navercorp.volleyextensions.volleyer.response.parser;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.navercorp.volleyextensions.volleyer.http.ContentType;

public class IntegratedNetworkResponseParserTest {

	private static final String jsonContent = "{\"imageUrl\":\"http://static.naver.com/volley-ext.jpg\"," +
			   "\"title\":\"Volley extention has released\"," +
			   "\"content\":\"Very good News\"}";

	private static final int DEFAULT_STATUS_CODE = 200;

	private NetworkResponse networkResponse = null;
	private Map<ContentType, NetworkResponseParser> parsers = null;
	private NetworkResponseParser integratedResponseParser = null;

	@Before
	public void setUp() {
		parsers = createDefaultParsersMap();
	}

	@Test
	public void resultShouldBeStringWhenTargetClassIsString() {
		// Given
		networkResponse = createJsonNetworkResponse();
		parsers = createDefaultParsersMap();
		Class<String> clazz = String.class;
		integratedResponseParser = createIntegratedNetworkResponseParser(parsers);
		// When
		Response<String> response = integratedResponseParser.parseNetworkResponse(networkResponse, clazz);
		// Then
		assertThat(jsonContent, is(response.result));
	}

	@Test
	public void resultShouldBeNullWhenTargetClassIsVoid() {
		// Given
		networkResponse = new NetworkResponse(jsonContent.getBytes());
		parsers = createDefaultParsersMap();
		integratedResponseParser = createIntegratedNetworkResponseParser(parsers);
		Class<Void> clazz = Void.class;
		// When
		Response<Void> response = integratedResponseParser.parseNetworkResponse(networkResponse, clazz);
		// Then
		assertNull(response.result);
		assertNull(response.cacheEntry);
	}

	@Test
	public void parserShouldSelectJsonMappedResponseParserWhenContentTypeIsApplicationJson() {
		// Given
		networkResponse = createJsonNetworkResponse();
		NetworkResponseParser responseParser = mock(NetworkResponseParser.class);
		parsers.put(ContentType.CONTENT_TYPE_APPLICATION_JSON, responseParser);
		integratedResponseParser = createIntegratedNetworkResponseParser(parsers);
		Class<News> clazz = News.class;
		// When
		Response<News> response = integratedResponseParser.parseNetworkResponse(networkResponse, clazz);
		// Then
		verify(responseParser).parseNetworkResponse(networkResponse, clazz);
	}

	@Test
	public void parserShouldReturnVolleyErrorWhenContentTypeIsNotMappedIntoResponseParser() {
		// Given
		networkResponse = createUnknownNetworkResponse();
		NetworkResponseParser responseParser = mock(NetworkResponseParser.class);
		parsers.put(ContentType.CONTENT_TYPE_APPLICATION_JSON, responseParser);
		integratedResponseParser = createIntegratedNetworkResponseParser(parsers);
		Class<News> clazz = News.class;
		// When
		Response<News> response = integratedResponseParser.parseNetworkResponse(networkResponse, clazz);
		// Then
		assertNotNull(response.error);
	}

	@Test
	public void parserShouldReturnVolleyErrorWhenContentTypeIsNotSpecified() {
		// Given
		networkResponse = new NetworkResponse(jsonContent.getBytes());
		NetworkResponseParser responseParser = mock(NetworkResponseParser.class);
		parsers.put(ContentType.CONTENT_TYPE_APPLICATION_JSON, responseParser);
		integratedResponseParser = createIntegratedNetworkResponseParser(parsers);
		Class<News> clazz = News.class;
		// When
		Response<News> response = integratedResponseParser.parseNetworkResponse(networkResponse, clazz);
		// Then
		assertNotNull(response.error);
	}

	private static NetworkResponse createJsonNetworkResponse() {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json; charset=utf-8");
		boolean notModified = true;
		return new NetworkResponse(DEFAULT_STATUS_CODE, jsonContent.getBytes(), headers, notModified);
	}

	private static NetworkResponse createUnknownNetworkResponse() {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/unknown; charset=utf-8");
		boolean notModified = true;
		return new NetworkResponse(DEFAULT_STATUS_CODE, jsonContent.getBytes(), headers, notModified);
	}

	private static HashMap<ContentType, NetworkResponseParser> createDefaultParsersMap() {
		return new HashMap<ContentType, NetworkResponseParser>();
	}

	private static IntegratedNetworkResponseParser createIntegratedNetworkResponseParser(Map<ContentType, NetworkResponseParser> parsers) {
		return new IntegratedNetworkResponseParser(parsers);
	}

	/** just for test */
	private static class News {
		public String imageUrl;
		public String title;
	}
}
