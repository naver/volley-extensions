package com.navercorp.volleyextensions.volleyer;

import com.navercorp.volleyextensions.volleyer.request.creator.DefaultRequestCreator;
import com.navercorp.volleyextensions.volleyer.request.creator.RequestCreator;

public class VolleyerContext {
	private RequestCreator requestCreator = new DefaultRequestCreator();
	public RequestCreator getRequestCreator() {
		return requestCreator;
	}
}
