package com.navercorp.volleyextensions.volleyer;

import android.util.Log;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.navercorp.volleyextensions.volleyer.request.creator.DefaultRequestCreator;
import com.navercorp.volleyextensions.volleyer.request.creator.RequestCreator;
import com.navercorp.volleyextensions.volleyer.request.executor.DefaultRequestExecutor;
import com.navercorp.volleyextensions.volleyer.request.executor.RequestExecutor;
import com.navercorp.volleyextensions.volleyer.response.parser.IntegratedNetworkResponseParserBuilder;
import com.navercorp.volleyextensions.volleyer.response.parser.Jackson2NetworkResponseParser;
import com.navercorp.volleyextensions.volleyer.response.parser.NetworkResponseParser;
import com.navercorp.volleyextensions.volleyer.response.parser.SimpleXmlNetworkResponseParser;

public class VolleyerContext {
	private RequestCreator requestCreator = new DefaultRequestCreator();
	private RequestExecutor requestExecutor = new DefaultRequestExecutor();
	private NetworkResponseParser defaultNetworkResponseParser = new IntegratedNetworkResponseParserBuilder()
																	.addParser(new Jackson2NetworkResponseParser())
																	.addParser(new SimpleXmlNetworkResponseParser())
																	.build(); 
	@SuppressWarnings("rawtypes")
	private Listener defaultListener = new Listener() {

		@Override
		public void onResponse(Object response) {
			// Log if "Volleyer" tag is on DEBUG level.
			// TODO : this code needs to be moved to VolleyerLog class.
			if(Log.isLoggable("Volleyer", Log.DEBUG)) {
				Log.d("Volleyer", "RESPONSE : " + response);
			}
		}};

	private ErrorListener defaultErrorListener = new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			// Log if "Volleyer" tag is on DEBUG level.
			// TODO : this code needs to be moved to VolleyerLog class.
			if(Log.isLoggable("Volleyer", Log.DEBUG)) {
				Log.d("Volleyer", "ERROR : " + error);
			}
		}};

	public RequestCreator getRequestCreator() {
		return requestCreator;
	}

	public RequestExecutor getRequestExecutor() {
		return requestExecutor;
	}

	public NetworkResponseParser getDefaultNetworkResponseParser() {
		return defaultNetworkResponseParser;
	}

	@SuppressWarnings("rawtypes")
	public Listener getDefaultListener() {
		return defaultListener;
	}

	public ErrorListener getDefaultErrorListener() {
		return defaultErrorListener;
	}
}
