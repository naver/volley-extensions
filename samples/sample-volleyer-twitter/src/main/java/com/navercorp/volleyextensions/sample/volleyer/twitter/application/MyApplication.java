/*
 * Copyright (C) 2014 Naver Corp.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
