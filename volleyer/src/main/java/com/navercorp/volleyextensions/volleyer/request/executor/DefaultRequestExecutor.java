package com.navercorp.volleyextensions.volleyer.request.executor;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;

public class DefaultRequestExecutor implements RequestExecutor {

	@Override
	public <T> void executeRequest(RequestQueue requestQueue, Request<T> request) {
		if (request == null) {
			return;
		}

		if (requestQueue == null) {
			deliverError(request, "RequestQueue is null. It cannot execute the request of " + request.toString() + ".");
			return;
		}

		requestQueue.add(request);
	}

	private <T> void deliverError(Request<T> request, String message) {
		VolleyError error = new VolleyError(message);
		request.deliverError(error);
	}

}
