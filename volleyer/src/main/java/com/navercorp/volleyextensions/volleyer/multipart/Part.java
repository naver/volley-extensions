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
import java.io.InputStream;

import com.navercorp.volleyextensions.volleyer.http.ContentType;
/**
 * An interface which represents a part as a member of {@code Multipart}.
 */
public interface Part extends Writable {
	/**
	 * Get a part name.
	 */
	String getName();
	/**
	 * Get content type of a content.
	 */
	ContentType getContentType();
	/**
	 * <pre>
	 * Get a content as {@code InputStream}
	 *
	 * WARN : Don't close {@code InputStream} in this method,
	 * {@InputStream} automatically closes after reading.
	 * </pre>
	 */
	InputStream getContent() throws IOException;
}
