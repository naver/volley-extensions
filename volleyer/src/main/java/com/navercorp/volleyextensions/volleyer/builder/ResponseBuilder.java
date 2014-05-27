package com.navercorp.volleyextensions.volleyer.builder;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.navercorp.volleyextensions.volleyer.ResponseParser;
import com.navercorp.volleyextensions.volleyer.VolleyerContext;
import com.navercorp.volleyextensions.volleyer.http.HttpContent;
import com.navercorp.volleyextensions.volleyer.util.Assert;

class ResponseBuilder<T> {
	
	private VolleyerContext volleyerContext;
	private HttpContent httpContent;
	private Class<T> clazz;
	private Listener<T> listener;
	private ErrorListener errorListener;
	private ResponseParser responseParser;

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

	public ResponseBuilder<T> setResponseParser(ResponseParser responseParser) {
		Assert.notNull(responseParser, "Response Parser");
		if (isDoneToBuild == true) {
			throw new IllegalStateException("ResponseBuilder should not be used any more. Because execute() has been called.");
		}
		this.responseParser = responseParser;
		return this;
	}

	public Request<T> execute() {
		if (isDoneToBuild == true) {
			throw new IllegalStateException("ResponseBuilder should not be used any more. Because execute() has been called.");
		}
		isDoneToBuild = true;
		// TODO : This dummy method should be removed and an actual request should be returned. 
		return createDummyRequest();
	}

	private Request<T> createDummyRequest() {
		return new Request<T>(0, null, errorListener){

			@Override
			protected Response<T> parseNetworkResponse(NetworkResponse response) {
				return null;
			}

			@Override
			protected void deliverResponse(T response) {
				// TODO Auto-generated method stub
				
			}};
	}
}
