package com.example.testvolleyextensions;

import java.io.File;

import android.app.Application;

public class MyApplication extends Application {

	private static final int DEFAULT_FILE_COUNT = 3;

	@Override
	public void onCreate() {
		super.onCreate();
		MyVolley.init(this);
	}

}
