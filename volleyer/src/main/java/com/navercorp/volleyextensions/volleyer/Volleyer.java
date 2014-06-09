package com.navercorp.volleyextensions.volleyer;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.navercorp.volleyextensions.volleyer.util.Assert;
import com.navercorp.volleyextensions.volleyer.util.VolleyerLog;
/**
 * Volleyer helps you to create a request and execute it.
 * <pre>
 * NOTE : First of all, {@link #init(Context)} should be called from your Application class.
 * And you can use APIs of {@code Volleyer}.
 * </pre>
 * @author Wonjun Kim
 *
 */
public class Volleyer {

	private static RequestQueue requestQueue = null;
	private static VolleyerContext volleyerContext = null;
	private static boolean isInitialized;

	public static RequestQueue getRequestQueue() {
		assertInitialized();
		return requestQueue;
	}
	/**
	 * Initialize Volleyer environment.
	 * <pre>
	 * NOTE : RequestQueue starts automatically in this method.
	 * 
	 * WARNING : You have to call this in you Application class.
	 * This method is not thread-safe. Do not call this elsewhere.
	 * </pre>
	 * @param context Application context. This should not be null.
	 */
	public static void init(Context context) {
		Assert.notNull(context, "Context");
		init(DefaultRequestQueueFactory.create(context));
	}

	/**
	 * Initialize Volleyer environment.
	 * Custom RequestQueue will be set.
	 * <pre>
	 * NOTE : RequestQueue starts automatically in this method.
	 * 
	 * WARNING : You have to call this in you Application class.
	 * This method is not thread-safe. Do not call this elsewhere.
	 * </pre>
	 * @param requestQueue Custom RequestQueue you want to use. This should not be null.
	 */
	public static void init(RequestQueue requestQueue) {
		init(requestQueue, DefaultVolleyerContextFactory.create(requestQueue));
	}

	/**
	 * Initialize Volleyer environment.
	 * Custom RequestQueue and default VolleyerContext will be set.
	 * <pre>
	 * NOTE : RequestQueue starts automatically in this method.
	 * 
	 * WARNING : You have to call this in you Application class.
	 * This method is not thread-safe. Do not call this elsewhere.
	 * </pre>
	 * @param requestQueue Custom RequestQueue you want to use. This should not be null.
	 * @param volleyerContext Custom VolleyerContext. This should not be null.
	 */
	public static void init(RequestQueue requestQueue, VolleyerContext volleyerContext) {
		if(isInitialized == true) {
			VolleyerLog.warn("Volleyer is already initialized. So it ignores this call.");
			return;
		}

		Assert.notNull(requestQueue, "RequestQueue");
		Assert.notNull(volleyerContext, "VolleyerContext");
		Volleyer.requestQueue = requestQueue;
		Volleyer.volleyerContext = volleyerContext;
		requestQueue.start();

		isInitialized = true;
	}

	private static void assertInitialized() {
		if (isInitialized == false) {
			throw new IllegalStateException("Volleyer is not initialized. Call init() from your Application class first.");
		}
	}

	/**
	 * Clear Volleyer environment.
	 * <pre>
	 * NOTE : This method is not a public API. It's only used for test cases.
	 * </pre>
	 */
	static void clear() {
		if (requestQueue != null) {
			requestQueue.stop();
			requestQueue = null;
		}
		volleyerContext = null;
		isInitialized = false;
	}
}
