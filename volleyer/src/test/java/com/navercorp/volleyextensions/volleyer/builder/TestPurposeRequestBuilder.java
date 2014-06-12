package com.navercorp.volleyextensions.volleyer.builder;

import com.navercorp.volleyextensions.volleyer.VolleyerContext;
import com.navercorp.volleyextensions.volleyer.http.HttpMethod;

class TestPurposeRequestBuilder extends RequestBuilder<TestPurposeRequestBuilder> {

	public TestPurposeRequestBuilder(VolleyerContext volleyerContext, String url, HttpMethod method) {
		super(volleyerContext, url, method);
	}

}
