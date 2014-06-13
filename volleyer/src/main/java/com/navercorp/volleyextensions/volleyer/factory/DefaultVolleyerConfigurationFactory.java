package com.navercorp.volleyextensions.volleyer.factory;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.navercorp.volleyextensions.volleyer.VolleyerConfiguration;
import com.navercorp.volleyextensions.volleyer.request.creator.DefaultRequestCreator;
import com.navercorp.volleyextensions.volleyer.request.creator.RequestCreator;
import com.navercorp.volleyextensions.volleyer.request.executor.DefaultRequestExecutor;
import com.navercorp.volleyextensions.volleyer.request.executor.RequestExecutor;
import com.navercorp.volleyextensions.volleyer.response.parser.*;
import com.navercorp.volleyextensions.volleyer.util.Assert;
import com.navercorp.volleyextensions.volleyer.util.VolleyerLog;

public class DefaultVolleyerConfigurationFactory {
	/**
	 * Default VolleyerConfiguration is immutable and properties of it are immutable too.
	 * Internally, DefaultVolleyerConfigurationFactory keeps default VolleyerConfiguration as singleton.
	 * 
	 * @author Wonjun Kim
	 *
	 */
	private static class ConfigurationHolder {
		private final static VolleyerConfiguration configuration;
		static {
			RequestCreator requestCreator = createRequestCreator();
			RequestExecutor requestExecutor = createRequestExecutor();
			NetworkResponseParser networkResponseParser = createNetworkResponseParser();
			ErrorListener errorListener = createErrorListener();
			configuration = new VolleyerConfiguration(requestCreator, requestExecutor, networkResponseParser, errorListener);
		}

		private static VolleyerConfiguration getConfiguration() {
			return configuration;
		}
	}
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
	public static VolleyerConfiguration create() {
		return ConfigurationHolder.getConfiguration();
	}

}
