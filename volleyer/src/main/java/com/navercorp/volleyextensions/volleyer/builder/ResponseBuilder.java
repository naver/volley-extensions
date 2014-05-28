package com.navercorp.volleyextensions.volleyer.builder;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.navercorp.volleyextensions.volleyer.VolleyerContext;
import com.navercorp.volleyextensions.volleyer.http.HttpContent;
import com.navercorp.volleyextensions.volleyer.request.creator.DefaultRequestCreator;
import com.navercorp.volleyextensions.volleyer.request.creator.RequestCreator;
import com.navercorp.volleyextensions.volleyer.request.executor.RequestExecutor;
import com.navercorp.volleyextensions.volleyer.response.parser.NetworkResponseParser;
import com.navercorp.volleyextensions.volleyer.response.parser.StringNetworkResponseParser;
import com.navercorp.volleyextensions.volleyer.util.Assert;

class ResponseBuilder<T> {
	
	private VolleyerContext volleyerContext;
	private HttpContent httpContent;
	private Class<T> clazz;
	private Listener<T> listener;
	private ErrorListener errorListener;
	private NetworkResponseParser responseParser;

	private boolean isDoneToBuild = false;

	ResponseBuilder(VolleyerContext volleyerContext, HttpContent httpContent, Class<T> clazz) {
		Assert.notNull(volleyerContext, "VolleyerContext");
		Assert.notNull(httpContent, "HttpContent");
		Assert.notNull(clazz, "Target class token");

		this.volleyerContext = volleyerContext;
		this.httpContent = httpContent;
		this.clazz = clazz;
	}
	
	public ResponseBuilder<T> setListener(Listener<T> listener) {
		if (isDoneToBuild == true) {
			throw new IllegalStateException("ResponseBuilder should not be used any more. Because afterRequest() has been called.");
		}
		this.listener = listener;
		return this;
	}

	public ResponseBuilder<T> setErrorListener(ErrorListener errorListener) {
		if (isDoneToBuild == true) {
			throw new IllegalStateException("ResponseBuilder should not be used any more. Because execute() has been called.");
		}
		this.errorListener = errorListener;
		return this;
	}

	public ResponseBuilder<T> setResponseParser(NetworkResponseParser responseParser) {
		Assert.notNull(responseParser, "Response Parser");
		if (isDoneToBuild == true) {
			throw new IllegalStateException("ResponseBuilder should not be used any more. Because execute() has been called.");
		}
		this.responseParser = responseParser;
		return this;
	}

	public Request<T> execute() {
		setDefaultListenerIfNull();
		setDefaultErrorListenerIfNull();
		setDefaultResponseParserIfNull();

		Request<T> request = buildRequest();
		executeRequest(request);
		return request;
	}

	private void setDefaultListenerIfNull() {
		if (listener != null) {
			return;
		}
		listener = createEmptyListener();
	}

	private Listener<T> createEmptyListener() {
		return new Listener<T>() {

			@Override
			public void onResponse(Object response) {
			}
		};
	}

	private void setDefaultErrorListenerIfNull() {
		if (errorListener != null) {
			return;
		}
		errorListener = createEmptyErrorListener();
	}

	private ErrorListener createEmptyErrorListener() {
		return new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
			}
		};
	}

	private void setDefaultResponseParserIfNull() {
		if (responseParser != null) {
			return;
		}
		// TODO : This code is temporarily written. This should be changed.
		responseParser = new StringNetworkResponseParser();
	}

	private Request<T> buildRequest() {
		RequestCreator requestCreator = volleyerContext.getRequestCreator();
		return requestCreator.createRequest(httpContent, clazz, responseParser, listener, errorListener);
	}

	private void executeRequest(Request<T> request) {
		RequestExecutor executor = volleyerContext.getRequestExecutor();
		executor.executeRequest(request);
	}
}
