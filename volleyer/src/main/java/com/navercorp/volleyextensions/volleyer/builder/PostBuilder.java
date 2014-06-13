package com.navercorp.volleyextensions.volleyer.builder;

import com.android.volley.RequestQueue;
import com.navercorp.volleyextensions.volleyer.VolleyerConfiguration;
import com.navercorp.volleyextensions.volleyer.http.HttpMethod;
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
}
