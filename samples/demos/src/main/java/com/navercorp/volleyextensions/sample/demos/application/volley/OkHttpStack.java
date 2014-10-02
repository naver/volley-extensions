/*
 * Copyright (C) 2014 Naver Corp.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.navercorp.volleyextensions.sample.demos.application.volley;

import com.android.volley.toolbox.HurlStack;
import com.squareup.okhttp.OkHttpClient;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * An {@link com.android.volley.toolbox.HttpStack HttpStack} implementation
 * which uses OkHttp as its transport.
 */
public class OkHttpStack extends HurlStack {
	private final OkHttpClient client;

	public OkHttpStack() {
		this(new OkHttpClient());
	}

	public OkHttpStack(OkHttpClient client) {
		if (client == null) {
			throw new NullPointerException("Client must not be null.");
		}
		this.client = client;
	}

	@Override
	protected HttpURLConnection createConnection(URL url) throws IOException {
		return client.open(url);
	}
}