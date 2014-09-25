package com.navercorp.volleyextensions.volleyer;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.navercorp.volleyextensions.volleyer.request.creator.RequestCreator;
import com.navercorp.volleyextensions.volleyer.request.executor.RequestExecutor;
import com.navercorp.volleyextensions.volleyer.response.parser.NetworkResponseParser;
import com.navercorp.volleyextensions.volleyer.util.VolleyerLog;

/**
 * Configuration class that contains several objects which determines strategies for Volleyer.
 * If you want to know each component, see links below.
 *
 * @see RequestCreator
 * @see RequestExecutor
 * @see NetowrkResponseParser
 * @see ErrorListener
 *
 */
public class VolleyerConfiguration {
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
	/**
	 * Return a RequestCreator instance which creates {@link Request} object.
	 * For details, see {@link RequestCreator}
	 */
	public RequestCreator getRequestCreator() {
		return requestCreator;
	}
	/**
	 * Resturn a RequestExecutor instance which executes a given {@link Request} object.
	 * For details, see {@link RequestExecutor}
	 */
	public RequestExecutor getRequestExecutor() {
		return requestExecutor;
	}
	/**
	 * Return a NetworkResponseParser for which Volleyer uses it when custom NetworkResponseParser is not set during building a request.
	 * @see NetworkResponseParser
	 */
	public NetworkResponseParser getFallbackNetworkResponseParser() {
		return networkResponseParser;
	}
	/**
	 * Return an ErrorListener for which Volleyer uses it when custom ErrorListener is not set on building a request.
	 */
	public ErrorListener getFallbackErrorListener() {
		return errorListener;
	}
}
