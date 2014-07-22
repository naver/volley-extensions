package com.navercorp.volleyextensions.volleyer.multipart.stack;

import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;

import android.net.http.AndroidHttpClient;
import android.os.Build;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;

public class DefaultMultipartHttpStack implements MultipartHttpStack {

	private static final String DEFAULT_USER_AGENT = "volleyer-multipart";
	private MultipartHttpStack stack;

	public DefaultMultipartHttpStack() {
		determineMultipartStack(DEFAULT_USER_AGENT);
	}

	public DefaultMultipartHttpStack(String userAgent) {
		determineMultipartStack(userAgent);
	}

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
