package com.navercorp.volleyextensions.volleyer.http;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.navercorp.volleyextensions.volleyer.multipart.Multipart;
import com.navercorp.volleyextensions.volleyer.multipart.MultipartContainer;
import com.navercorp.volleyextensions.volleyer.multipart.Part;
import com.navercorp.volleyextensions.volleyer.util.Assert;

public class HttpContent implements MultipartContainer {
	private HttpMethod method;
	private String url;
	private Map<String, String> headers;
	private byte[] body;
	private Multipart multipart;

	public HttpContent(String url, HttpMethod method) {
		Assert.notNull(url, "url");
		Assert.notNull(method, "HttpMethod");

		this.url = url;
		this.method = method;
		headers = new HashMap<String, String>();
		multipart = new Multipart();
	}

	public HttpMethod getMethod() {
		return method;
	}

	public String getUrl() {
		return url;
	}

	public void addHeader(String key, String value) {
		Assert.notNull(key, "Header key");
		Assert.notNull(value, "Header value");
		headers.put(key, value);
	}

	public String getHeader(String key) {
		return headers.get(key);
	}

	public Map<String, String> getHeaders() {
		return Collections.unmodifiableMap(headers);
	}

	public void setBody(byte[] body) {
		this.body = body;
	}

	public byte[] getBody() {
		return body;
	}

	@Override
	public boolean hasMultipart() {
		return !multipart.isEmpty();
	}

	@Override
	public Multipart getMultipart() {
		return multipart;
	}

	public void addPart(Part part) {
		multipart.add(part);
	}
}
