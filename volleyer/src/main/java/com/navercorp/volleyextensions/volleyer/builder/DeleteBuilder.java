package com.navercorp.volleyextensions.volleyer.builder;

import com.navercorp.volleyextensions.volleyer.VolleyerContext;
import com.navercorp.volleyextensions.volleyer.http.HttpMethod;

public class DeleteBuilder extends RequestBuilder<DeleteBuilder>{

	public DeleteBuilder(VolleyerContext volleyerContext, String url) {
		super(volleyerContext, url, HttpMethod.DELETE);
	}

}
