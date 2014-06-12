package com.navercorp.volleyextensions.volleyer.builder;

import com.navercorp.volleyextensions.volleyer.VolleyerConfiguration;
import com.navercorp.volleyextensions.volleyer.http.HttpMethod;

public class PutBuilder extends RequestBuilder<PutBuilder> {

	public PutBuilder(VolleyerConfiguration configuration, String url) {
		super(configuration, url, HttpMethod.PUT);
	}

}
