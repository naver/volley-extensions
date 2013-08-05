package com.nhncorp.volleyextensions.mock;

import com.android.volley.Response.Listener;

public class ResponseHoldListener<T> implements Listener<T> {
	private T response;
	@Override
	public void onResponse(T response) {
		this.response = response;
	}
	public T getLastResponse() {
		return this.response;
	}
}