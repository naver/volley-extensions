package com.navercorp.volleyextensions.volleyer.util;

import java.security.SecureRandom;
import java.util.Random;

public class StringUtils {

	private static char[] chars;

	static {
		initDefaultChars();
	}

	private static void initDefaultChars() {
		String charsStr = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		int length = charsStr.length();
		chars = new char[length];
		charsStr.getChars(0, length, chars, 0);
	}

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

	public static boolean isEmpty(String str) {
		return (str == null) || ("".equals(str));
	}
}
