package com.navercorp.volleyextensions.volleyer.util;

public class ClassUtils {
	/**
	 * <pre>
	 * Check whether a class is present.
	 * 
	 * NOTE : This idea is from spring framework's ClassUtils#isPresent(String).
	 * This isPresent method is very simple. If you want to see advanced isPresent implementation, you can check the spring's code in below link,
	 * //github.com/spring-projects/spring-android/blob/master/spring-android-core/src/main/java/org/springframework/util/ClassUtils.java
	 * 
	 * </pre>
	 * @param clazzName full class path of a class that you find
	 * @return true if the class exists
	 */
	public static boolean isPresent(String clazzName) {
		if (StringUtils.isEmpty(clazzName)) {
			return false;
		}

		boolean isPresent;

		try {
			@SuppressWarnings("unused")
			Class<?> clazz = Class.forName(clazzName);
			isPresent = true;
		} catch (ClassNotFoundException e) {
			isPresent = false;
		}
		return isPresent;
	}
}
