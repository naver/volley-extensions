package com.navercorp.volleyextensions.volleyer.request.executor;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

public interface RequestExecutor {
	<T> void executeRequest(RequestQueue requestQueue, Request<T> request);
}
