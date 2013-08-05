package com.nhncorp.volleyextensions.util;

public class Assert {
	private static final String MESSAGE_FORMAT = "\"%s\" argument must be not null.";

	public static void notNull(Object object, String argName) {
		if (object == null) {
			// When a parameter is null,
			// it throws a NullPointerException instead of an IllegalArgumentException.
			// This rule is recommended by the item 62 on Effective Java 2nd edition as follows.
			// "If a caller passes null in some parameter for which null values are prohibited, 
			// convention dictates that NullPointerException be thrown rather than IllegalArgumentException."
			throw new NullPointerException(String.format(MESSAGE_FORMAT, argName));
		}
	}
}
