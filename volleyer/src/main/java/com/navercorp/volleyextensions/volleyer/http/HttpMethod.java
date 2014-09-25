package com.navercorp.volleyextensions.volleyer.http;

import com.android.volley.Request;
/**
 * A wrapper for {@link com.android.volley.Request.Method}
 * @author Wonjun Kim
 *
 */
public enum HttpMethod {
	GET(Request.Method.GET), POST(Request.Method.POST), PUT(Request.Method.PUT), DELETE(Request.Method.DELETE);
	private int methodCode;

	HttpMethod(int methodCode) {
		this.methodCode = methodCode;
	}

	public int getMethodCode() {
		return methodCode;
	}
}
