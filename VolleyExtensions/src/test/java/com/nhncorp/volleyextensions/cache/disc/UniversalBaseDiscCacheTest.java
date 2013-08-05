package com.nhncorp.volleyextensions.cache.disc;

import static org.mockito.BDDMockito.*;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

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
