package com.navercorp.volleyextensions.volleyer.util;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class ClassUtilsTest {

	@Test
	public void shouldReturnFalseWhenClassNameNull() {
		// When
		boolean isPresent = ClassUtils.isPresent(null /* className */);
		// Then
		assertThat(isPresent, is(false));
	}

	@Test
	public void shouldReturnFalseWhenClassNameEmpty() {
		// When
		boolean isPresent = ClassUtils.isPresent("" /* className */);
		// Then
		assertThat(isPresent, is(false));
	}

	@Test
	public void shouldReturnTrueWhenClassPresent() {
		// Given
		String className = "java.io.IOException";
		// When
		boolean isPresent = ClassUtils.isPresent(className);
		// Then
		assertThat(isPresent, is(true));
	}

	@Test
	public void shouldReturnFalseWhenClassNotPresent() {
		// Given
		String className = "aoamwogmAWEGAWEg.asdmgm";
		// When
		boolean isPresent = ClassUtils.isPresent(className);
		// Then
		assertThat(isPresent, is(false));
	}

}
