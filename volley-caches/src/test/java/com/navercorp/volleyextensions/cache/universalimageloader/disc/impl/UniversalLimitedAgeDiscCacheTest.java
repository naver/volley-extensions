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
package com.navercorp.volleyextensions.cache.universalimageloader.disc.impl;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.navercorp.volleyextensions.cache.universalimageloader.disc.impl.UniversalLimitedAgeDiscCache;
import com.navercorp.volleyextensions.cache.universalimageloader.disc.naming.CustomizedFileNameGeneratorFactory;
import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;

public class UniversalLimitedAgeDiscCacheTest {
	@Rule public TemporaryFolder folder = new TemporaryFolder();
	String key = "testKey";
	byte[] value = "testValue".getBytes();
	Entry entry = new Entry();
	File cacheDir;
	
	@Before
	public void setUp() throws IOException{
    	cacheDir = folder.newFolder();
		entry.data = value;
	}
	
	@Test
    public void cacheShouldBeHitAndExpriedAged() throws IOException, InterruptedException {
    	// Given
		Cache cache = new UniversalLimitedAgeDiscCache(cacheDir, 1);
		// When
		cache.put(key, entry);
		// Then
		Entry hit = cache.get(key);
		assertThat(hit.data, is(value));
		assertThat(cacheDir.list().length, is(1));
		
		TimeUnit.SECONDS.sleep(2);
		hit = cache.get(key);
		assertNull(hit);
		assertThat(cacheDir.list().length, is(0));
    }
	
    @Test
    public void cacheShouldCreateMd5FileName() throws IOException {
    	// Given
    	Md5FileNameGenerator nameGenerator = new Md5FileNameGenerator();
    	Cache cache = new UniversalLimitedAgeDiscCache(cacheDir, nameGenerator, 2);
    	FileNameGenerator wrappedGenerator = CustomizedFileNameGeneratorFactory.createFileNameGenerator(nameGenerator);
    	String expected = wrappedGenerator.generate(key);
		// When
		cache.put(key, entry);
		// Then
		assertThat(cacheDir.list()[0], is(expected));
    }
}
