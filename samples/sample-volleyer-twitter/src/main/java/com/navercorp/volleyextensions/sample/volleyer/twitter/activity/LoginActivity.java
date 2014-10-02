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

import com.android.volley.NetworkResponse;

import com.navercorp.volleyextensions.sample.volleyer.twitter.R;
import com.navercorp.volleyextensions.sample.volleyer.twitter.application.MyApplication;
import com.navercorp.volleyextensions.sample.volleyer.twitter.client.Twitter;
import com.navercorp.volleyextensions.sample.volleyer.twitter.client.TwitterLogin;
import com.navercorp.volleyextensions.sample.volleyer.twitter.client.TwitterLogin.AccessTokenCallback;
import com.navercorp.volleyextensions.sample.volleyer.twitter.client.TwitterLogin.Callback;
import com.navercorp.volleyextensions.sample.volleyer.twitter.client.auth.Authorizor;
import com.navercorp.volleyextensions.sample.volleyer.twitter.client.auth.AuthorizorFactory;
import com.navercorp.volleyextensions.sample.volleyer.twitter.client.auth.token.Token;
import com.navercorp.volleyextensions.sample.volleyer.twitter.util.DialogUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private WebView webView;
	private TwitterLogin login;
	private ProgressDialog dialog;
	private boolean firstClick = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initializeButtons();
		initializeDialog();
		initializeWebView();
		initializeTwitterLogin();
	}

	@SuppressLint("ClickableViewAccessibility")
	private void initializeButtons() {
		final EditText pinNumberText = (EditText) findViewById(R.id.pin_number_text);
		final Button accessButton = (Button) findViewById(R.id.access_button);
		pinNumberText.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				if (firstClick) {
					// Clear text
					pinNumberText.setText("");
					firstClick = false;
				}

			}
		});

		accessButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				CharSequence pinNumber = pinNumberText.getText();
				LoginActivity.this.accessTwitter(String.valueOf(pinNumber));
			}});
	}

	private void initializeDialog() {
		dialog = DialogUtils.createDialog(this, "Wait for loading");
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initializeWebView() {
		webView = (WebView) findViewById(R.id.webview);
		webView.setWebViewClient(new WebViewClient(){

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				DialogUtils.hideDialog(dialog);
			}
		});
        WebSettings set = webView.getSettings();
        set.setJavaScriptEnabled(true);
        set.setBuiltInZoomControls(true);
	}


	private void initializeTwitterLogin() {
		Authorizor authorizor = AuthorizorFactory.createDefaultAuthorizor(this);

		if (!authorizor.isInitialized()) {
			makeToast("Failed to get a consumer token, so login process has been canceled.");
			// Do nothing.
			return;
		}

		login = new TwitterLogin(authorizor);
        login.requestToken(new Callback(){

			@Override
			public void success(Token token) {
				webView.loadUrl("https://api.twitter.com/oauth/authorize?oauth_token=" + token.key());
			}

			@Override
			public void error(NetworkResponse networkResponse) {
				makeToast("Failed to get a request token. error code : " + networkResponse.statusCode);
			}});
	}

	protected void accessTwitter(String pinNumber) {
		if(login == null) {
			return;
		}
		login.accessToken(pinNumber, new AccessTokenCallback(){

			@Override
			public void success(Twitter twitter) {
				makeToast("success!");
				MyApplication.setTwitter(twitter);
				LoginActivity.this.finish();
			}

			@Override
			public void error(NetworkResponse networkResponse) {
				makeToast("Failed to get an access token. error code : " + networkResponse.statusCode);
			}});
	}

	@SuppressLint("ShowToast")
	protected void makeToast(String text) {
		Toast.makeText(getBaseContext(), text, Toast.LENGTH_SHORT).show();		
	}

}
