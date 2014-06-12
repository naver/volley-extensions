package com.navercorp.volleyextensions.volleyer;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.navercorp.volleyextensions.volleyer.request.creator.RequestCreator;
import com.navercorp.volleyextensions.volleyer.request.executor.RequestExecutor;
import com.navercorp.volleyextensions.volleyer.response.parser.NetworkResponseParser;
import com.navercorp.volleyextensions.volleyer.util.VolleyerLog;

public class VolleyerConfiguration {
	@SuppressWarnings("rawtypes")
	private static final Listener listener = new Listener() {

		@Override
		public void onResponse(Object response) {
			VolleyerLog.debug("RESPONSE : " + response);
		}};

	private RequestCreator requestCreator;
	private RequestExecutor requestExecutor;
	private NetworkResponseParser networkResponseParser;
	private ErrorListener errorListener;

	public VolleyerConfiguration(RequestCreator requestCreator, RequestExecutor requestExecutor, NetworkResponseParser networkResponseParser, ErrorListener errorListener) {
		this.requestCreator = requestCreator;
		this.requestExecutor = requestExecutor;
		this.networkResponseParser = networkResponseParser;
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
