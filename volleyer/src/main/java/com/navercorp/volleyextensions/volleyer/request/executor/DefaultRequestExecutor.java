package com.navercorp.volleyextensions.volleyer.request.executor;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.navercorp.volleyextensions.volleyer.Volleyer;

public class DefaultRequestExecutor implements RequestExecutor {

	@Override
	public <T> void executeRequest(Request<T> request) {
		if (request == null) {
			return;
		}
		RequestQueue requestQueue = Volleyer.getRequestQueue();
		requestQueue.add(request);
	}

}
