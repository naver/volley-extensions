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
package com.navercorp.volleyextensions.view;

import static com.jayway.awaitility.Awaitility.*;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;

import javax.xml.bind.DatatypeConverter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.NoCache;
import com.github.kristofa.test.http.MockHttpServer;
import com.github.kristofa.test.http.SimpleHttpResponseProvider;
import com.jayway.awaitility.Duration;
import com.navercorp.volleyextensions.mock.MockExecutorDelivery;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Bitmap.CompressFormat;
import android.view.ViewGroup.LayoutParams;

/**
 * <pre>
 * NOTE : This class is made for researching to create tests for zoomable image views.
 * There are no test cases yet.
 * </pre>
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class TwoLevelDoubleTapZoomNetworkImageViewIntegrationTest {
	static final int THREAD_POOL_SIZE = 4;
	static final int PORT = 51234;
	static final String url = "http://localhost:" + PORT;
	
	private Activity activity;
	private RequestQueue requestQueue;
	private ImageLoader imageLoader;
	private SimpleHttpResponseProvider responseProvider;
	private MockHttpServer server;
	
	@Before
	public void setup()  {
	    activity = Robolectric.buildActivity(Activity.class).create().get();
	}
	
	@Before
	public void setUp() throws Exception {
		// init a Volley RequestQueue
		requestQueue = new RequestQueue(new NoCache(), new BasicNetwork(new HurlStack()), 
						THREAD_POOL_SIZE, new MockExecutorDelivery());
		requestQueue.start();
		imageLoader = new ImageLoader(requestQueue, new ImageCache(){

			@Override
			public Bitmap getBitmap(String url) {
				return null;
			}

			@Override
			public void putBitmap(String url, Bitmap bitmap) {
			}});
		// init mock http server
		responseProvider = new SimpleHttpResponseProvider();
		server = new MockHttpServer(PORT, responseProvider);
		server.start();
	}

	@After
	public void tearDown() throws Exception {
		// stop the mock http server of running
		server.stop();
		// stop the volley
		requestQueue.stop();
	}
	/**
	 * NOTE : Do not any test yet.
	 */
	@Test
	public void viewMustbezoomedWhenDoubleTapping() {
		// Given
		int layoutWidth = 300;
		int layoutHeight = 300;
		LayoutParams params = new LayoutParams(layoutWidth, layoutHeight);
		TwoLevelDoubleTapZoomNetworkImageView view = new TwoLevelDoubleTapZoomNetworkImageView(Robolectric.application);
		String jpeg = DatatypeConverter.printBase64Binary(createTestJpeg());
		responseProvider.expect(com.github.kristofa.test.http.Method.GET, "/")
						.respondWith(200, "image/jpeg", jpeg);
		// When
		view.setImageUrl(url , imageLoader);
		activity.addContentView(view, params);
		with().atMost(Duration.TEN_SECONDS);
		// Then
		assertTrue(true);
		
	}

	private static byte[] createTestJpeg() {
		Bitmap bitmap = createTestBitmap();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, 100, baos);
		return baos.toByteArray();
	}
	
	private static Bitmap createTestBitmap() {
		int[] colors = {Color.BLACK};
		int width = 300;
		int height = 300;
		android.graphics.Bitmap.Config config = android.graphics.Bitmap.Config.ARGB_8888;
		Bitmap bitmap = Bitmap.createBitmap(colors , width, height, config );
		return bitmap;
	}
	
}
