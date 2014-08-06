package com.navercorp.volleyextensions.volleyer.builder;

import java.io.File;

import com.android.volley.RequestQueue;
import com.navercorp.volleyextensions.volleyer.VolleyerConfiguration;
import com.navercorp.volleyextensions.volleyer.http.HttpMethod;
import com.navercorp.volleyextensions.volleyer.multipart.FilePart;
import com.navercorp.volleyextensions.volleyer.multipart.Part;
import com.navercorp.volleyextensions.volleyer.multipart.StringPart;
import com.navercorp.volleyextensions.volleyer.util.VolleyerLog;

abstract class BodyBuilder<B extends BodyBuilder<B>> extends RequestBuilder<B> {

	public BodyBuilder(RequestQueue requestQueue, VolleyerConfiguration configuration, String url, HttpMethod method) {
		super(requestQueue, configuration, url, method);
	}

	@SuppressWarnings("unchecked")
	public B withBody(byte[] body) {
		if (isBodyNull(body)) {
			return (B) this;
		}

		httpContent.setBody(body);
		return (B) this;
	}

	@SuppressWarnings("unchecked")
	public B withBody(String body) {
		if (isBodyNull(body)) {
			return (B) this;
		}

		byte[] bytes = body.getBytes();
		return withBody(bytes);
	}

	private boolean isBodyNull(Object body) {
		boolean isNull = false;

		if (body == null) {
			VolleyerLog.warn("You have to set the body which is not null.");
			isNull = true;
		}

		return isNull;
	}

	public B addFilePart(File file) {
		return addFilePart(file.getName(), file);
	}

	@SuppressWarnings("unchecked")
	public B addFilePart(String name, File file) {
		httpContent.addPart(new FilePart(name, file));
		return (B) this;
	}

	@SuppressWarnings("unchecked")
	public B addStringPart(String name, String value) {
		httpContent.addPart(new StringPart(name, value));
		return (B) this;
	}

	@SuppressWarnings("unchecked")
	public B addPart(Part part) {
		httpContent.addPart(part);
		return (B) this;
	}
}
