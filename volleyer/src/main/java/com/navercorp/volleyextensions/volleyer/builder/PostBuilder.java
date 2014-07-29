package com.navercorp.volleyextensions.volleyer.builder;


import com.android.volley.RequestQueue;
import com.navercorp.volleyextensions.volleyer.VolleyerConfiguration;
import com.navercorp.volleyextensions.volleyer.http.HttpMethod;

public class PostBuilder extends BodyBuilder<PostBuilder> {

	public PostBuilder(RequestQueue requestQueue, VolleyerConfiguration configuration, String url) {
		super(requestQueue, configuration, url, HttpMethod.POST);
	}

}
