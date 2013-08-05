package com.nhncorp.volleyextensions.mock;

import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;

public class ErrorResponseHoldListener implements ErrorListener {
	private VolleyError error;
	@Override
	public void onErrorResponse(VolleyError error) {
		this.error = error;
	}
	
	public VolleyError getLastError() {
		return this.error;
	}
}
