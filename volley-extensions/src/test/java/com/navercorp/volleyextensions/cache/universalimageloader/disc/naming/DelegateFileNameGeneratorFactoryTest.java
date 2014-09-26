package com.navercorp.volleyextensions.cache.universalimageloader.disc.naming;

import org.junit.Test;

import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;
import com.nostra13.universalimageloader.core.DefaultConfigurationFactory;

public class DelegateFileNameGeneratorFactoryTest {

	@Test
	public void shouldReturnAUILsDefaultGeneratorWhenCreatingDefaultDelegate() {
		// Given
		FileNameGenerator generator = CustomizedFileNameGeneratorFactory.createDefaultDelegate();
		FileNameGenerator expected = DefaultConfigurationFactory.createFileNameGenerator();
		// Then
		assertThat(generator, instanceOf(expected.getClass()));
	}

	@Test
	public void shouldReturnExpectedFileNameGeneratorWhenCreatingFileNameGenerator() {
		// Given
		String imageUri = "test";
		FileNameGenerator delegate = mock(FileNameGenerator.class);
		// When
		FileNameGenerator generator = CustomizedFileNameGeneratorFactory.createFileNameGenerator();
		FileNameGenerator generatorWithDelegate = CustomizedFileNameGeneratorFactory.createFileNameGenerator(delegate);
		generatorWithDelegate.generate(imageUri);
		// Then
		assertThat(generator, instanceOf(PrefixFileNameGenerator.class));
		assertThat(generatorWithDelegate, instanceOf(PrefixFileNameGenerator.class));
		verify(delegate).generate(imageUri);
	}
}
