package com.navercorp.volleyextensions.volleyer.multipart;
/**
 * An interface for classes which need multipart status.
 */
public interface MultipartContainer {
	/**
	 * Check whether the container has a part.
	 */
	boolean hasMultipart();
	/**
	 * Get a multipart instance.
	 */
	Multipart getMultipart();
}
