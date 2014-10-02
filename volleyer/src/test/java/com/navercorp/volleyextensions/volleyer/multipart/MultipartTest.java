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
package com.navercorp.volleyextensions.volleyer.multipart;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Matchers;

import com.navercorp.volleyextensions.volleyer.multipart.FilePart;
import com.navercorp.volleyextensions.volleyer.multipart.Multipart;
import com.navercorp.volleyextensions.volleyer.multipart.Part;
import com.navercorp.volleyextensions.volleyer.multipart.StringPart;

public class MultipartTest {

	static final Part secondPart = new StringPart("name", "value");
	static final Part thirdPart = new StringPart("someOtherName", "otherValue");

	static Part firstPart = null;
	static String firstPartName = "lorem";
	static String fileContent = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim"
			+ "\nveniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate"
			+ "\nvelit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim"
			+ "\nid est laborum.";

	Multipart multipart;
	List<Part> parts;

	@BeforeClass
	public static void setUpOnce() {
		File file = PartTestUtils.createTestFile(fileContent);
		firstPart = new FilePart(firstPartName, file);
	}

	@Before
	public void setUp() {
		multipart = new Multipart();
		parts = createParts();
	}

	@Test
	public void shouldReturnDefaultBoundary() {
		// When
		String boundary = multipart.getBoundary();
		// Then
		assertNotNull(boundary);
		assertTrue(boundary.length() > 0);
	}

	@Test
	public void shouldReturnBoundary() {
		// Given
		String boundary = "myboundary";
		multipart = new Multipart(boundary);
		// When
		String result = multipart.getBoundary();
		// Then
		assertThat(result, is(boundary));
	}

	@Test
	public void shouldReturnContentTypeContainingBoundary() {
		// Given
		String boundary = "myboundary";
		multipart = new Multipart(boundary);
		String expected = "multipart/form-data; boundary=--" + boundary;
		// When
		String result = multipart.getContentType();
		// Then
		assertThat(result, is(expected));
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionWhenBoundaryIsNull() {
		new Multipart(null /* boundary */);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionWhenBoundaryIsEmpty() {
		new Multipart("" /* boundary */);
	}

	@Test
	public void shouldContainPartWhenAdded() {
		// Given
		Part part = firstPart;
		// When
		multipart.add(part);
		// Then
		assertThat(multipart.contains(part), is(Boolean.TRUE));
	}

	@Test
	public void shouldIncreaseSizeWhenAdded() {
		// Given
		Part part = firstPart;
		int size = multipart.size();
		// When
		boolean isChanged = multipart.add(part);
		// Then
		assertThat(isChanged, is(Boolean.TRUE));
		assertThat(multipart.size(), is(size + 1));
	}

	@Test
	public void shouldNotAddNullPart() {
		// Given
		int size = multipart.size();
		// When
		boolean isChanged = multipart.add(null);
		// Then
		assertThat(isChanged, is(Boolean.FALSE));
		assertThat(multipart.size(), is(size));
	}

	@Test
	public void shouldNotContainPartWhenRemoved() {
		// Given
		multipart.add(firstPart);
		// When
		multipart.remove(firstPart);
		// Then
		assertThat(multipart.contains(firstPart), is(Boolean.FALSE));
	}

	@Test
	public void shouldBeEmptySizeWhenCleared() {
		// Given
		multipart.add(firstPart);
		multipart.add(secondPart);
		multipart.add(thirdPart);
		// When
		multipart.clear();
		// Then
		assertThat(multipart.isEmpty(), is(Boolean.TRUE));
	}

	@Test
	public void shouldContainPartsWhenAdded() {
		// Given
		int size = multipart.size();
		// When
		boolean isChanged = multipart.addAll(parts);
		// Then
		assertThat(isChanged, is(Boolean.TRUE));
		assertThat(multipart.containsAll(parts), is(Boolean.TRUE));
		assertThat(multipart.size(), is(size + parts.size()));
	}

	@Test
	public void shouldNotIncraseSizeWhenAddedNullCollection() {
		// Given
		int size = multipart.size();
		// When
		multipart.addAll(null);
		// Then
		assertThat(multipart.size(), is(size));
	}

	@Test
	public void shouldNotAddNullPartsInCollection() {
		// Given
		int size = multipart.size();
		int sizeOfActualParts = parts.size();
		parts.add(null);
		parts.add(null);
		// When
		boolean isChanged = multipart.addAll(parts);
		// Then
		assertThat(isChanged, is(Boolean.TRUE));
		assertThat(multipart.size(), is(size + sizeOfActualParts));
	}

	@Test
	public void shouldNotContainPartsWhenRemovedAll() {
		// Given
		int size = multipart.size();
		multipart.addAll(parts);
		// When
		multipart.removeAll(parts);
		// Then
		assertThat(multipart.containsAll(parts), is(Boolean.FALSE));
		assertThat(multipart.size(), is(size));
	}

	@Test
	public void iteratorShouldContainPartsCorrectly() {
		// Given
		multipart.addAll(parts);
		// When
		Iterator<Part> iter = multipart.iterator();
		// Then
		assertThat(iter.next() , is(firstPart));
		assertThat(iter.next() , is(secondPart));
		assertThat(iter.next() , is(thirdPart));
	}

	@Test
	public void arrayShouldContainPartsCorrectly() {
		// Given
		multipart.addAll(parts);
		// When
		Object[] partArray = (Object[]) multipart.toArray();
		// Then
		assertThat(partArray , is(parts.toArray()));
	}

	@Test
	public void shouldWriteToOutputStream() throws IOException {
		// Given
		OutputStream out = mock(OutputStream.class);
		multipart.addAll(parts);
		// When
		multipart.write(out);
		// Then
		verify(out, atLeastOnce()).write(Matchers.any(byte[].class));
	}

	@Test
	public void shouldNotWriteToOutputStreamIfEmpty() throws IOException {
		// Given
		OutputStream out = mock(OutputStream.class);
		// When
		multipart.write(out);
		// Then
		verify(out, never()).write(Matchers.any(byte[].class));
	}

	@Test
	public void shouldWriteParts() throws IOException {
		// Given
		OutputStream out = mock(OutputStream.class);
		Part mockPart = mock(Part.class);
		Part otherMockPart = mock(Part.class);
		multipart.add(mockPart);
		multipart.add(otherMockPart);
		// When
		multipart.write(out);
		// Then
		verify(mockPart).write(out);
		verify(otherMockPart).write(out);
	}

	@Test
	public void shouldWriteAsExpected() throws IOException {
		// Given
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		multipart.addAll(parts);
		// When
		multipart.write(out);
		// Then
		// It does not assert, but just print an output.
		System.out.println(new String(out.toByteArray()));
	}

	private static List<Part> createParts() {
		List<Part> parts = new ArrayList<Part>(){{
			add(firstPart);
			add(secondPart);
			add(thirdPart);
		}};
		return parts;
	}
}
