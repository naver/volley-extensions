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
package com.navercorp.volleyextensions.sample.volleyer.twitter.activity;

import com.navercorp.volleyextensions.sample.volleyer.twitter.R;
import com.navercorp.volleyextensions.sample.volleyer.twitter.application.MyApplication;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	private Button statusButton;
	private View homeTimelineButton;
	private Button logoutButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initializeButtons();
	}

	private void initializeButtons() {
		statusButton = (Button) findViewById(R.id.status);
		homeTimelineButton = (Button) findViewById(R.id.home_timeline);
		logoutButton = (Button) findViewById(R.id.logout);

		statusButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				Intent intent = new Intent(MainActivity.this, StatusActivity.class);
				startActivity(intent);
			}});
		homeTimelineButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				Intent intent = new Intent(MainActivity.this, ListActivity.class);
				startActivity(intent);
			}});
		logoutButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				MyApplication.setTwitter(null);
				jumpToLoginActivityIfNotLogin();
			}});
	}

	@Override
	protected void onStart() {
		super.onStart();
		jumpToLoginActivityIfNotLogin();
	}

	private void jumpToLoginActivityIfNotLogin() {
		if (MyApplication.getTwitter() == null) {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
