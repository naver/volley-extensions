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
package com.navercorp.volleyextensions.sample.demos.amazon.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.navercorp.volleyextensions.request.SimpleXmlRequest;
import com.navercorp.volleyextensions.sample.demos.R;
import com.navercorp.volleyextensions.sample.demos.amazon.listview.AmazonListAdapter;
import com.navercorp.volleyextensions.sample.demos.amazon.model.ShoppingRssFeed;
import com.navercorp.volleyextensions.sample.demos.application.volley.MyVolley;

public class AmazonActivity extends Activity {
	private static final String GET_ITEMS_URL = "http://www.amazon.com/rss/tag/running/recent/ref=tag_rsh_hl_ersr";

	private RequestQueue requestQueue;
	private ImageLoader loader;
	private ListView listView;
	private ProgressBar loadingProgress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_amazon);

		listView = (ListView) findViewById(R.id.listView);
		loadingProgress = (ProgressBar) findViewById(R.id.loadingProgress);

		loader = MyVolley.getImageLoader();
		requestQueue = MyVolley.getRequestQueue();

		loadShoppingItems();
	}

	private void loadShoppingItems() {
		SimpleXmlRequest<ShoppingRssFeed> request = new SimpleXmlRequest<ShoppingRssFeed>(
				GET_ITEMS_URL, ShoppingRssFeed.class,
				new Listener<ShoppingRssFeed>() {

					@Override
					public void onResponse(final ShoppingRssFeed feed) {
						// Hide a loading bar, and show a list view
						loadingProgress.setVisibility(View.INVISIBLE);
						listView.setVisibility(View.VISIBLE);

						listView.setAdapter(new AmazonListAdapter(
								AmazonActivity.this, feed.getChannel()
										.getShoppingItems()));
					}
				});

		requestQueue.add(request);
	}

	@Override
	protected void onStart() {
		super.onStart();

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

}
