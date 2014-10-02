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

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.navercorp.volleyextensions.volleyer.util.StringUtils;
/**
 * A collection of {@link Part}s.
 * @author Wonjun Kim
 *
 */
public class Multipart implements Collection<Part>, Writable {

	private static final int DEFAULT_BOUNDARY_LENGTH = 30;
	private List<Part> parts = new ArrayList<Part>();
	private String boundary;

	public Multipart() {
		this(StringUtils.generateRandom(DEFAULT_BOUNDARY_LENGTH));
	}

	public Multipart(String boundary) {
		assertBoundary(boundary);
		this.boundary = boundary;
	}

	private static void assertBoundary(String boundary) {
		if (StringUtils.isEmpty(boundary)) {
			throw new IllegalArgumentException("Boundary must not be empty.");
		}
	}

	/**
	 * Make the integrated multipart content by combining {@code Part}s and write it to {@code OutputStream}.
	 */
	@Override
	public void write(OutputStream out) throws IOException {
		if (parts.size() == 0) {
			return;
		}

		writeParts(out);
	}

	public String getContentType() {
		return "multipart/form-data; boundary=" + "--" + boundary;
	}

	private void writeParts(OutputStream out) throws IOException {
		for(Part part : parts) {
			writeBoundaryStart(out);
			part.write(out);
		}

		writeBoundaryEnd(out);
	}

	private void writeBoundaryStart(OutputStream out) throws IOException {
		out.write(("----" + boundary + "\n").getBytes());
	}

	private void writeBoundaryEnd(OutputStream out) throws IOException {
		out.write(("----" + boundary + "--\n").getBytes());
	}

	@Override
	public boolean add(Part part) {
		if (part == null) {
			return false;
		}

		boolean isChanged = parts.add(part);
		return isChanged;
	}

	@Override
	public boolean addAll(Collection<? extends Part> collection) {
		if (collection == null) {
			return false;
		}

		boolean isChanged = false;
		for(Part part : collection) {
			isChanged = isChanged | add(part);
		}

		return isChanged;
	}

	@Override
	public void clear() {
		parts.clear();
	}

	@Override
	public boolean contains(Object obj) {
		return parts.contains(obj);
	}

	@Override
	public boolean containsAll(Collection<?> collection) {
		return parts.containsAll(collection);
	}

	@Override
	public boolean isEmpty() {
		return parts.isEmpty();
	}

	@Override
	public Iterator<Part> iterator() {
		return parts.iterator();
	}

	@Override
	public boolean remove(Object obj) {
		return parts.remove(obj);
	}

	@Override
	public boolean removeAll(Collection<?> collection) {
		return parts.removeAll(collection);
	}

	@Override
	public boolean retainAll(Collection<?> collection) {
		return parts.retainAll(collection);
	}

	@Override
	public Object[] toArray() {
		return parts.toArray();
	}

	@SuppressWarnings("hiding")
	@Override
	public <Part> Part[] toArray(Part[] array) {
		return (Part[]) parts.toArray(array);
	}

	@Override
	public int size() {
		return parts.size();
	}

	/**
	 * Return a multipart boundary string.
	 */
	public String getBoundary() {
		return boundary;
	}
}
