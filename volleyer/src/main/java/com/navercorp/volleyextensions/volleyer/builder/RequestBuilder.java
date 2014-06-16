package com.navercorp.volleyextensions.volleyer.builder;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.navercorp.volleyextensions.volleyer.VolleyerConfiguration;
import com.navercorp.volleyextensions.volleyer.http.HttpContent;
import com.navercorp.volleyextensions.volleyer.http.HttpMethod;
import com.navercorp.volleyextensions.volleyer.util.Assert;

abstract class RequestBuilder<B extends RequestBuilder<B>> {
	private RequestQueue requestQueue;
	private final VolleyerConfiguration configuration;
	protected final HttpContent httpContent;

	protected boolean isDoneToBuild = false;

	public RequestBuilder(RequestQueue requestQueue, VolleyerConfiguration configuration, String url, HttpMethod method) {
		Assert.notNull(requestQueue, "RequestQueue");
		Assert.notNull(configuration, "VolleyerConfiguration");

		this.requestQueue = requestQueue;
		this.configuration = configuration;
		httpContent = new HttpContent(url, method);
	}

	@SuppressWarnings("unchecked")
	public B addHeader(String key, String value) {
		assertFinishState();

		httpContent.addHeader(key, value);
		return (B) this;
	}

	protected final void assertFinishState() {
		if (isDoneToBuild == true) {
			throw new IllegalStateException("RequestBuilder should not be used any more. Because afterRequest() has been called.");
		}
	}

	public <T> ResponseBuilder<T> setTargetClass(Class<T> clazz) {
		Assert.notNull(clazz, "Target Class token");

		assertFinishState();

		ResponseBuilder<T> builder = new ResponseBuilder<T>(requestQueue, configuration, httpContent, clazz);
		markFinishState();
		return builder;
	}

	protected final void markFinishState() {
		isDoneToBuild = true;
		// Let requestQueue be null for avoiding memory leak when this builder is referenced by some variable.
		requestQueue = null;
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

	public Request<Void> execute() {
		assertFinishState();
		ResponseBuilder<Void> builder = setTargetClass(Void.class);
		return builder.execute();
	}

}
