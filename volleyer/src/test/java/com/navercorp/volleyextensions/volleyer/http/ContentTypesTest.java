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
