/*
 * Copyright (C) 2014 Naver Business Platform Corp.
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
package com.example.testvolleyextensions;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.nhncorp.volleyextensions.request.Jackson2Request;
import com.nhncorp.volleyextensions.request.SimpleXmlRequest;

public class NaverShopActivity extends Activity {
	private static final String ITEMS_PER_PAGE = "50";
	private static final String GET_ITEMS_URL = "http://openapi.naver.com/search?key=5f7c1042d0a71a565da51fbca37395b8&query=door&start=1&display="
			+ ITEMS_PER_PAGE + "&target=shop&sort=sim";

	private RequestQueue requestQueue;
	private ImageLoader loader;
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navershop);

		listView = (ListView) findViewById(R.id.listView);

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

						listView.setAdapter(new NaverShopListAdapter(
								NaverShopActivity.this, feed.getChannel()
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
