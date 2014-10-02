package com.navercorp.volleyextensions.volleyer.builder;

import com.android.volley.RequestQueue;
import com.navercorp.volleyextensions.volleyer.VolleyerConfiguration;
import com.navercorp.volleyextensions.volleyer.http.HttpMethod;
/**
 * A builder for creating and executing a GET method request
 * @author Wonjun Kim
 *
 */
public class GetBuilder extends RequestBuilder<GetBuilder> {

	public GetBuilder(RequestQueue requestQueue, VolleyerConfiguration configuration, String url) {
		super(requestQueue, configuration, url, HttpMethod.GET);
	}

}
