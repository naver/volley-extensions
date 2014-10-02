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

import org.junit.Test;

public class ContentTypeTest {

	@Test(expected = NullPointerException.class)
	public void createContentTypeShouldThrowNpeWhenContentTypeStringIsNull() {
		// Given
		String nullContentTypeString = null;
		// When & Then
		ContentType.createContentType(nullContentTypeString);
	}

	@Test(expected = IllegalArgumentException.class)
	public void createContentTypeShouldThrowIllegalArgumentExceptionWhenContentTypeStringIsEmpty() {
		// Given
		String emptyContentTypeString = "";
		// When & Then
		ContentType.createContentType(emptyContentTypeString);
	}

	@Test
	public void toStringShouldReturnContentTypeString() {
		// Given
		String contentTypeString = "application/json";
		// When
		ContentType contentType = ContentType.createContentType(contentTypeString);
		// Then
		assertThat(contentTypeString , is(contentType.toString()));
	}

	@Test
	public void createApplicationJsonContentTypeShouldReturnSameInstanceWhenDefaultBuiltInInstanceAlreadyExists() {
		// Given
		String applicationJsonContentTypeString = "application/json";
		// When
		ContentType applicationJsonContentType = ContentType.createContentType(applicationJsonContentTypeString);
		ContentType otherApplicationJsonContentType = ContentType.createContentType(applicationJsonContentTypeString);
		// Then
		assertTrue(applicationJsonContentType == otherApplicationJsonContentType);
	}

	@Test
	public void createApplicationXmlContentTypeShouldReturnSameInstanceWhenDefaultBuiltInInstanceAlreadyExists() {
		// Given
		String applicationXmlContentTypeString = "application/xml";
		// When
		ContentType applicationXmlContentType = ContentType.createContentType(applicationXmlContentTypeString);
		ContentType otherApplicationXmlContentType = ContentType.createContentType(applicationXmlContentTypeString);
		// Then
		assertTrue(applicationXmlContentType == otherApplicationXmlContentType);
	}

	@Test
	public void createTextJsonContentTypeShouldReturnSameInstanceWhenDefaultBuiltInInstanceAlreadyExists() {
		// Given
		String textJsonContentTypeString = "text/json";
		// When
		ContentType textJsonContentType = ContentType.createContentType(textJsonContentTypeString);
		ContentType otherTextJsonContentType = ContentType.createContentType(textJsonContentTypeString);
		// Then
		assertTrue(textJsonContentType == otherTextJsonContentType);
	}

	@Test
	public void createTextXmlContentTypeShouldReturnSameInstanceWhenDefaultBuiltInInstanceAlreadyExists() {
		// Given
		String textXmlContentTypeString = "text/xml";
		// When
		ContentType textXmlContentType = ContentType.createContentType(textXmlContentTypeString);
		ContentType otherTextXmlContentType = ContentType.createContentType(textXmlContentTypeString);
		// Then
		assertTrue(textXmlContentType == otherTextXmlContentType);
	}

	@Test
	public void createTextPlainContentTypeShouldReturnSameInstanceWhenDefaultBuiltInInstanceAlreadyExists() {
		// Given
		String textPlainContentTypeString = "text/plain";
		// When
		ContentType textPlainContentType = ContentType.createContentType(textPlainContentTypeString);
		ContentType otherTextPlainContentType = ContentType.createContentType(textPlainContentTypeString);
		// Then
		assertTrue(textPlainContentType == otherTextPlainContentType);
	}

	@Test
	public void hashcodeShouldBeSameWithContentTypeString() {
		// Given
		String contentTypeString = "application/json";
		// When
		ContentType contentType = ContentType.createContentType(contentTypeString);
		// Then
		assertThat(contentType.hashCode(), is(contentTypeString.hashCode()));
	}

	@Test
	public void contentTypeShouldEqualWithItself() {
		// Given
		String contentTypeString = "text/just+for+test";
		// When
		ContentType contentType = ContentType.createContentType(contentTypeString);
		// Then
		assertTrue(contentType.equals(contentType));
	}

	@Test
	public void contentTypeShouldEqualWithOtherContentTypeWhichShareContentTypeString() {
		// Given
		String contentTypeString = "text/just+for+test";
		String sameContentTypeString = new String(contentTypeString);
		// When
		ContentType contentType = ContentType.createContentType(contentTypeString);
		ContentType otherContentType = ContentType.createContentType(sameContentTypeString);
		// Then
		assertTrue(contentType.equals(otherContentType));
		assertTrue(otherContentType.equals(contentType));
	}

	@Test
	public void equalsShouldSatisfyTransitivityRule() {
		// Given
		String contentTypeString = "text/just+for+test";
		String sameContentTypeString = new String(contentTypeString);
		String thirdContentTypeString = new String(sameContentTypeString);
		// When
		ContentType contentType = ContentType.createContentType(contentTypeString);
		ContentType otherContentType = ContentType.createContentType(sameContentTypeString);
		ContentType thirdContentType = ContentType.createContentType(thirdContentTypeString);
		// Then
		assertTrue(contentType.equals(otherContentType));
		assertTrue(otherContentType.equals(thirdContentType));
		assertTrue(contentType.equals(thirdContentType));
	}

	@Test
	public void contentTypeShouldNotEqualWhenOtherContentTypeWhenContentTypeIsDifferent() {
		// Given
		String contentTypeString = "text/just+for+test";
		String otherContentTypeString = "text/other+content+type";
		// When
		ContentType contentType = ContentType.createContentType(contentTypeString);
		ContentType otherContentType = ContentType.createContentType(otherContentTypeString);
		// Then
		assertTrue(contentType.equals(otherContentType) == false);
	}

	@Test
	public void contentTypeShouldNotEqualWhenOtherInstanceIsNotContentType() {
		// Given
		String contentTypeString = "text/just+for+test";
		// When
		ContentType contentType = ContentType.createContentType(contentTypeString);
		Object fakeContentType = "hello test";
		// Then
		assertTrue(contentType.equals(fakeContentType) == false);
	}

	@Test
	public void contentTypeShouldNotEqualWithNull() {
		// Given
		String contentTypeString = "text/just+for+test";
		// When
		ContentType contentType = ContentType.createContentType(contentTypeString);
		ContentType nullContentType = null;
		// Then
		assertTrue(contentType.equals(nullContentType) == false);
	}

	@Test
	public void contentTypeStringShouldBeDeletedCharset() {
		// Given
		String contentTypeString = "text/plain; charset=utf-8";
		String resultContentTypeString = "text/plain";
		// When
		ContentType contentType = ContentType.createContentType(contentTypeString);
		ContentType otherContentType = ContentType.createContentType(contentTypeString);
		// Then
		assertThat(resultContentTypeString, is(contentType.toString()));
		assertTrue(contentType == otherContentType);
	}

	@Test
	public void contentTypeStringShouldBeDeletedSpaces() {
		// Given
		String contentTypeString = "   t  e     xt / pl   ai   n   ";
		String resultContentTypeString = "text/plain";
		// When
		ContentType contentType = ContentType.createContentType(contentTypeString);
		ContentType otherContentType = ContentType.createContentType(contentTypeString);
		// Then
		assertThat(resultContentTypeString, is(contentType.toString()));
		assertTrue(contentType == otherContentType);
	}

	@Test
	public void contentTypeStringShouldBeLowerCase() {
		// Given
		String contentTypeString = "tEXT/pLAin";
		String resultContentTypeString = "text/plain";
		// When
		ContentType contentType = ContentType.createContentType(contentTypeString);
		ContentType otherContentType = ContentType.createContentType(contentTypeString);
		// Then
		assertThat(resultContentTypeString, is(contentType.toString()));
		assertTrue(contentType == otherContentType);
	}	
}
