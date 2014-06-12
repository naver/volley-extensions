package com.navercorp.volleyextensions.volleyer.builder;

import com.navercorp.volleyextensions.volleyer.VolleyerConfiguration;
import com.navercorp.volleyextensions.volleyer.http.HttpMethod;

public class GetBuilder extends RequestBuilder<GetBuilder> {

	public GetBuilder(VolleyerConfiguration configuration, String url) {
		super(configuration, url, HttpMethod.GET);
	}

}
