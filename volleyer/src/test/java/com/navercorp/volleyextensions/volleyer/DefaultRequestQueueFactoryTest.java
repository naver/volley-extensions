package com.navercorp.volleyextensions.volleyer;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpStack;

import android.content.Context;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class DefaultRequestQueueFactoryTest {
	Context context;

	@Before
	public void setUp() {
		context = Robolectric.application.getApplicationContext();
	}

	@Test
	public void factoryShouldCreateRequestQueue() {
		// When
		RequestQueue requestQueue = DefaultRequestQueueFactory.create(context);
		// Then
		assertNotNull(requestQueue);
	}
	@Test
	public void factoryShouldReturnCacheDir() {
		// When
		File dir = DefaultRequestQueueFactory.getCacheDir(context);
		// Then
		assertNotNull(dir);
	}

	@Test
	public void factoryShouldReturnHttpStack() {
		// When
		HttpStack httpStack = DefaultRequestQueueFactory.getDefaultStack();
		// Then
		assertNotNull(httpStack);
	}

	@Test
	public void factoryShouldReturnBasicNetwork() {
		// Given
		HttpStack httpStack = DefaultRequestQueueFactory.getDefaultStack();
		// When
		Network network = DefaultRequestQueueFactory.getDefaultNetwork(httpStack);
		// Then
		assertEquals(network.getClass(), BasicNetwork.class);
	}

	@Test
	public void factoryShouldReturnDiskBasedCache() {
		// When
		Cache diskCache = DefaultRequestQueueFactory.getDefaultDiskCache(context);
		// Then
		assertEquals(diskCache.getClass(), DiskBasedCache.class);
	}
}
