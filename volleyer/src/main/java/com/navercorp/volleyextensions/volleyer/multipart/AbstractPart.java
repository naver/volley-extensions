package com.navercorp.volleyextensions.volleyer.multipart;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.navercorp.volleyextensions.volleyer.http.ContentType;
import com.navercorp.volleyextensions.volleyer.util.Assert;
import com.navercorp.volleyextensions.volleyer.util.IoUtils;

public abstract class AbstractPart implements Part {

	private static final byte[] CONTENT_DISPOSITION = "Content-Disposition: form-data;".getBytes();

	private static final int DEFAULT_BUFFER_SIZE = 1024;

	private String name;

	public AbstractPart(String name) {
		Assert.notNull(name, "Part name");
		this.name = name;
	}

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

	protected abstract byte[] getExtraHeader();

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
