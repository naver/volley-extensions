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
package com.navercorp.volleyextensions.volleyer.request;

import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.navercorp.volleyextensions.volleyer.http.HttpContent;
import com.navercorp.volleyextensions.volleyer.multipart.Multipart;
import com.navercorp.volleyextensions.volleyer.multipart.MultipartContainer;
import com.navercorp.volleyextensions.volleyer.response.parser.NetworkResponseParser;
import com.navercorp.volleyextensions.volleyer.util.Assert;
/**
 * <pre>
 * A Request class that is made from a {@link ResponseBuilder} after completion of settings by volleyer.
 * If you want to use other Request class, implement other {@link RequestCreator} class.
 * 
 * @see RequestCreator
 * </pre>
 */
public class VolleyerRequest<T> extends Request<T> implements MultipartContainer {

	private NetworkResponseParser responseParser;
	private Listener<T> listener;
	private Class<T> clazz;
	private HttpContent httpContent;
	/**
	 * @param httpContent HttpContent instance from builder
	 * @param clazz Target class from builder
	 * @param responseParser NetworkResponseParser instance from builder
	 * @param listener Volley listener instance from builder
	 * @param errorListener error listener instance from builder
	 */
	public VolleyerRequest(HttpContent httpContent, Class<T> clazz, NetworkResponseParser responseParser, Listener<T> listener, ErrorListener errorListener) {
		super(httpContent.getMethod().getMethodCode(), httpContent.getUrl(), errorListener);

		Assert.notNull(clazz, "Target class token");
		Assert.notNull(responseParser, "NetworkResponseParser");
		Assert.notNull(listener, "listener");

		this.httpContent = httpContent;
		this.responseParser = responseParser;
		this.listener = listener;
		this.clazz = clazz;
		
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		return httpContent.getHeaders();
	}

	@Override
	public byte[] getBody() {
		return httpContent.getBody();
	}

	@Override
	protected void deliverResponse(T result) {
		listener.onResponse(result);
	}
	/**
	 * @return Specific type object of an converted object from response data
	 */
	protected Class<T> getTargetClass() {
		return this.clazz;
	}

	protected Response<T> parseNetworkResponse(NetworkResponse response) {
		return responseParser.parseNetworkResponse(response, clazz);
	}

	@Override
	public boolean hasMultipart() {
		return httpContent.hasMultipart();
	}

	@Override
	public Multipart getMultipart() {
		return httpContent.getMultipart();
	}
}
