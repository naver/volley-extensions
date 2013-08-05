package com.example.testvolleyextensions;

import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.nhncorp.volleyextensions.request.Jackson2Request;
import com.nhncorp.volleyextensions.request.SimpleXmlRequest;
import com.android.volley.Response.ErrorListener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
//import com.fasterxml.jackson.core.JsonParser;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

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
