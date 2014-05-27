package com.navercorp.volleyextensions.volleyer.http;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

import com.navercorp.volleyextensions.volleyer.http.HttpContent;
import com.navercorp.volleyextensions.volleyer.http.HttpMethod;

public class HttpContentTest {

	@Test(expected = NullPointerException.class)
	public void httpContentConstructorShouldThrowNpeWhenUrlIsNull() {
		// Given
		String nullUrl = null;
		HttpMethod method = HttpMethod.GET;
		// When & Then
		new HttpContent(nullUrl, method);
	}

	@Test(expected = NullPointerException.class)
	public void httpContentConstructorShouldThrowNpeWhenHttpMethodIsNull() {
		// Given
		String url = "test";
		HttpMethod nullMethod = null;
		// When & Then
		new HttpContent(url, nullMethod);
	}

	@Test(expected=NullPointerException.class)
	public void addHeaderMethodShouldThrowNpeWhenKeyIsNull() {
		// Given
		String nullKey = null;
		String value = "test";
		
		String url = "test";
		HttpMethod method = HttpMethod.GET;
		HttpContent httpContent = new HttpContent(url, method);
		
		// When & Then
		httpContent.addHeader(nullKey, value);
	}

	@Test(expected=NullPointerException.class)
	public void addHeaderMethodShouldThrowNpeWhenValueIsNull() {
		// Given
		String key = "test";
		String nullValue = null;
		
		String url = "test";
		HttpMethod method = HttpMethod.GET;
		HttpContent httpContent = new HttpContent(url, method);
		
		// When & Then
		httpContent.addHeader(key, nullValue);
	}
	
	@Test
	public void getHeaderMethodShouldReturnThingThatIsAlreadyPut() {
		// Given
		String key = "test";
		String value = "sometest";
		
		String url = "test";
		HttpMethod method = HttpMethod.GET;
		HttpContent httpContent = new HttpContent(url, method);
		
		// When
		httpContent.addHeader(key, value);
		// Then
		assertTrue(value.equals(httpContent.getHeader(key)));
	}

	@Test
	public void getBodyMethodShouldReturnThingThatIsAlreadSet() {
		// Given
		byte[] body = {'t','e','s','t'};
		
		String url = "test";
		HttpMethod method = HttpMethod.GET;
		HttpContent httpContent = new HttpContent(url, method);
		
		// When
		httpContent.setBody(body);
		// Then
		assertTrue(body.equals(httpContent.getBody()));
	}

	@Test
	public void getHttpMethodShouldReturnThingThatIsAlreadSet() {
		// Given
		String url = "test";
		HttpMethod method = HttpMethod.GET;
		HttpContent httpContent = new HttpContent(url, method);
		
		// When
		HttpMethod newMethod = httpContent.getMethod();
		// Then
		assertTrue(method.equals(newMethod));
	}

	@Test
	public void getUrlMethodShouldReturnThingThatIsAlreadSet() {
		// Given
		String url = "test";
		HttpMethod method = HttpMethod.GET;
		HttpContent httpContent = new HttpContent(url, method);
		
		// When
		String newUrl = httpContent.getUrl();
		// Then
		assertTrue(url.equals(newUrl));
	}	

	@Test(expected = UnsupportedOperationException.class)
	public void getHeadersMethodReturnUnModifiableMap() {
		// Given
		String url = "test";
		HttpMethod method = HttpMethod.GET;
		HttpContent httpContent = new HttpContent(url, method);

		// When
		httpContent.addHeader("name", "John Doe");
		Map<String, String> headers = httpContent.getHeaders();
		// Then
		headers.put("hello", "world");
	}
}
