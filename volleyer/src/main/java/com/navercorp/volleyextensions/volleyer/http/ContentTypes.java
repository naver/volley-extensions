package com.navercorp.volleyextensions.volleyer.http;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ContentTypes {

	private List<ContentType> contentTypes;

	public ContentTypes(ContentType... arrayOfContentTypes) {
		contentTypes = new ArrayList<ContentType>();
		
		setContentTypes(arrayOfContentTypes);
		assertTypeArguments();
	}

	private void assertTypeArguments() {
		if(contentTypes.size() == 0) {
			throw new IllegalArgumentException("None of content types were added.");
		}
	}

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

	private void addContentType(ContentType contentType) {
		if (contentType == null) {
			return;
		}

		contentTypes.add(contentType);
		return;
	}

	public List<ContentType> getListOfContentTypes() {
		return Collections.unmodifiableList(contentTypes);
	} 
	
}
