package com.navercorp.volleyextensions.volleyer.request.executor;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.navercorp.volleyextensions.volleyer.Volleyer;
import com.navercorp.volleyextensions.volleyer.util.Assert;

public class DefaultRequestExecutor implements RequestExecutor {

	private RequestQueue requestQueue;

	/**
	 * Default Constructor
	 * <pre>
	 * NOTE:
	 * It assume that {@code RequestQueue} already started when {@code DefaultRequestExecutor} executes a request.
	 * </pre>
	 * @param requestQueue RequestQueue must not be null.
	 */
	public DefaultRequestExecutor(RequestQueue requestQueue) {
		Assert.notNull(requestQueue, "RequestQueue");
		this.requestQueue = requestQueue;
	}

	@Override
	public <T> void executeRequest(Request<T> request) {
		if (request == null) {
			return;
		}
		requestQueue.add(request);
	}

}
