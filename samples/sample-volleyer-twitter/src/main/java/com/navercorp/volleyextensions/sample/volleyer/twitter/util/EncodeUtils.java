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
