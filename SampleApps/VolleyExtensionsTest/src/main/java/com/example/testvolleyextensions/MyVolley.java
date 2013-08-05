package com.example.testvolleyextensions;

import java.io.File;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import android.content.Context;
import android.graphics.Bitmap;

import com.android.volley.Cache;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.HurlStack.UrlRewriter;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.NoCache;
import com.nhncorp.volleyextensions.cache.disc.impl.UniversalUnlimitedDiscCache;
import com.nhncorp.volleyextensions.cache.memory.impl.UniversalLimitedAgeMemoryCache;
import com.nhncorp.volleyextensions.cache.memory.impl.UniversalLruMemoryCache;

public class MyVolley {
	private static final int DEFAULT_FILE_COUNT = 3;
	private static final int DEFAULT_CACHE_SIZE = 10 * 1024 * 1024;
	private static final long DEFAULT_MAX_AGE = 60;
	private static RequestQueue requestQueue;
	private static ImageLoader imageLoader;

	public static void init(Context context) {
		if (context == null)
			throw new NullPointerException("context must not be null.");

//		initializeSSL();
		Cache diskCache = getDefaultDiskCache(context);
		ImageCache memoryCache = getDefaultImageCache(context);
		requestQueue = new RequestQueue(diskCache, new BasicNetwork(
				new HurlStack()));

		imageLoader = new ImageLoader(requestQueue, memoryCache);

		requestQueue.start();
	}

	private static void initializeSSL() {
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			@Override
			public void checkClientTrusted(X509Certificate[] arg0, String arg1)
					throws CertificateException {
				// TODO Auto-generated method stub

			}

			@Override
			public void checkServerTrusted(X509Certificate[] arg0, String arg1)
					throws CertificateException {
				// TODO Auto-generated method stub

			}
		} };
		// Install the all-trusting trust manager
		SSLContext sc;
		try {
			sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());

			HttpsURLConnection
					.setDefaultSSLSocketFactory(sc.getSocketFactory());
			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {

				@Override
				public boolean verify(String hostname, SSLSession session) {
					// TODO Auto-generated method stub
					return true;
				}
			};

			// Install the all-trusting host verifier
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
			HttpsURLConnection
					.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (KeyManagementException e) {

		}

	}

	public static RequestQueue getRequestQueue() {
		if (requestQueue == null)
			throw new IllegalStateException("RequestQueue is not initialized.");
		return requestQueue;
	}

	public static ImageLoader getImageLoader() {
		if (imageLoader == null)
			throw new IllegalStateException("ImageLoader is not initialized.");
		return imageLoader;
	}

	private static ImageCache getDefaultImageCache(Context context) {
		return new UniversalLimitedAgeMemoryCache(new UniversalLruMemoryCache(
				DEFAULT_CACHE_SIZE), DEFAULT_MAX_AGE);
	}

	private static Cache getDefaultDiskCache(Context context) {
		File cacheDir = getCacheDir(context);
		if (cacheDir == null) {
			return new NoCache();
		}

		return new UniversalUnlimitedDiscCache(cacheDir);
	}

	private static File getCacheDir(Context context) {
		File file = new File(context.getCacheDir().getPath()
				+ "/test-universal");
		return file;
	}
}
