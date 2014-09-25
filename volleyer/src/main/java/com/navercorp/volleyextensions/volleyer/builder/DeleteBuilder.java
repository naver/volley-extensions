package com.navercorp.volleyextensions.volleyer.builder;

import com.android.volley.RequestQueue;
import com.navercorp.volleyextensions.volleyer.VolleyerConfiguration;
import com.navercorp.volleyextensions.volleyer.http.HttpMethod;
/**
 * A builder for creating and executing a DELETE method request
 * @author Wonjun Kim
 *
 */
public class DeleteBuilder extends RequestBuilder<DeleteBuilder>{

	public DeleteBuilder(RequestQueue requestQueue, VolleyerConfiguration configuration, String url) {
		super(requestQueue, configuration, url, HttpMethod.DELETE);
	}

}
