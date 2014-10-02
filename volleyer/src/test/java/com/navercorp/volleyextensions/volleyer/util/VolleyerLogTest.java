package com.navercorp.volleyextensions.volleyer.util;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class VolleyerLogTest {

	@Before
	public void setUp(){
		ShadowLog.stream = System.out;
	}

	@Test
	public void volleyerLogShouldPrintCorrectLogs() {
		// When
		VolleyerLog.info("*******************************************************");
		VolleyerLog.info("Don't be suprised if exceptions are printed on console!");
		VolleyerLog.info("*******************************************************");
		VolleyerLog.debug("DEBUG");
		VolleyerLog.debug(new Exception("Debug Test"), "DEBUG");
		VolleyerLog.warn("WARN");
		VolleyerLog.warn(new Exception("Warn Test"), "WARN");
		VolleyerLog.error("ERROR");
		VolleyerLog.error(new Exception("Error Test"), "ERROR");
		VolleyerLog.info("**************************");
		VolleyerLog.info("The end of VolleyerLogTest");
		VolleyerLog.info("**************************");
	}

}
