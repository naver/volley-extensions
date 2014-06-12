package com.navercorp.volleyextensions.volleyer.builder;

import com.navercorp.volleyextensions.volleyer.VolleyerContext;
import com.navercorp.volleyextensions.volleyer.http.HttpMethod;

public class PutBuilder extends RequestBuilder<PutBuilder> {

	public PutBuilder(VolleyerContext volleyerContext, String url) {
		super(volleyerContext, url, HttpMethod.PUT);
	}

}
