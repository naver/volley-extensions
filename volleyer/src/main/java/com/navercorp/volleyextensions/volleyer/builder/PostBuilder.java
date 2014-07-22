package com.navercorp.volleyextensions.volleyer.builder;

import java.io.File;

import com.android.volley.RequestQueue;
import com.navercorp.volleyextensions.volleyer.VolleyerConfiguration;
import com.navercorp.volleyextensions.volleyer.http.HttpMethod;
import com.navercorp.volleyextensions.volleyer.multipart.FilePart;
import com.navercorp.volleyextensions.volleyer.multipart.Part;
import com.navercorp.volleyextensions.volleyer.multipart.StringPart;
import com.navercorp.volleyextensions.volleyer.util.VolleyerLog;

public class PostBuilder extends RequestBuilder<PostBuilder> {

	public PostBuilder(RequestQueue requestQueue, VolleyerConfiguration configuration, String url) {
		super(requestQueue, configuration, url, HttpMethod.POST);
	}

	public PostBuilder setBody(byte[] body) {
		if (isBodyNull(body)) {
			return this;
		}

		httpContent.setBody(body);
		return this;
	}

	public PostBuilder setBody(String body) {
		if (isBodyNull(body)) {
			return this;
		}

		byte[] bytes = body.getBytes();
		return setBody(bytes);
	}

	private boolean isBodyNull(Object body) {
		boolean isNull = false;

		if (body == null) {
			VolleyerLog.warn("You have to set the body which is not null.");
			isNull = true;
		}

		return isNull;
	}

	public PostBuilder addFilePart(File file) {
		return addFilePart(file.getName(), file);
	}

	public PostBuilder addFilePart(String name, File file) {
		httpContent.addPart(new FilePart(name, file));
		return this;
	}

	public PostBuilder addStringPart(String name, String value) {
		httpContent.addPart(new StringPart(name, value));
		return this;
	}

	public PostBuilder addPart(Part part) {
		httpContent.addPart(part);
		return this;
	}
}
