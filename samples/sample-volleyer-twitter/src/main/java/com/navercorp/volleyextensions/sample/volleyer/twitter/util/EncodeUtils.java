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
package com.navercorp.volleyextensions.sample.volleyer.twitter.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.navercorp.volleyextensions.volleyer.util.StringUtils;

public class EncodeUtils {
	public static String encodeUtf8(String originalStr) {
		try {
			return URLEncoder.encode(originalStr, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// unreachable
			throw new IllegalStateException(e);
		}
	}

	public static String encodeParameter(String originalStr) {
		if (StringUtils.isEmpty(originalStr)) {
			return originalStr;
		}

		String str = encodeUtf8(originalStr);
		return str.replace("+", "%20")
				.replace("*", "%2A")
				.replace("%7E", "~");
	}

}
