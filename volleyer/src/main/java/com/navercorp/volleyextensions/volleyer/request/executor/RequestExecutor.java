package com.navercorp.volleyextensions.volleyer.request.executor;

import com.android.volley.Request;

public interface RequestExecutor {
	<T> void executeRequest(Request<T> request);
}
