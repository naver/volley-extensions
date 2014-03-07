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
package com.navercorp.volleyextensions.sample.demos;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	ImageLoader loader;
	RequestQueue req;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button gotoMe2dayButton = (Button) findViewById(R.id.gotoMe2dayButton);
		Button gotoNaverShopButton = (Button) findViewById(R.id.gotoNavershopButton);
		gotoMe2dayButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, Me2dayActivity.class);
				MainActivity.this.startActivity(intent);		
			}});
		
		gotoNaverShopButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, NaverShopActivity.class);
				MainActivity.this.startActivity(intent);
			}});
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
