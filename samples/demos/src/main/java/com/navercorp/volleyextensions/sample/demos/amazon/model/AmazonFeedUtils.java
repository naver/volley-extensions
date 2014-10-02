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
package com.navercorp.volleyextensions.sample.demos.amazon.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class AmazonFeedUtils {
	private static final String IMG_URL_EXTRACTION_PATTERN_REGEX = "(<img src=\")(http[s]*://[A-Za-z0-9\\./=%_-]+)";
	private static final Pattern IMG_URL_EXTRACTION_PATTERN = Pattern.compile(IMG_URL_EXTRACTION_PATTERN_REGEX);
	private static final int IMG_URL_POSITION_IN_REGEX = 2;
	private static final String EMPTY_STRING = "";

	public static String extractImageFrom(String content) {
		if ( content == null ) {
			return EMPTY_STRING;
		}

		Matcher matcher = IMG_URL_EXTRACTION_PATTERN.matcher(content);

		if (containUrlFrom(matcher)) {
		    return getUrlFrom(matcher);
		}

		return EMPTY_STRING;
	}

	private static boolean containUrlFrom(Matcher matcher) {
		return matcher.find() && matcher.groupCount() >= IMG_URL_POSITION_IN_REGEX;
	}

	private static String getUrlFrom(Matcher matcher) {
		return matcher.group(IMG_URL_POSITION_IN_REGEX);
	}	
}
