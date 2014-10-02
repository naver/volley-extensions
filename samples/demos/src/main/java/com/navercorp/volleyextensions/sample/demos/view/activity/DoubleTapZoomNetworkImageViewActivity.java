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
package com.navercorp.volleyextensions.sample.demos.view.activity;

import com.android.volley.toolbox.ImageLoader;
import com.navercorp.volleyextensions.sample.demos.R;
import com.navercorp.volleyextensions.sample.demos.application.volley.MyVolley;
import com.navercorp.volleyextensions.view.ZoomableNetworkImageView;
import com.navercorp.volleyextensions.view.ZoomableNetworkImageView.OnImageChangedListener;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;

public class DoubleTapZoomNetworkImageViewActivity extends Activity {
	private static final String SAMPLE_IMAGE_URL = "http://www.phototravelpages.com/wallpapers/sunny-gardens-1024.jpg";
	private ZoomableNetworkImageView zoomableImageView;
	private ImageLoader imageLoader;
	private ProgressBar circleProgressBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_double_tap_zoom_network_image_view);
		imageLoader = MyVolley.getImageLoader();
		initializeZoomableImageView();
	}

	private void initializeZoomableImageView() {
		zoomableImageView = (ZoomableNetworkImageView) findViewById(R.id.zoom_imageview);
		circleProgressBar = (ProgressBar) findViewById(R.id.circleProgressBar);
		OnImageChangedListener listener = new OnImageChangedListener(){

			@Override
			public void onImageChanged(ZoomableNetworkImageView zoomableImageView) {
				if ( zoomableImageView.getDrawable() == null) {
					return;
				}
				circleProgressBar.setVisibility(View.GONE);
				zoomableImageView.removeOnImageChangedListener();
			}};
		zoomableImageView.setOnImageChangedListener(listener);
		zoomableImageView.setImageUrl(SAMPLE_IMAGE_URL, imageLoader);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}
}
