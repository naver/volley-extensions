package com.navercorp.volleyextensions.volleyer.builder;

import com.navercorp.volleyextensions.volleyer.VolleyerConfiguration;
import com.navercorp.volleyextensions.volleyer.http.HttpMethod;

public class DeleteBuilder extends RequestBuilder<DeleteBuilder>{

	public DeleteBuilder(VolleyerConfiguration configuration, String url) {
		super(configuration, url, HttpMethod.DELETE);
	}

}
