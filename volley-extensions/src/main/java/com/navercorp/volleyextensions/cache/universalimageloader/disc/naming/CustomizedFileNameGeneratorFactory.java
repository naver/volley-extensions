package com.navercorp.volleyextensions.cache.universalimageloader.disc.naming;

import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;
import com.nostra13.universalimageloader.core.DefaultConfigurationFactory;
/**
 * <pre>
 * A factory that creates a customized, and protective {@code FileNameGenerator}.
 * This generator class is made for special purpose, and is used only for disc caches of this library.
 * You see the comment in {@link PrefixNameGenerator} for details.
 * </pre>
 * 
 * @author Wonjun Kim
 *
 */
public class CustomizedFileNameGeneratorFactory {

	/**
	 * Create a customized FileNameGenerator without any option.
	 */
	public static FileNameGenerator createFileNameGenerator() {
		FileNameGenerator delegate = createDefaultDelegate();
		return new PrefixFileNameGenerator(delegate);
	}

	/**
	 * Create a default generator for delegate.
	 * @return AUIL's default generator.
	 */
	static FileNameGenerator createDefaultDelegate() {
		FileNameGenerator delegate = DefaultConfigurationFactory.createFileNameGenerator();
		return delegate;
	}

	/**
	 * Create a customized FileNameGenerator with specific delegate.
	 * @param delegate {@code FileNameGenerator} that was delivered from constructor of a disc cache.
	 */
	public static FileNameGenerator createFileNameGenerator(FileNameGenerator delegate) {
		return new PrefixFileNameGenerator(delegate);
	}
}
