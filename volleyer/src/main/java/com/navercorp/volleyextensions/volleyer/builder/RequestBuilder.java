package com.navercorp.volleyextensions.volleyer.builder;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.navercorp.volleyextensions.volleyer.VolleyerContext;
import com.navercorp.volleyextensions.volleyer.http.HttpContent;
import com.navercorp.volleyextensions.volleyer.http.HttpMethod;
import com.navercorp.volleyextensions.volleyer.util.Assert;

public class RequestBuilder {
	private VolleyerContext volleyerContext;
	private HttpContent httpContent;

	private boolean isDoneToBuild = false;

	public RequestBuilder(VolleyerContext volleyerContext, String url, HttpMethod method) {
		Assert.notNull(volleyerContext, "VolleyerContext");

		this.volleyerContext = volleyerContext;
		httpContent = new HttpContent(url, method);
	}

	public RequestBuilder addHeader(String key, String value) {
		assertFinishState();

		httpContent.addHeader(key, value);
		return this;
	}

	private void assertFinishState() {
		if (isDoneToBuild == true) {
			throw new IllegalStateException("RequestBuilder should not be used any more. Because afterRequest() has been called.");
		}
	}

	public <T> ResponseBuilder<T> setTargetClass(Class<T> clazz) {
		Assert.notNull(clazz, "Target Class token");

		assertFinishState();

		ResponseBuilder<T> builder = new ResponseBuilder<T>(volleyerContext, httpContent, clazz);
		isDoneToBuild = true;
		return builder;
	}

	public ResponseBuilder<String> setListener(Listener<String> listener) {
		assertFinishState();
		ResponseBuilder<String> builder = setTargetClass(String.class);
		builder.setListener(listener);
		return builder;
	}

	public ResponseBuilder<String> setErrorListener(ErrorListener errorListener) {
		assertFinishState();
		ResponseBuilder<String> builder = setTargetClass(String.class);
		builder.setErrorListener(errorListener);
		return builder;
	}

	public Request<String> execute() {
		assertFinishState();
		ResponseBuilder<String> builder = setTargetClass(String.class);
		return builder.execute();
	}

}
