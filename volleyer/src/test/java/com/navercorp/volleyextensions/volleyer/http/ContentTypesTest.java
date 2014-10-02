package com.navercorp.volleyextensions.volleyer.http;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class ContentTypesTest {

	@Test(expected = IllegalArgumentException.class)
	public void constructorShouldThrowIllegalArgumentExceptionWhenThereIsNoArgument() {
		// When
		ContentTypes contentTypes = new ContentTypes();
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructorShouldThrowIllegalArgumentExceptionWhenArgumentsAreAllNull() {
		// When
		ContentTypes contentTypes = new ContentTypes(null, null);
	}

	@Test
	public void listShouldIncludeContentTypeArguments(){
		// Given
		ContentType xmlContentType = ContentType.createContentType("application/xml");
		ContentType jsonContentType = ContentType.createContentType("application/json");
		ContentType rssXmlContentType = ContentType.createContentType("application/rss+xml");
		// When
		ContentTypes contentTypes = new ContentTypes(xmlContentType, jsonContentType, rssXmlContentType);
		List<ContentType> list = contentTypes.getListOfContentTypes();
		// Then
		assertThat(list.size(), is(3));
		assertTrue(list.contains(xmlContentType));
		assertTrue(list.contains(jsonContentType));
		assertTrue(list.contains(rssXmlContentType));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void listShouldBeunmodifiableList(){
		// Given
		ContentType xmlContentType = ContentType.createContentType("application/xml");
		ContentType jsonContentType = ContentType.createContentType("application/json");
		ContentType rssXmlContentType = ContentType.createContentType("application/rss+xml");

		ContentTypes contentTypes = new ContentTypes(xmlContentType, jsonContentType, rssXmlContentType);
		List<ContentType> list = contentTypes.getListOfContentTypes();
		// When & Then
		list.add(xmlContentType);
	}	
}
