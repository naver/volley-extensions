package com.navercorp.volleyextensions.volleyer.builder;

import static org.junit.Assert.*;

import org.junit.Test;

import com.navercorp.volleyextensions.volleyer.VolleyerContext;
import com.navercorp.volleyextensions.volleyer.http.HttpContent;
import com.navercorp.volleyextensions.volleyer.http.HttpMethod;

public class TargetClassBuilderTest {

	@Test(expected = NullPointerException.class)
	public void targetClassBuilderConstructorShouldThrowNpeWhenVolleyerContextIsNull() {
		// Given
		String url = "test";
		HttpMethod method = HttpMethod.GET;
		VolleyerContext nullVolleyerContext = null;
		HttpContent httpContent = new HttpContent(url, method);
		// When & Then
		new TargetClassBuilder(nullVolleyerContext, httpContent);
	}

	@Test(expected = NullPointerException.class)
	public void targetClassBuilderConstructorShouldThrowNpeWhenHttpContentIsNull() {
		// Given
		VolleyerContext volleyerContext = null;
		HttpContent nullHttpContent = null;
		// When & Then
		new TargetClassBuilder(volleyerContext, nullHttpContent);
	}
	
	@Test(expected = NullPointerException.class)
	public void setTargetClassMethodShouldThrowNpeWhenTargetClassIsNull() {
		// Given
		String url = "test";
		HttpMethod method = HttpMethod.GET;
		VolleyerContext volleyerContext = new VolleyerContext();
		HttpContent httpContent = new HttpContent(url, method);
		TargetClassBuilder builder = new TargetClassBuilder(volleyerContext, httpContent);
		Class<?> clazz = null;
		// When & Then
		builder.setTargetClass(clazz);
	}

	@Test
	public void setTargetClassMethodShouldReturnAnActualInstanceWhenTargetClassIsNotNull() {
		// Given
		String url = "test";
		HttpMethod method = HttpMethod.GET;
		VolleyerContext volleyerContext = new VolleyerContext();
		HttpContent httpContent = new HttpContent(url, method);
		TargetClassBuilder builder = new TargetClassBuilder(volleyerContext, httpContent);
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
		VolleyerContext volleyerContext = new VolleyerContext();
		HttpContent httpContent = new HttpContent(url, method);
		TargetClassBuilder builder = new TargetClassBuilder(volleyerContext, httpContent);
		Class<String> clazz = String.class;
		// When
		builder.setTargetClass(clazz);
		// Then
		builder.setTargetClass(clazz);
	}
}
