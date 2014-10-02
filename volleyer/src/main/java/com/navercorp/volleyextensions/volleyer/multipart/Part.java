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
