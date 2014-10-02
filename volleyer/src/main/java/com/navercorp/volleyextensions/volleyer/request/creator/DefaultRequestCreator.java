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
package com.navercorp.volleyextensions.volleyer.request.creator;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.navercorp.volleyextensions.volleyer.http.HttpContent;
import com.navercorp.volleyextensions.volleyer.request.VolleyerRequest;
import com.navercorp.volleyextensions.volleyer.response.parser.NetworkResponseParser;
/**
 * A RequestCreator implementation class which creates VolleyerRequest.
 */
public class DefaultRequestCreator implements RequestCreator {

	@Override
	public <T> Request<T> createRequest(HttpContent httpContent,
			Class<T> clazz, NetworkResponseParser responseParser,
			Listener<T> listener, ErrorListener errorListener) {
		return new VolleyerRequest<T>(httpContent, clazz, responseParser, listener, errorListener);
	}

}
