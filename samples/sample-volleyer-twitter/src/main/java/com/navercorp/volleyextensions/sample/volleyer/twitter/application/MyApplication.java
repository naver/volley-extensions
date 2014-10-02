package com.navercorp.volleyextensions.sample.volleyer.twitter.application;

import static com.navercorp.volleyextensions.volleyer.Volleyer.*;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.navercorp.volleyextensions.sample.volleyer.twitter.client.Twitter;
import com.navercorp.volleyextensions.volleyer.factory.DefaultRequestQueueFactory;
import com.navercorp.volleyextensions.cache.universalimageloader.memory.impl.UniversalLruMemoryCache;

import android.app.Application;

public class MyApplication extends Application {

	private static final int DEFAULT_CACHE_SIZE = 10 * 1024 * 1024;

	private static Twitter twitter;
	private static ImageLoader imageLoader;
	RequestQueue rq;

	@Override
	public void onCreate() {
		super.onCreate();
		initVolley();
		initImageLoader();
		initVolleyer();
	}

	private void initImageLoader() {
		imageLoader = new ImageLoader(rq, new UniversalLruMemoryCache(DEFAULT_CACHE_SIZE));
	}

	private void initVolley() {
		rq = DefaultRequestQueueFactory.create(this);
		rq.start();
	}

	private void initVolleyer() {
		volleyer(rq).settings().setAsDefault().done();
	}

	public synchronized static void setTwitter(Twitter twitter) {
		MyApplication.twitter = twitter;
	}

	public synchronized static Twitter getTwitter() {
		return twitter;
	}

	public static ImageLoader getImageLoader() {
		return imageLoader;
	}
}
