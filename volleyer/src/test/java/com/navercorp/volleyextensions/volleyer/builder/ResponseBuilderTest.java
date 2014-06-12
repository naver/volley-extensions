package com.navercorp.volleyextensions.volleyer.builder;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.navercorp.volleyextensions.volleyer.DefaultVolleyerConfigurationFactory;
import com.navercorp.volleyextensions.volleyer.VolleyerConfiguration;
import com.navercorp.volleyextensions.volleyer.http.HttpContent;
import com.navercorp.volleyextensions.volleyer.http.HttpMethod;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class ResponseBuilderTest {
	RequestQueue requestQueue = mock(RequestQueue.class);

	@Test(expected=NullPointerException.class)
	public void responseBuilderConstructorShouldThrowNpeWhenVolleyerConfigurationIsNull() {
		// Given
		String url = "http://test";
		HttpMethod method = HttpMethod.GET;
		HttpContent httpContent = new HttpContent(url, method);
		VolleyerConfiguration nullConfiguration = null;
		Class<String> clazz = String.class;
		// When & Then
		new ResponseBuilder<String>(nullConfiguration, httpContent, clazz);
	}

	@Test(expected=NullPointerException.class)
	public void responseBuilderConstructorShouldThrowNpeWhenHttpContentIsNull() {
		// Given
		HttpContent nullHttpContent = null;
		VolleyerConfiguration configuration = DefaultVolleyerConfigurationFactory.create();
		Class<String> clazz = String.class;
		// When & Then
		new ResponseBuilder<String>(configuration, nullHttpContent, clazz);
	}
	
	@Test(expected=NullPointerException.class)
	public void responseBuilderConstructorShouldThrowNpeWhenTargetClasstIsNull() {
		// Given
		String url = "http://test";
		HttpMethod method = HttpMethod.GET;
		HttpContent httpContent = new HttpContent(url, method);
		VolleyerConfiguration configuration = DefaultVolleyerConfigurationFactory.create();
		Class<String> nullClazz = null;
		// When & Then
		new ResponseBuilder<String>(configuration, httpContent, nullClazz);
	}

	@Test
	public void setListenerMethodShouldReturnSameInstanceOfBuilder() {
		// Given
		String url = "http://test";
		HttpMethod method = HttpMethod.GET;
		HttpContent httpContent = new HttpContent(url, method);
		VolleyerConfiguration configuration = DefaultVolleyerConfigurationFactory.create();
		Class<String> clazz = String.class;
		ResponseBuilder<String> builder = new ResponseBuilder<String>(configuration, httpContent, clazz);
		Listener<String> listener = new Listener<String>(){
			@Override
			public void onResponse(String response) {
			}};
		// When
		ResponseBuilder<String> newBuilder = builder.setListener(listener);
		// Then
		assertTrue(builder == newBuilder);
	}

	@Test
	public void setErrorListenerMethodShouldReturnSameInstanceOfBuilder() {
		// Given
		String url = "http://test";
		HttpMethod method = HttpMethod.GET;
		HttpContent httpContent = new HttpContent(url, method);
		VolleyerConfiguration configuration = DefaultVolleyerConfigurationFactory.create();
		Class<String> clazz = String.class;
		ResponseBuilder<String> builder = new ResponseBuilder<String>(configuration, httpContent, clazz);
		ErrorListener errorListener = new ErrorListener (){
			@Override
			public void onErrorResponse(VolleyError error) {
			}};
		// When
		ResponseBuilder<String> newBuilder = builder.setErrorListener(errorListener);
		// Then
		assertTrue(builder == newBuilder);
	}

	@Test(expected = IllegalStateException.class)
	public void setListenerMethodShouldThrowIllegalStateExceptionWhenExecuteMethodIsAlreadyCalled() {
		// Given
		String url = "http://test";
		HttpMethod method = HttpMethod.GET;
		HttpContent httpContent = new HttpContent(url, method);
		VolleyerConfiguration configuration = DefaultVolleyerConfigurationFactory.create();
		Class<String> clazz = String.class;
		ResponseBuilder<String> builder = new ResponseBuilder<String>(configuration, httpContent, clazz);
		Listener<String> listener = new Listener<String>(){
			@Override
			public void onResponse(String response) {
			}};
		// When
		builder.execute();
		// Then
		builder.setListener(listener);
	}

	@Test(expected = IllegalStateException.class)
	public void setErrorListenerMethodShouldThrowIllegalStateExceptionWhenExecuteMethodIsAlreadyCalled() {
		// Given
		String url = "http://test";
		HttpMethod method = HttpMethod.GET;
		HttpContent httpContent = new HttpContent(url, method);
		VolleyerConfiguration configuration = DefaultVolleyerConfigurationFactory.create();
		Class<String> clazz = String.class;
		ResponseBuilder<String> builder = new ResponseBuilder<String>(configuration, httpContent, clazz);
		ErrorListener errorListener = new ErrorListener (){
			@Override
			public void onErrorResponse(VolleyError error) {
			}};
		// When
		builder.execute();
		// Then
		builder.setErrorListener(errorListener);
	}

}
