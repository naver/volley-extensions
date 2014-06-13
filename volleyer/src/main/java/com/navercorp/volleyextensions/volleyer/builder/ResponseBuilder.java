package com.navercorp.volleyextensions.volleyer.builder;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.navercorp.volleyextensions.volleyer.VolleyerConfiguration;
import com.navercorp.volleyextensions.volleyer.http.HttpContent;
import com.navercorp.volleyextensions.volleyer.request.creator.RequestCreator;
import com.navercorp.volleyextensions.volleyer.request.executor.RequestExecutor;
import com.navercorp.volleyextensions.volleyer.response.parser.NetworkResponseParser;
import com.navercorp.volleyextensions.volleyer.response.parser.StringNetworkResponseParser;
import com.navercorp.volleyextensions.volleyer.util.Assert;

public class ResponseBuilder<T> {
	
	private RequestQueue requestQueue;
	private VolleyerConfiguration configuration;
	private HttpContent httpContent;
	private Class<T> clazz;
	private Listener<T> listener;
	private ErrorListener errorListener;
	private NetworkResponseParser responseParser;

	private boolean isDoneToBuild = false;

	ResponseBuilder(RequestQueue requestQueue, VolleyerConfiguration configuration, HttpContent httpContent, Class<T> clazz) {
		Assert.notNull(requestQueue, "RequestQueue");
		Assert.notNull(configuration, "VolleyerConfiguration");
		Assert.notNull(httpContent, "HttpContent");
		Assert.notNull(clazz, "Target class token");

		this.requestQueue = requestQueue;
		this.configuration = configuration;
		this.httpContent = httpContent;
		this.clazz = clazz;
	}
	
	public ResponseBuilder<T> setListener(Listener<T> listener) {
		assertFinishState();
		this.listener = listener;
		return this;
	}

	public ResponseBuilder<T> setErrorListener(ErrorListener errorListener) {
		assertFinishState();
		this.errorListener = errorListener;
		return this;
	}

	private void assertFinishState() {
		if (isDoneToBuild == true) {
			throw new IllegalStateException("ResponseBuilder should not be used any more. Because execute() has been called.");
		}
	}

	public ResponseBuilder<T> setResponseParser(NetworkResponseParser responseParser) {
		Assert.notNull(responseParser, "Response Parser");
		assertFinishState();
		this.responseParser = responseParser;
		return this;
	}

	public Request<T> execute() {
		setDefaultListenerIfNull();
		setDefaultErrorListenerIfNull();
		setDefaultResponseParserIfNull();

		Request<T> request = buildRequest();
		executeRequest(request);
		markFinishState();
		return request;
	}

	protected final void markFinishState() {
		isDoneToBuild = true;
		// Let requestQueue be null for avoiding memory leak when this builder is referenced by some variable.
		requestQueue = null;
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

		responseParser = configuration.getDefaultNetworkResponseParser();
	}

	private Request<T> buildRequest() {
		RequestCreator requestCreator = configuration.getRequestCreator();
		return requestCreator.createRequest(httpContent, clazz, responseParser, listener, errorListener);
	}

	private void executeRequest(Request<T> request) {
		RequestExecutor executor = configuration.getRequestExecutor();
		executor.executeRequest(requestQueue, request);
	}
}
