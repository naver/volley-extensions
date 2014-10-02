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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.navercorp.volleyextensions.volleyer.http.ContentType;
/**
 * <pre>
 * A part class of a content of actual file.
 * (This part usually is used for uploading a file.)
 * </pre>
 */
public class FilePart extends AbstractPart {

	private final File file;
	private ContentType contentType;
	private String filename;
	/**
	 * <pre>
	 * Constructor with default part name and default content type.
	 * NOTE : part name is determined by a file name.
	 * </pre>
	 * @param file File instance whose path actually exists.
	 */
	public FilePart(File file) {
		this(file.getName(), file);
	}
	/**
	 * <pre>
	 * Constructor with custom part name and default content type.
	 * </pre>
	 * @param name part name
	 * @param file File instance whose path actually exists.
	 */
	public FilePart(String name, File file) {
		this(name, file, null);
	}
	/**
	 * <pre>
	 * Constructor with custom part name and custom content type.
	 * </pre>
	 * @param name part name
	 * @param file File instance whose path actually exists.
	 * @param contentType content type of a content 
	 */
	public FilePart(String name, File file, ContentType contentType) {
		super(name);
		this.file = file;
		contentType = createDefaultContentTypeIfNull(contentType);
		this.contentType = contentType;
		this.filename = file.getName();
	}

	private ContentType createDefaultContentTypeIfNull(ContentType contentType) {
		if (contentType == null) {
			contentType = ContentType.createContentType("application/octet-stream");
		}

		return contentType;
	}

	@Override
	public ContentType getContentType() {
		return contentType;
	}

	@Override
	protected byte[] getExtraHeader() {
		return ("filename=\"" + filename + "\"").getBytes();
	}

	@Override
	public InputStream getContent() throws IOException {
		if (!file.exists()) {
			throw new IOException("The file of parts doesn't exist. [PATH : ] "
					+ file.getAbsolutePath());
		}

		InputStream is = new BufferedInputStream(new FileInputStream(file));
		return is;
	}

	public String getFilename() {
		return filename;
	}
}
