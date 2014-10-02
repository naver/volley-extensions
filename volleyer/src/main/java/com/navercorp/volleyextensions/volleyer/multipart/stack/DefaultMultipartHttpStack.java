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
package com.navercorp.volleyextensions.volleyer.multipart.stack;

import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;

import android.net.http.AndroidHttpClient;
import android.os.Build;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
/**
 * <pre>
 * Default multipart http stack.
 * It includes a internal multipart stack which is determined by an Android version on a device.
 * </pre>
 */
public class DefaultMultipartHttpStack implements MultipartHttpStack {

	private static final String DEFAULT_USER_AGENT = "volleyer-multipart";
	private MultipartHttpStack stack;
	/**
	 * Default constructor
	 */
	public DefaultMultipartHttpStack() {
		determineMultipartStack(DEFAULT_USER_AGENT);
	}
	/**
	 * <pre>
	 * Constructor with custom user agent for Apache client.
	 * (This user agent is valid only if the device chooses not UrlConnection, but Apache client.).
	 * </pre>
	 */
	public DefaultMultipartHttpStack(String userAgent) {
		determineMultipartStack(userAgent);
	}
	/**
	 * <pre>
	 * Constructor with custom Apache http client.
	 * (This client is valid only if the device chooses not UrlConnection, but Apache client.).
	 * </pre>
	 */
	public DefaultMultipartHttpStack(HttpClient httpClient) {
		determineMultipartStack(httpClient);
	}

	private void determineMultipartStack(String userAgent) {
		HttpClient httpClient = AndroidHttpClient.newInstance(userAgent);
		determineMultipartStack(httpClient);
	}

	private void determineMultipartStack(HttpClient httpClient) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            stack = new MultipartHurlStack();
            return;
        }

		httpClient = createHttpClientIfNull(httpClient);
		// Prior to Gingerbread, HttpUrlConnection was unreliable.
		// See: http://android-developers.blogspot.com/2011/09/androids-http-clients.html
		stack = new MultipartHttpClientStack(httpClient);
	}

	private HttpClient createHttpClientIfNull(HttpClient httpClient) {
		if (httpClient != null) {
			return httpClient;
		}

		return AndroidHttpClient.newInstance(DEFAULT_USER_AGENT);
	}

	@Override
	public HttpResponse performRequest(Request<?> request, Map<String, String> additionalHeaders) throws IOException, AuthFailureError {
		return stack.performRequest(request, additionalHeaders);
	}

}
