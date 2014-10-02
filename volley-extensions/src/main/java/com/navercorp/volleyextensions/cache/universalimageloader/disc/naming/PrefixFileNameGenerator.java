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
package com.navercorp.volleyextensions.cache.universalimageloader.disc.naming;

import com.navercorp.volleyextensions.util.Assert;
import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;
/**
 * <pre>
 * A wrapper class for {@link FileNameGenerator}.
 * This class appends a prefix to a filename that {@code delegate} produced.
 * This is for avoiding duplicate filenames between Volley and AUIL. 
 * Duplicate problem can be occured when Volley and AUIL are using some disc cache and share the same cache directory each other. 
 * (This situation is not a commonplace affair, but the possibility of occuring it is not an impossible.)
 * All of the disc caches on this library starts to use this class internally to prevent this problem from now.
 * </pre>
 * @author Wonjun Kim
 *
 */
class PrefixFileNameGenerator implements FileNameGenerator {

	private static final String DEFAULT_PREFIX = "vlly-";

	private final FileNameGenerator delegate;
	private String prefix;
	/**
	 * Constructor with default prefix
	 * @param delegate Generator that generates a file name first and give it to this class.
	 */
	public PrefixFileNameGenerator(FileNameGenerator delegate) {
		this(delegate, DEFAULT_PREFIX);
	}
	/**
	 * Constructor with custom prefix
	 * @param delegate Generator that generates a file name first and give it to this class.
	 * @param prefix custom prefix string
	 */
	public PrefixFileNameGenerator(FileNameGenerator delegate, String prefix) {
		Assert.notNull(delegate, "Delegated FileNameGenerator");
		Assert.notNull(prefix, "Prefix");

		this.delegate = delegate;
		this.prefix = prefix;
	}

	/**
	 * Append a prefix to a filename that {@code delegate} produced
	 * @return prefix-appended filename. 
	 */
	@Override
	public String generate(String imageUri) {
		if (imageUri == null) {
			return null;
		}
		return addPrefix(imageUri);
	}

	private String addPrefix(String imageUri) {
		String filename = delegate.generate(imageUri);
		return prefix + filename;
	}

}
