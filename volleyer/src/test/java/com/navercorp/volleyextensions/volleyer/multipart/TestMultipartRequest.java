package com.navercorp.volleyextensions.volleyer.multipart;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.navercorp.volleyextensions.volleyer.multipart.Multipart;
import com.navercorp.volleyextensions.volleyer.multipart.MultipartContainer;

public class TestMultipartRequest extends Request<Object> implements MultipartContainer {

	static final ErrorListener listener = new ErrorListener(){

		@Override
		public void onErrorResponse(VolleyError error) {
			// Do nothing
		}};
	static final String url = "http://test";

	private MultipartContainer mock = null;

	public TestMultipartRequest(String url, int method, MultipartContainer mock) {
		super(method, url, listener);
		this.mock = mock;
	}

	@Override
	public boolean hasMultipart() {
		if (mock != null) {
			return mock.hasMultipart();
		}

		return false;
	}

	@Override
	public Multipart getMultipart() {
		if (mock != null) {
			return mock.getMultipart();
		}
		return null;
	}

	@Override
	protected Response<Object> parseNetworkResponse(NetworkResponse response) {
		Object obj = new Object();
		return Response.success(obj , HttpHeaderParser.parseCacheHeaders(response));
	}

	@Override
	protected void deliverResponse(Object response) {
	}

}
