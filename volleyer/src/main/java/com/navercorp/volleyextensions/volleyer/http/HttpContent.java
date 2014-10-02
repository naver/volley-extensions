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
package com.navercorp.volleyextensions.volleyer.http;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.navercorp.volleyextensions.volleyer.multipart.Multipart;
import com.navercorp.volleyextensions.volleyer.multipart.MultipartContainer;
import com.navercorp.volleyextensions.volleyer.multipart.Part;
import com.navercorp.volleyextensions.volleyer.util.Assert;
/**
 * A class containing an information of HTTP request.
 * @author Wonjun Kim
 *
 */
public class HttpContent implements MultipartContainer {
	private HttpMethod method;
	private String url;
	private Map<String, String> headers;
	private byte[] body;
	private Multipart multipart;

	/**
	 * Default constructor for HttpContent
	 * @param url url string, must not null and start url scheme.
	 * @param method Http method, must not null.
	 */
	public HttpContent(String url, HttpMethod method) {
		Assert.notNull(url, "url");
		Assert.notNull(method, "HttpMethod");

		this.url = url;
		this.method = method;
		headers = new HashMap<String, String>();
		multipart = new Multipart();
	}

	/**
	 * Return a Http method.
	 */
	public HttpMethod getMethod() {
		return method;
	}

	/**
	 * Return a url string.
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Add a request header.
	 * 
	 * @param key key of a header, must not be null.
	 * @param value value of a header, must not be null.
	 */
	public void addHeader(String key, String value) {
		Assert.notNull(key, "Header key");
		Assert.notNull(value, "Header value");
		headers.put(key, value);
	}

	/**
	 * Return value of a header.
	 * @param key header key to find
	 * @return value if it exists
	 */
	public String getHeader(String key) {
		return headers.get(key);
	}

	/**
	 * Return a map of headers.
	 * @return unmodifiable map of headers
	 */
	public Map<String, String> getHeaders() {
		return Collections.unmodifiableMap(headers);
	}

	/**
	 * <pre>
	 * Set body.
	 * NOTE : Volleyer includes a body for a Request if and only if HttpContent does not have a multipart.
	 * </pre>
	 * @param body byte array of body
	 */
	public void setBody(byte[] body) {
		this.body = body;
	}
	/**
	 * Return body as a byte array.
	 */
	public byte[] getBody() {
		return body;
	}
	/**
	 * @return true if multipart exists
	 */
	@Override
	public boolean hasMultipart() {
		return !multipart.isEmpty();
	}
	/**
	 * Return a Multipart instance.
	 */
	@Override
	public Multipart getMultipart() {
		return multipart;
	}
	/**
	 * Add a part into multipart.
	 * @param part must not be null
	 */
	public void addPart(Part part) {
		multipart.add(part);
	}
}
