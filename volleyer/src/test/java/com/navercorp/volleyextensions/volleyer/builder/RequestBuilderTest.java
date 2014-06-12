package com.navercorp.volleyextensions.volleyer.builder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.navercorp.volleyextensions.volleyer.DefaultVolleyerConfigurationFactory;
import com.navercorp.volleyextensions.volleyer.VolleyerConfiguration;
import com.navercorp.volleyextensions.volleyer.http.HttpMethod;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class RequestBuilderTest {
	RequestQueue requestQueue;

	@Before
	public void setUp() {
		requestQueue = mock(RequestQueue.class);
	}

	@Test(expected=NullPointerException.class)
	public void requestBuilderConstructorShouldThrowNpeWhenVolleyerConfigurationIsNull() {
		// Given
		String url = "test";
		HttpMethod method = HttpMethod.GET;
		VolleyerConfiguration nullVolleyerConfiguration = null;
		
		// When & Then
		new TestPurposeRequestBuilder(nullVolleyerConfiguration, url, method);
	}

	@Test(expected=NullPointerException.class)
	public void requestBuilderConstructorShouldThrowNpeWhenUrlIsNull() {
		// Given
		String nullUrl = null;
		HttpMethod method = HttpMethod.GET;
		VolleyerConfiguration configuration = DefaultVolleyerConfigurationFactory.create();
		
		// When & Then
		new TestPurposeRequestBuilder(configuration, nullUrl, method);
	}

	@Test(expected=NullPointerException.class)
	public void requestBuilderConstructorShouldThrowNpeWhenHttpMethodIsNull() {
		// Given
		String url = "test";
		HttpMethod nullMethod = null;
		VolleyerConfiguration configuration = DefaultVolleyerConfigurationFactory.create();
		
		// When & Then
		new TestPurposeRequestBuilder(configuration, url, nullMethod);
	}

	@Test(expected=NullPointerException.class)
	public void addHeaderMethodShouldThrowNpeWhenKeyIsNull() {
		// Given
		String nullKey = null;
		String value = "test";
		
		String url = "test";
		HttpMethod method = HttpMethod.GET;
		VolleyerConfiguration configuration = DefaultVolleyerConfigurationFactory.create();
		TestPurposeRequestBuilder builder = new TestPurposeRequestBuilder(configuration, url, method);
		
		// When & Then
		builder.addHeader(nullKey, value);
	}

	@Test(expected=NullPointerException.class)
	public void addHeaderMethodShouldThrowNpeWhenValueIsNull() {
		// Given
		String key = "test";
		String nullValue = null;
		
		String url = "test";
		HttpMethod method = HttpMethod.GET;
		VolleyerConfiguration configuration = DefaultVolleyerConfigurationFactory.create();
		TestPurposeRequestBuilder builder = new TestPurposeRequestBuilder(configuration, url, method);
		
		// When & Then
		builder.addHeader(key, nullValue);
	}

	@Test
	public void addHeaderMethodShouldReturnSameInstanceOfBuilder() {
		// Given
		String key = "testKey";
		String value = "testValue";
		
		String url = "test";
		HttpMethod method = HttpMethod.GET;
		VolleyerConfiguration configuration = DefaultVolleyerConfigurationFactory.create();
		TestPurposeRequestBuilder builder = new TestPurposeRequestBuilder(configuration, url, method);
		
		// When
		TestPurposeRequestBuilder newBuilder = builder.addHeader(key, value);
		// Then
		assertTrue(builder == newBuilder);
	}

	@Test
	public void afterRequestMethodShouldReturnAnActualInstance() {
		// Given
		String url = "test";
		HttpMethod method = HttpMethod.GET;
		VolleyerConfiguration configuration = DefaultVolleyerConfigurationFactory.create();
		TestPurposeRequestBuilder builder = new TestPurposeRequestBuilder(configuration, url, method);
		
		// When
		ResponseBuilder<String> responseBuilder = builder.setTargetClass(String.class);
		// Then
		assertNotNull(responseBuilder);
	}

	@Test(expected=IllegalStateException.class)
	public void addHeaderMethodShouldThrowIllegalStateExceptionWhenAfterRequestMethodIsAlreadyCalled() {
		// Given
		String key = "testKey";
		String value = "testValue";
		
		String url = "test";
		HttpMethod method = HttpMethod.GET;
		VolleyerConfiguration configuration = DefaultVolleyerConfigurationFactory.create();
		TestPurposeRequestBuilder builder = new TestPurposeRequestBuilder(configuration, url, method);
		
		// When & Then
		builder.setTargetClass(String.class);
		builder.addHeader(key, value);
	}
	@Test(expected = NullPointerException.class)
	public void setTargetClassMethodShouldThrowNpeWhenTargetClassIsNull() {
		// Given
		String key = "testKey";
		String value = "testValue";
		
		String url = "test";
		HttpMethod method = HttpMethod.GET;
		VolleyerConfiguration configuration = DefaultVolleyerConfigurationFactory.create();
		TestPurposeRequestBuilder builder = new TestPurposeRequestBuilder(configuration, url, method);
		Class<?> clazz = null;
		// When & Then
		builder.setTargetClass(clazz);
	}

	@Test
	public void setTargetClassMethodShouldReturnAnActualInstanceWhenTargetClassIsNotNull() {
		// Given
		String key = "testKey";
		String value = "testValue";
		
		String url = "test";
		HttpMethod method = HttpMethod.GET;
		VolleyerConfiguration configuration = DefaultVolleyerConfigurationFactory.create();
		TestPurposeRequestBuilder builder = new TestPurposeRequestBuilder(configuration, url, method);
		Class<String> clazz = String.class;
		// When
		ResponseBuilder<String> responseBuilder = builder.setTargetClass(clazz);
		// Then
		assertNotNull(responseBuilder);
	}

	@Test(expected = IllegalStateException.class)
	public void setTargetClassMethodShouldThrowIllegalStateExceptionWhenSetTargetClassMethodIsCalledAgain() {
		// Given
		String url = "test";
		HttpMethod method = HttpMethod.GET;
		VolleyerConfiguration configuration = DefaultVolleyerConfigurationFactory.create();
		TestPurposeRequestBuilder builder = new TestPurposeRequestBuilder(configuration, url, method);
		Class<String> clazz = String.class;
		// When
		builder.setTargetClass(clazz);
		// Then
		builder.setTargetClass(clazz);
	}	

	@Test()
	public void requestQueueShouldBeExecutedWhenExecuteMethodIsCalled() {
		// Given
		String url = "http://test";
		HttpMethod method = HttpMethod.GET;
		VolleyerConfiguration configuration = DefaultVolleyerConfigurationFactory.create();
		TestPurposeRequestBuilder builder = new TestPurposeRequestBuilder(configuration, url, method);
		// When
		Request<String> request = builder.execute();
		// Then
		// TODO : RequestQueue is not used at all. some volleyer api that take a requestQueue will be added soon.
		// verify(requestQueue).add(request);
	}

	@Test(expected = IllegalStateException.class)
	public void requestBuilderShouldThrowIllegalStateExceptionWhenExecuteMethodIsCalledAgain() {
		// Given
		String url = "http://test";
		HttpMethod method = HttpMethod.GET;
		VolleyerConfiguration configuration = DefaultVolleyerConfigurationFactory.create();
		TestPurposeRequestBuilder builder = new TestPurposeRequestBuilder(configuration, url, method);
		// When
		builder.execute();
		// Then
		builder.execute();
	}

	@Test(expected = IllegalStateException.class)
	public void requestBuilderShouldThrowIllegalStateExceptionWhensetListenerMethodIsCalledAgain() {
		// Given
		String url = "http://test";
		HttpMethod method = HttpMethod.GET;
		VolleyerConfiguration configuration = DefaultVolleyerConfigurationFactory.create();
		TestPurposeRequestBuilder builder = new TestPurposeRequestBuilder(configuration, url, method);
		Listener<String> listener = new Listener<String>(){

			@Override
			public void onResponse(String response) {
			}};
		// When
		builder.setListener(listener);
		// Then
		builder.setListener(listener);
	}

	@Test(expected = IllegalStateException.class)
	public void requestBuilderShouldThrowIllegalStateExceptionWhensetErrorListenerMethodIsCalledAgain() {
		// Given
		String url = "http://test";
		HttpMethod method = HttpMethod.GET;
		VolleyerConfiguration configuration = DefaultVolleyerConfigurationFactory.create();
		TestPurposeRequestBuilder builder = new TestPurposeRequestBuilder(configuration, url, method);
		ErrorListener errorListener = new ErrorListener(){

			@Override
			public void onErrorResponse(VolleyError error) {
			}};
		// When
		builder.setErrorListener(errorListener);
		// Then
		builder.setErrorListener(errorListener);
	}
}
