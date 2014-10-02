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
