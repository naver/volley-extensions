package com.navercorp.volleyextensions.volleyer;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.navercorp.volleyextensions.volleyer.request.creator.RequestCreator;
import com.navercorp.volleyextensions.volleyer.request.executor.RequestExecutor;
import com.navercorp.volleyextensions.volleyer.response.parser.NetworkResponseParser;

public class VolleyerConfiguration {
	private RequestCreator requestCreator;
	private RequestExecutor requestExecutor;
	private NetworkResponseParser networkResponseParser;
	@SuppressWarnings("rawtypes")
	private Listener listener;
	private ErrorListener errorListener;

	public VolleyerConfiguration(RequestCreator requestCreator, RequestExecutor requestExecutor, NetworkResponseParser networkResponseParser, @SuppressWarnings("rawtypes") Listener listener, ErrorListener errorListener) {
		this.requestCreator = requestCreator;
		this.requestExecutor = requestExecutor;
		this.networkResponseParser = networkResponseParser;
		this.listener = listener;
		this.errorListener = errorListener;
	}

	public RequestCreator getRequestCreator() {
		return requestCreator;
	}

	public RequestExecutor getRequestExecutor() {
		return requestExecutor;
	}

	public NetworkResponseParser getDefaultNetworkResponseParser() {
		return networkResponseParser;
	}

	@SuppressWarnings("rawtypes")
	public Listener getDefaultListener() {
		return listener;
	}

	public ErrorListener getDefaultErrorListener() {
		return errorListener;
	}
}
