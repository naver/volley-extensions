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
package com.navercorp.volleyextensions.volleyer.request.executor;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
/**
 * A RequestExecutor implementation class which executes {@code Request} immediately by adding it into {@code Requestqueue}.
 */
public class DefaultRequestExecutor implements RequestExecutor {

	@Override
	public <T> void executeRequest(RequestQueue requestQueue, Request<T> request) {
		if (requestQueue == null) {
			deliverError(request, "RequestQueue is null. It cannot execute the request of " + request.toString() + ".");
			return;
		}

		requestQueue.add(request);
	}

	private <T> void deliverError(Request<T> request, String message) {
		VolleyError error = new VolleyError(message);
		request.deliverError(error);
	}

}
