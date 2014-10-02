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
package com.navercorp.volleyextensions.volleyer.factory;

import java.io.File;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.NoCache;
import com.navercorp.volleyextensions.volleyer.util.Assert;
/**
 * A factory which creates a {@code RequestQueue} instance without any settings.
 */
public class DefaultRequestQueueFactory {
	/**
	 * Create a {@code RequestQueue} instance.
	 * @param context must not be null.
	 * @return RequestQueue well-made RequestQueue instance
	 */
	public static RequestQueue create(Context context) {
		Assert.notNull(context, "Context");
		Cache diskCache = getDefaultDiskCache(context);
		HttpStack httpStack = HttpStackFactory.createDefaultHttpStack();
		Network network = getDefaultNetwork(httpStack);

		return new RequestQueue(diskCache, network);
	}

	public static Network getDefaultNetwork(HttpStack httpStack) {
		Assert.notNull(httpStack, "HttpStack");
		return new BasicNetwork(httpStack);
	}

	public static Cache getDefaultDiskCache(Context context) {
		File cacheDir = getCacheDir(context);
		if (cacheDir == null) {
			return new NoCache();
		}

		return new DiskBasedCache(cacheDir);
	}

	public static File getCacheDir(Context context) {
		File file = new File(context.getCacheDir().getPath() + "/volleyer-cache");
		return file;
	}
}
