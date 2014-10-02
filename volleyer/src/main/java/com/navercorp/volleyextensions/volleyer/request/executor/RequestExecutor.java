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
/**
 * <pre>
 * An interface that determines how to execute a Request object after completion of settings by volleyer.
 * To apply the implementation for this interface, you need to set it for {@link VolleyerConfiguration}.
 * </pre>
 */
public interface RequestExecutor {
	/**
	 * <pre>
	 * Execute a {@code Request}.
	 * @param requestQueue running {@code RequestQueue}
	 * @param request A {@code Request} instance which was made just now
	 * </pre>
	 */
	<T> void executeRequest(RequestQueue requestQueue, Request<T> request);
}
