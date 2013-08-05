package com.nhncorp.volleyextensions.util;

import java.io.Closeable;

public class IoUtils {
	public static void closeQuietly(Closeable resource) {
		if (resource == null) {
			return;
		}
		
		try {
			resource.close();
		} catch (Exception e) {
			// ignore
		}
	}
}
