package com.nhncorp.volleyextensions.cache.disc.impl;

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
    	//Given
		Cache cache = new UniversalLimitedAgeDiscCache(cacheDir, 1);
		//When
		cache.put(key, entry);
		//Then
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
    	//Given
    	Md5FileNameGenerator nameGenerator = new Md5FileNameGenerator();
    	Cache cache = new UniversalLimitedAgeDiscCache(cacheDir, nameGenerator, 2);
		//When
		cache.put(key, entry);
		//Then
		assertThat(cacheDir.list()[0], is("266ups70gzf5c2qtog8x4rzv4"));
    }
}
