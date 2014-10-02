package com.navercorp.volleyextensions.volleyer.builder;

import com.android.volley.RequestQueue;
import com.navercorp.volleyextensions.volleyer.VolleyerConfiguration;
import com.navercorp.volleyextensions.volleyer.http.HttpMethod;
/**
 * A builder for creating and executing a PUT method request
 * @author Wonjun Kim
 *
 */
public class PutBuilder extends BodyBuilder<PutBuilder> {

	public PutBuilder(RequestQueue requestQueue, VolleyerConfiguration configuration, String url) {
		super(requestQueue, configuration, url, HttpMethod.PUT);
	}

}
