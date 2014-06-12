package com.navercorp.volleyextensions.volleyer.builder;

import com.navercorp.volleyextensions.volleyer.VolleyerConfiguration;
import com.navercorp.volleyextensions.volleyer.http.HttpMethod;

class TestPurposeRequestBuilder extends RequestBuilder<TestPurposeRequestBuilder> {

	public TestPurposeRequestBuilder(VolleyerConfiguration configuration, String url, HttpMethod method) {
		super(configuration, url, method);
	}

}
