package com.navercorp.volleyextensions.volleyer.multipart;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.navercorp.volleyextensions.volleyer.http.ContentType;

public class FilePart extends AbstractPart {

	private final File file;
	private ContentType contentType;
	private String filename;

	public FilePart(File file) {
		this(file.getName(), file);
	}

	public FilePart(String name, File file) {
		this(name, file, null);
	}

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
