package com.navercorp.volleyextensions.volleyer;

import com.navercorp.volleyextensions.volleyer.request.creator.DefaultRequestCreator;
import com.navercorp.volleyextensions.volleyer.request.creator.RequestCreator;
import com.navercorp.volleyextensions.volleyer.request.executor.DefaultRequestExecutor;
import com.navercorp.volleyextensions.volleyer.request.executor.RequestExecutor;

public class VolleyerContext {
	private RequestCreator requestCreator = new DefaultRequestCreator();
	private RequestExecutor requestExecutor = new DefaultRequestExecutor();
	public RequestCreator getRequestCreator() {
		return requestCreator;
	}

	public RequestExecutor getRequestExecutor() {
		return requestExecutor;
	}
}
