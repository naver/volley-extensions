package com.navercorp.volleyextensions.volleyer.builder;

import com.android.volley.RequestQueue;
import com.navercorp.volleyextensions.volleyer.VolleyerConfiguration;
import com.navercorp.volleyextensions.volleyer.http.HttpMethod;

class TestPurposeRequestBuilder extends RequestBuilder<TestPurposeRequestBuilder> {

	public TestPurposeRequestBuilder(RequestQueue requestQueue, VolleyerConfiguration configuration, String url, HttpMethod method) {
		super(requestQueue, configuration, url, method);
	}

}
