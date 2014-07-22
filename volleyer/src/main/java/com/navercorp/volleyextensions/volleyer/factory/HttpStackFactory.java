package com.navercorp.volleyextensions.volleyer.factory;

import org.apache.http.client.HttpClient;

import android.net.http.AndroidHttpClient;
import android.os.Build;

import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.navercorp.volleyextensions.volleyer.multipart.stack.*;

public class HttpStackFactory {

	private static final String DEFAULT_USER_AGENT = "volleyer";

	public static HttpStack createDefaultHttpStack() {
		HttpStack httpStack = null;
		HttpClient httpClient = null;

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			httpStack = createHurlStack();
        } else {
			// Prior to Gingerbread, HttpUrlConnection was unreliable.
			// See: http://android-developers.blogspot.com/2011/09/androids-http-clients.html
			httpClient = createAndroidHttpClient(DEFAULT_USER_AGENT);
			httpStack = createHttpClientStack(httpClient);
        }

		MultipartHttpStack multipartStack = new DefaultMultipartHttpStack(httpClient);
		HttpStack completedStack = new MultipartHttpStackWrapper(httpStack, multipartStack);

		return completedStack;
	}

	public static HurlStack createHurlStack() {
		return new HurlStack();
	}

	public static HttpClientStack createHttpClientStack(HttpClient httpClient) {
		return new HttpClientStack(httpClient);
	}

	private static HttpClient createAndroidHttpClient(String userAgent) {
		return AndroidHttpClient.newInstance(userAgent);
	}
}
