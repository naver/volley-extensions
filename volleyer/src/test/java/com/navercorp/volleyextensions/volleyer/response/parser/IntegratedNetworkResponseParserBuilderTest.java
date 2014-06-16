package com.navercorp.volleyextensions.volleyer.response.parser;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

import org.junit.Test;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.navercorp.volleyextensions.volleyer.http.ContentType;
import com.navercorp.volleyextensions.volleyer.http.ContentTypes;

public class IntegratedNetworkResponseParserBuilderTest {

	@Test
	public void addParserShouldUseGetContentTypesMethodWhenTypedNetworkResponseParserIsTheArgument() {
		// Given
		MockTypedNetworkResponseParser typedResponseParser = new MockTypedNetworkResponseParser();
		IntegratedNetworkResponseParser.Builder builder = new IntegratedNetworkResponseParser.Builder();
		// When
		builder.addParser(typedResponseParser);
		// Then
		assertThat(typedResponseParser.isCalledGetContentTypes(), is(true));
	}

	@Test(expected = NullPointerException.class)
	public void addParserShouldThrowNpeWhenTypedNetworkResponseParserIsNull() {
		// Given
		TypedNetworkResponseParser typedResponseParser = null;
		IntegratedNetworkResponseParser.Builder builder = new IntegratedNetworkResponseParser.Builder();
		// When & Then
		builder.addParser(typedResponseParser);
	}

	@Test(expected = NullPointerException.class)
	public void addParserShouldThrowNpeWhenNetworkResponseParserIsNull() {
		// Given
		NetworkResponseParser responseParser = null;
		IntegratedNetworkResponseParser.Builder builder = new IntegratedNetworkResponseParser.Builder();
		// When & Then
		builder.addParser(ContentType.CONTENT_TYPE_APPLICATION_JSON, responseParser);
	}

	@Test(expected = NullPointerException.class)
	public void addParserShouldThrowNpeWhenNetworkContentTypeIsNull() {
		// Given
		NetworkResponseParser responseParser = mock(NetworkResponseParser.class);
		IntegratedNetworkResponseParser.Builder builder = new IntegratedNetworkResponseParser.Builder();
		ContentType nullContentType = null;
		// When & Then
		builder.addParser(nullContentType, responseParser);
	}

	@Test(expected = NullPointerException.class)
	public void buildShouldCreateIntegratedNetworkResponseParserInstance() {
		// Given
		TypedNetworkResponseParser typedResponseParser = null;
		// When
		IntegratedNetworkResponseParser integratedResponseParser = new IntegratedNetworkResponseParser.Builder()
																		.addParser(typedResponseParser)
																		.build();
		// Then
		assertNotNull(integratedResponseParser);
	}

	/**
	 * Mock class of TypedNetworkResponseParser. This is used for tests.
	 */
	static class MockTypedNetworkResponseParser implements TypedNetworkResponseParser {

		private boolean isCalled = false;

		@Override
		public <T> Response<T> parseNetworkResponse(NetworkResponse response, Class<T> clazz) {
			return null;
		}

		@Override
		public ContentTypes getContentTypes() {
			isCalled = true;
			return new ContentTypes(ContentType.CONTENT_TYPE_APPLICATION_JSON, ContentType.CONTENT_TYPE_TEXT_PLAIN);
		}

		public boolean isCalledGetContentTypes() {
			return isCalled;
		}

	}
}
