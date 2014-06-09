package com.navercorp.volleyextensions.volleyer.util;

import android.util.Log;

public class VolleyerLog {
	public static final String TAG = "Volleyer";
	
	public static void info(String message) {
		log(Log.INFO, message);
	}

	public static void info(String message, Throwable throwable) {
		int level = Log.INFO;
		if (Log.isLoggable(TAG, level)) {
			Log.i(TAG, message, throwable);
		}
	}

	public static void debug(String message) {
		log(Log.DEBUG, message);
	}

	public static void debug(String message, Throwable throwable) {
		int level = Log.DEBUG;
		if (Log.isLoggable(TAG, level)) {
			Log.d(TAG, message, throwable);
		}
	}

	public static void warn(String message) {
		log(Log.WARN, message);
	}

	public static void warn(String message, Throwable throwable) {
		int level = Log.WARN;
		if (Log.isLoggable(TAG, level)) {
			Log.w(TAG, message, throwable);
		}
	}

	public static void verbose(String message) {
		log(Log.VERBOSE, message);
	}

	public static void verbose(String message, Throwable throwable) {
		int level = Log.VERBOSE;
		if (Log.isLoggable(TAG, level)) {
			Log.v(TAG, message, throwable);
		}
	}

	public static void error(String message) {
		log(Log.ERROR, message);
	}

	public static void error(String message, Throwable throwable) {
		int level = Log.ERROR;
		if (Log.isLoggable(TAG, level)) {
			Log.e(TAG, message, throwable);
		}
	}

	/**
	 * Log if {@code TAG} is on {@code level}.
	 * @param level
	 * @param message
	 */
	public static void log(int level, String message) {
		if (Log.isLoggable(TAG, level)) {
			Log.println(level, TAG, message);
		}
	}
}
