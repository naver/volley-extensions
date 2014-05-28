package com.navercorp.volleyextensions.volleyer.builder;

import com.navercorp.volleyextensions.volleyer.VolleyerContext;
import com.navercorp.volleyextensions.volleyer.http.HttpContent;
import com.navercorp.volleyextensions.volleyer.util.Assert;

class TargetClassBuilder {

	private VolleyerContext volleyerContext;
	private HttpContent httpContent;

	private boolean isDoneToBuild = false;

	TargetClassBuilder(VolleyerContext volleyerContext, HttpContent httpContent) {
		Assert.notNull(volleyerContext, "VolleyerContext");
		Assert.notNull(httpContent, "HttpContent");

		this.volleyerContext = volleyerContext;
		this.httpContent = httpContent;
	}

	public <T> ResponseBuilder<T> setTargetClass(Class<T> clazz) {
		Assert.notNull(clazz, "Target Class token");

		assertFinishState();

		ResponseBuilder<T> builder = new ResponseBuilder<T>(volleyerContext, httpContent, clazz);
		isDoneToBuild = true;
		return builder;
	}

	private void assertFinishState() {
		if (isDoneToBuild == true) {
			throw new IllegalStateException("RequestBuilder should not be used any more. Because setTargetClass() has been called.");
		}
	}

}
