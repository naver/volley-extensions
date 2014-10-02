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

import static com.navercorp.volleyextensions.volleyer.Volleyer.volleyer;

import java.io.File;

import com.android.volley.NetworkResponse;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.navercorp.volleyextensions.sample.volleyer.twitter.R;
import com.navercorp.volleyextensions.sample.volleyer.twitter.application.MyApplication;
import com.navercorp.volleyextensions.sample.volleyer.twitter.client.Twitter;
import com.navercorp.volleyextensions.sample.volleyer.twitter.client.model.Status;
import com.navercorp.volleyextensions.sample.volleyer.twitter.util.FileUtils;
import com.navercorp.volleyextensions.volleyer.util.StringUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class StatusActivity extends Activity {

	private Button updateButton;
	private EditText statusText;
	private EditText imageUriText;
	boolean firstClick = true;
	private TextView imageBoxText;
	private CheckBox imageCheckBox;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_status);
		initializeButtons();
	}

	private void initializeButtons() {
		updateButton = (Button) findViewById(R.id.update_button);
		statusText = (EditText) findViewById(R.id.status_text);
		imageUriText = (EditText) findViewById(R.id.image_uri_text);
		imageBoxText = (TextView) findViewById(R.id.image_box_text);
		imageCheckBox = (CheckBox) findViewById(R.id.add_image_box);

		updateButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if (updateButton.isEnabled() == false) {
					return;
				}

				updateButton.setEnabled(false /* disabled */);
				String text = String.valueOf(statusText.getText());
				if (firstClick || StringUtils.isEmpty(text)) {
					makeToast("text is empty.");
					updateButton.setEnabled(true /* enabled */);
					return;
				}

				if (!imageCheckBox.isChecked()) {
					updateStatus(text);
					return;
				}

				String url = String.valueOf(imageUriText.getText());
				if (StringUtils.isEmpty(url)) {
					makeToast("Image url is empty.");
					updateButton.setEnabled(true /* enabled */);
					return;
				}

				downloadImageAndUpdateStatusWithMedia(text, url);
			}});

		statusText.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(firstClick) {
					statusText.setText("");
					firstClick = false;
				}
			}});

		imageCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton button, boolean checked) {
				int visible = 0;
				if (checked) {
					visible = View.VISIBLE;
				} else {
					visible = View.GONE;
				}

				imageUriText.setVisibility(visible);
				imageBoxText.setVisibility(visible);
			}});
	}

	protected void updateStatus(String text) {
		Twitter twitter = MyApplication.getTwitter();
		twitter.updateStatus(text, new Twitter.Callback<Status>() {

			@Override
			public void success(Status status) {
				makeToast("The status has been updated.");
				Log.d(StatusActivity.class.getSimpleName(), "status = "+ status);
				updateButton.setEnabled(true /* enabled */);
			}

			@Override
			public void error(NetworkResponse networkResponse) {
				// Create a error text.
				StringBuilder sb = new StringBuilder();
				sb.append("Failed to update a status.");

				// If error exists,
				if (networkResponse != null) {
					sb.append(" error code : ");
					sb.append(networkResponse.statusCode);
				}

				makeToast(sb.toString());
				updateButton.setEnabled(true /* enabled */);
			}
		});
	}

	protected void downloadImageAndUpdateStatusWithMedia(final String text, final String imageUrl) {
		final Twitter twitter = MyApplication.getTwitter();

		// Download an image file from url.
		volleyer().post(imageUrl)
					.withTargetClass(NetworkResponse.class)
					.withListener(new Listener<NetworkResponse>(){

						@Override
						public void onResponse(NetworkResponse networkResponse) {

							File file = FileUtils.createFile(StatusActivity.this.getCacheDir(), networkResponse.data);

							if (file == null) {
								makeToast("Failed to get an image.");
								updateButton.setEnabled(true /* enabled */);
								return;
							}

							updateStatusWithMedia(text, twitter, file);

						}

					})
					.withErrorListener(new ErrorListener(){

						@Override
						public void onErrorResponse(VolleyError error) {
							makeToast("Failed to get an image.");
							updateButton.setEnabled(true /* enabled */);
						}
					})
					.execute();
	}

	protected void updateStatusWithMedia(final String text, final Twitter twitter, File file) {
		twitter.updateStatusWithMedia(text, file, new Twitter.Callback<Status>() {

			@Override
			public void success(Status status) {
				makeToast("The status has been updated.");
				Log.d(StatusActivity.class.getSimpleName(), "status = "+ status);
				updateButton.setEnabled(true /* enabled */);
			}

			@Override
			public void error(NetworkResponse networkResponse) {
				makeToast("Failed to update a status.");
				updateButton.setEnabled(true /* enabled */);
			}
		});
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
	protected void onStop() {
		super.onStop();
	}

	@SuppressLint("ShowToast")
	protected void makeToast(String text) {
		Toast.makeText(getBaseContext(), text, Toast.LENGTH_SHORT).show();	
	}
}
