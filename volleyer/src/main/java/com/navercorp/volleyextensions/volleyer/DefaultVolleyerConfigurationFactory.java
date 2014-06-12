package com.navercorp.volleyextensions.volleyer;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.navercorp.volleyextensions.volleyer.request.creator.DefaultRequestCreator;
import com.navercorp.volleyextensions.volleyer.request.creator.RequestCreator;
import com.navercorp.volleyextensions.volleyer.request.executor.DefaultRequestExecutor;
import com.navercorp.volleyextensions.volleyer.request.executor.RequestExecutor;
import com.navercorp.volleyextensions.volleyer.response.parser.IntegratedNetworkResponseParserBuilder;
import com.navercorp.volleyextensions.volleyer.response.parser.Jackson2NetworkResponseParser;
import com.navercorp.volleyextensions.volleyer.response.parser.NetworkResponseParser;
import com.navercorp.volleyextensions.volleyer.response.parser.SimpleXmlNetworkResponseParser;
import com.navercorp.volleyextensions.volleyer.util.Assert;
import com.navercorp.volleyextensions.volleyer.util.VolleyerLog;

public class DefaultVolleyerConfigurationFactory {

	@SuppressWarnings("rawtypes")
	private static Listener defaultListener = new Listener() {

		@Override
		public void onResponse(Object response) {
			VolleyerLog.debug("RESPONSE : " + response);
		}};

	private static ErrorListener defaultErrorListener = new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			VolleyerLog.debug("ERROR : ", error);
		}};

	public static NetworkResponseParser createNetworkResponseParser() {
		return new IntegratedNetworkResponseParserBuilder()
				.addParser(new Jackson2NetworkResponseParser())
				.addParser(new SimpleXmlNetworkResponseParser())
				.build();
	}

	public static RequestCreator createRequestCreator() {
		return new DefaultRequestCreator();
	}

	public static RequestExecutor createRequestExecutor() {
		return new DefaultRequestExecutor();
	}

	/**
	 * Create a default listener.
	 * (But, This method just returns the same instance actually.)
	 * @return default listener
	 */
	@SuppressWarnings("rawtypes")
	public static Listener createListener() {
		return defaultListener;
	}

	/**
	 * Create a default error listener.
	 * (But, This method just returns the same instance actually.)
	 * @return default error listener
	 */
	public static ErrorListener createErrorListener() {
		return defaultErrorListener;
	}

	/**
	 * Create a VolleyerConfiguration instance which includes default implementations.
	 * @return VolleyerConfiguration instance
	 */
	@SuppressWarnings("rawtypes")
	public static VolleyerConfiguration create() {
		RequestCreator requestCreator = createRequestCreator();
		RequestExecutor requestExecutor = createRequestExecutor();
		NetworkResponseParser networkResponseParser = createNetworkResponseParser();
		Listener listener = createListener();
		ErrorListener errorListener = createErrorListener();

		VolleyerConfiguration configuration = new VolleyerConfiguration(requestCreator, requestExecutor, networkResponseParser, listener, errorListener);
		return configuration;
	}

}
