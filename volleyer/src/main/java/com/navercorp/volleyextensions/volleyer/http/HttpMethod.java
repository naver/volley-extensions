package com.navercorp.volleyextensions.volleyer.http;

import com.android.volley.Request;

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
