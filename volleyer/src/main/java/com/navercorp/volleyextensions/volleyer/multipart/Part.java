package com.navercorp.volleyextensions.volleyer.multipart;

import java.io.IOException;
import java.io.InputStream;

import com.navercorp.volleyextensions.volleyer.http.ContentType;

public interface Part extends Writable {
	String getName();
	ContentType getContentType();
	InputStream getContent() throws IOException;
}
