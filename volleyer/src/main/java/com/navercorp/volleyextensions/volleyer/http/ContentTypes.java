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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * A collection of {@link ContentType}s.
 * @author Wonjun Kim
 *
 */
public class ContentTypes {

	private List<ContentType> contentTypes;
	/**
	 * Make a collection by including {@code arrayContentTypes}
	 * @param arrayOfContentTypes should contain 1 or more {@link ContentType}s.
	 */
	public ContentTypes(ContentType... arrayOfContentTypes) {
		contentTypes = new ArrayList<ContentType>();
		
		setContentTypes(arrayOfContentTypes);
		assertTypeArguments();
	}
	/**
	 * Throw an error if types are empty.
	 */
	private void assertTypeArguments() {
		if(contentTypes.size() == 0) {
			throw new IllegalArgumentException("None of content types were added.");
		}
	}
	/**
	 * Add a bunch of {@code ContentType}s.
	 * @param arrayOfContentTypes
	 */
	private void setContentTypes(ContentType... arrayOfContentTypes) {
		if (arrayOfContentTypes == null) {
			return;
		}

		int length = arrayOfContentTypes.length;
		for(int i = 0; i<length; ++i) {
			ContentType contentType = arrayOfContentTypes[i];
			addContentType(contentType);
		}
	}
	/**
	 * Add a {@code ContentType}.
	 * @param contentType
	 */
	private void addContentType(ContentType contentType) {
		if (contentType == null) {
			return;
		}

		contentTypes.add(contentType);
		return;
	}
	/**
	 * @return unmodifiable list of {@code ContentType}s.
	 */
	public List<ContentType> getListOfContentTypes() {
		return Collections.unmodifiableList(contentTypes);
	} 
	
}
