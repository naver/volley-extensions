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
package com.navercorp.volleyextensions.sample.volleyer.twitter.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.res.AssetManager;

import com.navercorp.volleyextensions.volleyer.util.Assert;
import com.navercorp.volleyextensions.volleyer.util.IoUtils;
import com.navercorp.volleyextensions.volleyer.util.StringUtils;

public class FileUtils {

	private static final int DEFAULT_LENGTH = 10;

	public static File createFile(File directory, byte[] data) {
		String filename = "file-" + StringUtils.generateRandom(DEFAULT_LENGTH) + ".file";
		return createFile(directory, filename, data);
	}

	public static File createFile(File directory, String filename, byte[] data) {
		Assert.notNull(directory, "Directory");
		Assert.notNull(filename, "filename");
		Assert.notNull(data, "data");

		File file = new File(directory.getAbsolutePath() + "/" + filename);
		OutputStream os = null;
		try {
			os = new BufferedOutputStream(new FileOutputStream(file));
			os.write(data);
			os.flush();
		} catch (IOException e) {
			file = null;
		} finally {
			IoUtils.closeQuietly(os);
		}
		return file;
	}

	public static byte[] getBytesFrom(Context context, String fileName) {
		AssetManager manager = context.getAssets();
		InputStream is = null;
		byte[] bytes = null;
		try {
			is = new BufferedInputStream(manager.open(fileName));
			bytes = readStream(is);

		} catch (IOException e) {
		} finally {
			IoUtils.closeQuietly(is);
		}

		return bytes;
	}

	private static byte[] readStream(InputStream is) throws IOException {
		// Use ByteArrayOutputStream, this does not need to close.
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		int c;
		while ((c = is.read()) != -1) {
			baos.write(c);
		}

		byte[] bytes = baos.toByteArray();
		return bytes;
	}

}
