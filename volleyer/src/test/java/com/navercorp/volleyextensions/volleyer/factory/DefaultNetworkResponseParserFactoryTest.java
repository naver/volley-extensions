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
package com.navercorp.volleyextensions.volleyer.factory;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;

import com.navercorp.volleyextensions.volleyer.response.parser.IntegratedNetworkResponseParser.Builder;
import com.navercorp.volleyextensions.volleyer.response.parser.*;
import com.navercorp.volleyextensions.volleyer.util.ClassUtils;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ClassUtils.class)
public class DefaultNetworkResponseParserFactoryTest {

	static final String JACKSON_2_X_CLASS_NAME = "com.fasterxml.jackson.databind.ObjectMapper";
	static final String JACKSON_1_X_CLASS_NAME = "org.codehaus.jackson.map.ObjectMapper";
	static final String SIMPLE_XML_CLASS_NAME = "org.simpleframework.xml.core.Persister";

	private Builder builder;

	@Before
	public void setUp() {
		builder = mock(Builder.class);
		PowerMockito.mockStatic(ClassUtils.class);
	}

	@Test
	public void shouldAddJackson2NetworkResponserIfPresent() {
		// Given
		given(ClassUtils.isPresent(JACKSON_2_X_CLASS_NAME)).willReturn(true);
		Matcher<TypedNetworkResponseParser> matcher = argMatcher(new PlainMatcher(){

			@Override
			public boolean matches(Object item) {
				if (item instanceof Jackson2NetworkResponseParser) {
					return true;
				}
				return false;
			}});
		// When
		DefaultNetworkResponseParserFactory.addJacksonParserIfPresent(builder);
		// Then
		verify(builder).addParser(Matchers.argThat(matcher));
	}

	@Test
	public void shouldAddJacksonNetworkResponserIfPresent() {
		// Given
		given(ClassUtils.isPresent(JACKSON_2_X_CLASS_NAME)).willReturn(false);
		given(ClassUtils.isPresent(JACKSON_1_X_CLASS_NAME)).willReturn(true);
		Matcher<TypedNetworkResponseParser> matcher = argMatcher(new PlainMatcher(){

			@Override
			public boolean matches(Object item) {
				if (item instanceof JacksonNetworkResponseParser) {
					return true;
				}
				return false;
			}});
		// When
		DefaultNetworkResponseParserFactory.addJacksonParserIfPresent(builder);
		// Then

		verify(builder).addParser(Matchers.argThat(matcher));
	}

	@Test
	public void shouldNotAddJacksonsIfNotPresent() {
		// Given
		given(ClassUtils.isPresent(JACKSON_2_X_CLASS_NAME)).willReturn(false);
		given(ClassUtils.isPresent(JACKSON_1_X_CLASS_NAME)).willReturn(false);
		Matcher<TypedNetworkResponseParser> matcher = argMatcher(new PlainMatcher(){

			@Override
			public boolean matches(Object item) {
				if (item instanceof JacksonNetworkResponseParser) {
					return true;
				}

				if (item instanceof Jackson2NetworkResponseParser) {
					return true;
				}
				return false;
			}});
		// When
		DefaultNetworkResponseParserFactory.addJacksonParserIfPresent(builder);
		// Then
		verify(builder, never()).addParser(Matchers.argThat(matcher));
	}

	@Test
	public void shouldAddSimpleXmlNetworkResponseParserIfPresent() {
		// Given
		given(ClassUtils.isPresent(SIMPLE_XML_CLASS_NAME)).willReturn(true);
		Matcher<TypedNetworkResponseParser> matcher = argMatcher(new PlainMatcher(){

			@Override
			public boolean matches(Object item) {
				if (item instanceof SimpleXmlNetworkResponseParser) {
					return true;
				}
				return false;
			}});
		// When
		DefaultNetworkResponseParserFactory.addSimpleXmlParserIfPresent(builder);
		// Then
		verify(builder).addParser(Matchers.argThat(matcher));
	}

	@Test
	public void shouldNotAddSimpleXmlNetworkResponseParserIfNotPresent() {
		// Given
		given(ClassUtils.isPresent(SIMPLE_XML_CLASS_NAME)).willReturn(false);
		Matcher<TypedNetworkResponseParser> matcher = argMatcher(new PlainMatcher(){

			@Override
			public boolean matches(Object item) {
				if (item instanceof SimpleXmlNetworkResponseParser) {
					return true;
				}
				return false;
			}});
		// When
		DefaultNetworkResponseParserFactory.addSimpleXmlParserIfPresent(builder);
		// Then
		verify(builder, never()).addParser(Matchers.argThat(matcher));
	}

	private static Matcher<TypedNetworkResponseParser> argMatcher(final PlainMatcher plainMatcher) {
		return new Matcher<TypedNetworkResponseParser>(){

			@Override
			public void describeTo(Description description) {
			}

			@Override
			public boolean matches(Object item) {
				return plainMatcher.matches(item);
			}

			@Override
			public void describeMismatch(Object item, Description mismatchDescription) {
			}

			@Override
			public void _dont_implement_Matcher___instead_extend_BaseMatcher_() {
			}};
	}

	interface PlainMatcher {
		boolean matches(Object item);
	}
}
