package com.nhncorp.volleyextensions.cache.disc.impl;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class UniversalTotalSizeLimitedDiscCacheTest {
	@Rule public TemporaryFolder folder = new TemporaryFolder();
	String key = "testKey";
	byte[] value = "testValue".getBytes();
	Entry entry = new Entry();
	File cacheDir;
	
	@BeforeClass
	public static void setUpOnce() throws Exception {
		ShadowLog.stream = System.out;
	}

	@Before
	public void setUp() throws IOException{
    	cacheDir = folder.newFolder();
		entry.data = value;
	}
	
	@Test
    public void cacheShouldBeHit() throws IOException {
    	//Given
		Cache cache = new UniversalTotalSizeLimitedDiscCache(cacheDir, 500);
		//When
		cache.put(key, entry);
		//Then
		Entry hit = cache.get(key);
		assertThat(hit.data, is(value));
		assertThat(cacheDir.list().length, is(1));
    }
	
    @Test
    public void cacheShouldBeHitWithFileLimit() throws IOException, InterruptedException {
    	//Given
    	Cache cache = new UniversalTotalSizeLimitedDiscCache(cacheDir, 56*2);
		//When
		cache.put(key + "1", entry);
		TimeUnit.MICROSECONDS.sleep(10);
		cache.put(key + "2", entry);
		TimeUnit.MICROSECONDS.sleep(10);
		cache.put(key + "3", entry);
		TimeUnit.MICROSECONDS.sleep(10);
		//Then
		assertThat(cacheDir.list().length, is(2));
		assertNotNull(cache.get(key + "3"));
		assertNotNull(cache.get(key + "2"));
		assertNull(cache.get(key + "1"));
    }
    
	
    @Test
    public void cacheShouldBeHitWithMd5FileName() throws IOException {
    	//Given
    	Md5FileNameGenerator nameGenerator = new Md5FileNameGenerator();
    	Cache cache = new UniversalTotalSizeLimitedDiscCache(cacheDir, nameGenerator, 500);
		//When
		cache.put(key, entry);
		//Then
		Entry hit = cache.get(key);
		assertThat(hit.data, is(value));
		assertThat(cacheDir.list().length, is(1));
		assertThat(cacheDir.list()[0], is("266ups70gzf5c2qtog8x4rzv4"));
    }
}
