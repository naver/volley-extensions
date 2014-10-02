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
import java.io.OutputStream;

import com.navercorp.volleyextensions.volleyer.http.ContentType;
import com.navercorp.volleyextensions.volleyer.util.Assert;
import com.navercorp.volleyextensions.volleyer.util.IoUtils;
/**
 * <pre>
 * A class which generates all of the form of a part.
 * Sub classes that overrides this class can consider only their own content.
 * </pre>
 */
public abstract class AbstractPart implements Part {

	private static final byte[] CONTENT_DISPOSITION = "Content-Disposition: form-data;".getBytes();

	private static final int DEFAULT_BUFFER_SIZE = 1024;

	private String name;

	public AbstractPart(String name) {
		Assert.notNull(name, "Part name");
		this.name = name;
	}
	/**
	 * <pre>
	 * Write content to {@code OutputStream} with generating a form of a part. 
	 * To help for implementation, refered to rfc1341 : {@link //www.w3.org/Protocols/rfc1341/7_2_Multipart.html}.
	 * </pre>
	 */
	@Override
	public void write(OutputStream out) throws IOException {
		writeContentDisposition(out);
		writeName(out);
		writeExtraHeaderIfNotNull(out);
		writeCRLF(out);
		writeContentTypeIfNotNull(out);
		writeCRLF(out);
		writeContent(out);
		writeCRLF(out);
	}

	private void writeName(OutputStream out) throws IOException {
		out.write((" name=\"" + getName() + "\"").getBytes());
	}

	private void writeContentDisposition(OutputStream out) throws IOException {
		out.write(CONTENT_DISPOSITION);
	}

	private void writeContentTypeIfNotNull(OutputStream out) throws IOException {
		ContentType contentType = getContentType();
		if (contentType == null) {
			return;
		}

		out.write(("Content-Type: " + contentType).getBytes());
		writeCRLF(out);
	}

	protected final void writeExtraHeaderIfNotNull(OutputStream out) throws IOException {
		byte[] extraHeader = getExtraHeader();
		if (extraHeader == null) {
			return;
		}

		out.write(';');
		out.write(extraHeader);
	}
	/**
	 * Add headers of a part other than some basic header such as part name, filename.
	 * @return byte array of headers
	 */
	protected abstract byte[] getExtraHeader();
	/**
	 * Write content to {@code OutputStream} and close the stream of content.
	 * @throws IOException
	 */
	protected final void writeContent(OutputStream out) throws IOException {
		InputStream is = null;
		try {
			is = getContent();
			if (is == null) {
				return;
			}

			byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
			int readLength = 0;

			while (( readLength = is.read(buffer) ) != -1) {
				int writeLength = (readLength < DEFAULT_BUFFER_SIZE) ? readLength : DEFAULT_BUFFER_SIZE;
				out.write(buffer, 0, writeLength);
			}
		} finally {
			IoUtils.closeQuietly(is);
		}

	}

	@SuppressWarnings("unused")
	private void writeLine(OutputStream out, String str) throws IOException {
		out.write((str + "\n").getBytes());
	}

	private void writeCRLF(OutputStream out) throws IOException {
		out.write("\r\n".getBytes());
	}

	@Override
	public String getName() {
		return name;
	}

}
