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
package com.navercorp.volleyextensions.volleyer.multipart;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.navercorp.volleyextensions.volleyer.multipart.Multipart;
import com.navercorp.volleyextensions.volleyer.multipart.MultipartContainer;

public class TestMultipartRequest extends Request<Object> implements MultipartContainer {

	static final ErrorListener listener = new ErrorListener(){

		@Override
		public void onErrorResponse(VolleyError error) {
			// Do nothing
		}};
	static final String url = "http://test";

	private MultipartContainer mock = null;

	public TestMultipartRequest(String url, int method, MultipartContainer mock) {
		super(method, url, listener);
		this.mock = mock;
	}

	@Override
	public boolean hasMultipart() {
		if (mock != null) {
			return mock.hasMultipart();
		}

		return false;
	}

	@Override
	public Multipart getMultipart() {
		if (mock != null) {
			return mock.getMultipart();
		}
		return null;
	}

	@Override
	protected Response<Object> parseNetworkResponse(NetworkResponse response) {
		Object obj = new Object();
		return Response.success(obj , HttpHeaderParser.parseCacheHeaders(response));
	}

	@Override
	protected void deliverResponse(Object response) {
	}

}
