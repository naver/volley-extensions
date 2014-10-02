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
package com.navercorp.volleyextensions.volleyer.response.parser;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
/**
 * <pre>
 * An interface for helping to parse data of {@code NetworkResponse} to a target class.
 * 
 * NOTE : If you implement new NetworkResponseParser,
 *        it's recommend you to make it thread-safe and use it as a singleton.
 * </pre>
 * @see NetworkResponse
 * 
 */
public interface NetworkResponseParser {
	/**
	 * Parse data of {@code NetworkResponse} to T object.
	 * @param <T> Target class that data will be parsed to.
	 * @return Response which contains parsed T object or contains some error if it happened.
	 */
	<T> Response<T> parseNetworkResponse(NetworkResponse response, Class<T> clazz);
}
