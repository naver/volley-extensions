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
package com.navercorp.volleyextensions.volleyer.util;

import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class StringUtilsTest {

	@Test
	public void utilShouldReturnNullWhenLengthIsInvalid() {
		// Given
		int length = -1;
		// When
		String str = StringUtils.generateRandom(length);
		// Then
		assertNull(str);
	}

	@Test
	public void utilShouldReturnEmptyStringWhenLengthIsZero() {
		// Given
		int length = 0;
		// When
		String str = StringUtils.generateRandom(length);
		// Then
		assertThat(str, is(""));
	}

	@Test
	public void utilShouldReturnStringWhenLengthIsValid() {
		// Given
		int length = 30;
		// When
		String str = StringUtils.generateRandom(length);
		// Then
		assertNotNull(str);
	}

	@Test
	public void utilShouldReturnCorrectLengthString() {
		// Given
		int one = 1;
		int length = 3;
		int otherLength = 10;
		// When
		String oneStr = StringUtils.generateRandom(one);
		String str = StringUtils.generateRandom(length);
		String otherStr = StringUtils.generateRandom(otherLength);
		// Then
		assertThat(oneStr.length(), is(one));
		assertThat(str.length(), is(length));
		assertThat(otherStr.length(), is(otherLength));
	}

	@Test
	public void utilShouldReturnTrueWhenStringIsNull() {
		// When
		boolean result = StringUtils.isEmpty(null);
		// Then
		assertThat(result, is(Boolean.valueOf(true)));
	}

	@Test
	public void utilShouldReturnTrueWhenStringIsEmpty() {
		// When
		boolean result = StringUtils.isEmpty("");
		// Then
		assertThat(result, is(Boolean.valueOf(true)));
	}

	@Test
	public void utilShouldReturnFalseWhenStringIsNotEmpty() {
		// Given
		String notEmpty = "String notEmpty";
		// When
		boolean result = StringUtils.isEmpty(notEmpty);
		// Then
		assertThat(result, is(Boolean.valueOf(false)));
	}
}
