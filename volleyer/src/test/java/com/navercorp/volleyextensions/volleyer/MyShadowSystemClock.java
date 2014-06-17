package com.navercorp.volleyextensions.volleyer;

import org.robolectric.annotation.Implements;

import android.os.SystemClock;
/**
 * <pre>
 * Shadow Class for preventing NPE when using robolectric.
 * 
 * <b>NOTE</b> : This class is extracted from below link,
 * http://stackoverflow.com/questions/20745301/shadowsystemclock-nullpointerexception
 * </pre>
 */
@Implements(value = SystemClock.class, callThroughByDefault = true)
public class MyShadowSystemClock {
	public static long elapsedRealtime() {
		return 0;
	}
}
