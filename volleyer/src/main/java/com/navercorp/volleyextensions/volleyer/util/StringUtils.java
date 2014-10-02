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
package com.navercorp.volleyextensions.volleyer.util;

import java.security.SecureRandom;
import java.util.Random;

public class StringUtils {

	private static char[] chars;

	static {
		initDefaultChars();
	}
	/**
	 * Prepare a-z characters for {@link #generateRandom(int)}.
	 */
	private static void initDefaultChars() {
		String charsStr = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		int length = charsStr.length();
		chars = new char[length];
		charsStr.getChars(0, length, chars, 0);
	}
	/**
	 * Generate string which contains random characters.
	 * @param length must be a positive number or 0
	 * @return Generated random string
	 */
	public static String generateRandom(int length) {
		String generatedStr = null;
		// Return null if length is invalid
		if (length < 0) {
			return generatedStr;
		}

		// Return empty string if length is zero
		if (length == 0) {
			return "";
		}

		char[] generatedChar = new char[length];
		int charsLength = chars.length;

		Random random = new SecureRandom();
		for(int i = 0; i < length; ++i) {
			int index = random.nextInt(charsLength);
			generatedChar[i] = (char) chars[index];
		}

		generatedStr = String.valueOf(generatedChar);
		return generatedStr;
	}
	/**
	 * Check whether string is empty.
	 * @return true if {@code str} is null or empty
	 */
	public static boolean isEmpty(String str) {
		return (str == null) || ("".equals(str));
	}
}
