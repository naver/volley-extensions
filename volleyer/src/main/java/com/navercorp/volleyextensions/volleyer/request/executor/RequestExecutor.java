package com.navercorp.volleyextensions.volleyer.request.executor;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
/**
 * <pre>
 * An interface that determines how to execute a Request object after completion of settings by volleyer.
 * To apply the implementation for this interface, you need to set it for {@link VolleyerConfiguration}.
 * </pre>
 */
public interface RequestExecutor {
	/**
	 * <pre>
	 * Execute a {@code Request}.
	 * @param requestQueue running {@code RequestQueue}
	 * @param request A {@code Request} instance which was made just now
	 * </pre>
	 */
	<T> void executeRequest(RequestQueue requestQueue, Request<T> request);
}
