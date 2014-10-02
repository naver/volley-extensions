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

import java.util.List;

import com.android.volley.NetworkResponse;
import com.navercorp.volleyextensions.sample.volleyer.twitter.R;
import com.navercorp.volleyextensions.sample.volleyer.twitter.application.MyApplication;
import com.navercorp.volleyextensions.sample.volleyer.twitter.client.Twitter;
import com.navercorp.volleyextensions.sample.volleyer.twitter.client.model.Status;
import com.navercorp.volleyextensions.sample.volleyer.twitter.client.model.Timeline;
import com.navercorp.volleyextensions.sample.volleyer.twitter.listadapter.TimelineListAdapter;
import com.navercorp.volleyextensions.sample.volleyer.twitter.util.DialogUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

public class ListActivity extends Activity {

	private ListView timelineListView;
	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		initializeDialog();
		initializeListView();
	}

	private void initializeDialog() {
		dialog = DialogUtils.createDialog(this, "Wait for loading");
	}

	private void initializeListView() {
		timelineListView = (ListView) findViewById(R.id.timeline_list);
	}

	@Override
	protected void onStart() {
		super.onStart();
		jumpToLoginActivityIfNotLogin();
		loadTimeline();
	}

	private void loadTimeline() {
		Twitter twitter = MyApplication.getTwitter();
		twitter.getHomeTimeline(new Twitter.Callback<Timeline>() {

			@Override
			public void success(Timeline timeline) {
				Log.d("ListActivity", "success to receive a timeline.");
				List<Status> statuses = timeline.getStatuses();
				updateListView(statuses);
				DialogUtils.hideDialog(dialog);
			}

			@Override
			public void error(NetworkResponse networkResponse) {
				DialogUtils.changeDialogMessage(dialog, "Failed to receive a home timeline.");
			}
		});
	}

	protected void updateListView(List<Status> statuses) {
		TimelineListAdapter adapter = new TimelineListAdapter(this, statuses);
		timelineListView.setAdapter(adapter);
	}

	private void jumpToLoginActivityIfNotLogin() {
		if (MyApplication.getTwitter() == null) {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		DialogUtils.hideDialog(dialog);
	}

	@SuppressLint("ShowToast")
	protected void makeToast(String text) {
		Toast.makeText(getBaseContext(), text, Toast.LENGTH_SHORT).show();	
	}
}
