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
package com.navercorp.volleyextensions.cache.universalimageloader.disc;

import static org.mockito.BDDMockito.*;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.navercorp.volleyextensions.cache.universalimageloader.disc.UniversalBaseDiscCache;
import com.nostra13.universalimageloader.cache.disc.DiscCacheAware;
@RunWith(MockitoJUnitRunner.class)
public class UniversalBaseDiscCacheTest {
	@Mock DiscCacheAware delegate;

	@Test(expected = NullPointerException.class)
	public void constructorShouldThrowNpeWhenCacheDirIsNull() {
		new UniversalBaseDiscCache(null, delegate){}; 
	}
	
	@Test(expected = NullPointerException.class)
	public void constructorShouldThrowNpeWhenDelegateIsNull() {
		new UniversalBaseDiscCache(new File(""), null){}; 
	}
	
	@Test
	public void mkdirShoudNotBeCalledWhenDirectoryExists() {
		// Given
		File cacheDir = mock(File.class); 
		given(cacheDir.exists()).willReturn(true);
		UniversalBaseDiscCache discCache = new UniversalBaseDiscCache(cacheDir, delegate){}; 
		// When
		discCache.initialize();
		// Then
		verify(cacheDir, never()).mkdir();
	}
	
	@Test
	public void mkdirShoudBeCalledWhenDirectoryNotExists() {
		// Given
		File cacheDir = mock(File.class); 
		given(cacheDir.exists()).willReturn(false);
		given(cacheDir.mkdir()).willReturn(true);
		UniversalBaseDiscCache discCache = new UniversalBaseDiscCache(cacheDir, delegate){}; 
		// When
		discCache.initialize();
		// Then
		verify(cacheDir).mkdir();
	}
	
}
