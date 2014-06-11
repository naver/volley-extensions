package com.navercorp.volleyextensions.volleyer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import android.content.Context;

import com.android.volley.RequestQueue;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class VolleyerTest {
	RequestQueue requestQueue;
	Context context;
	private VolleyerContext volleyerContext;

	@Before
	public void setUp() {
		requestQueue = mock(RequestQueue.class);
		context = Robolectric.application.getApplicationContext();
		volleyerContext = mock(VolleyerContext.class);
	}
}
