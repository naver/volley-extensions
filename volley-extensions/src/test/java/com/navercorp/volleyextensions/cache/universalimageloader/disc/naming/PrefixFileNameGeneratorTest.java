package com.navercorp.volleyextensions.cache.universalimageloader.disc.naming;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;
import com.nostra13.universalimageloader.core.DefaultConfigurationFactory;

public class PrefixFileNameGeneratorTest {
	private static final String TEST_PREFIX = "T.EST";

	private FileNameGenerator delegate;

	@Before
	public void setUp() {
		delegate = DefaultConfigurationFactory.createFileNameGenerator();
	}

	@Test(expected=NullPointerException.class)
	public void shouldThrowNpeWhenDelegateNull() {
		// Given
		new PrefixFileNameGenerator(null /* delegate */);
	}

	@Test(expected=NullPointerException.class)
	public void shouldThrowNpeWhenDelegateNullWithPrefix() {
		// Given
		new PrefixFileNameGenerator(null /* delegate */, TEST_PREFIX);
	}

	@Test(expected=NullPointerException.class)
	public void shouldThrowNpeWhenPrefixNull() {
		// Given
		new PrefixFileNameGenerator(delegate, null /* prefix */);
	}

	@Test
	public void shouldReturnExpectedFileNameWhenGenerating() {
		// Given
		FileNameGenerator wrapper = new PrefixFileNameGenerator(delegate, TEST_PREFIX);
		String imageUri = "https://testtest/WNEOWmasdgmao/msmld.zz?kaksk=eaewggg&asdg%20hk";
		// When
		String filename = delegate.generate(imageUri);
		String wrapperFilename = wrapper.generate(imageUri);
		// Then
		String expected = TEST_PREFIX + filename;
		assertThat(wrapperFilename, is(expected));
	}

	@Test
	public void shouldReturnNullWhenImageUriNull() {
		// Given
		FileNameGenerator wrapper = new PrefixFileNameGenerator(delegate, TEST_PREFIX);
		// When
		String wrapperFilename = wrapper.generate(null);
		// Then
		assertThat(wrapperFilename, is(nullValue()));
	}

}
