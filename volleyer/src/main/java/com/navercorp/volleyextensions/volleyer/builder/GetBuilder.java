package com.navercorp.volleyextensions.volleyer.builder;

import com.navercorp.volleyextensions.volleyer.VolleyerContext;
import com.navercorp.volleyextensions.volleyer.http.HttpMethod;

public class GetBuilder extends RequestBuilder<GetBuilder> {

	public GetBuilder(VolleyerContext volleyerContext, String url) {
		super(volleyerContext, url, HttpMethod.GET);
	}

}
