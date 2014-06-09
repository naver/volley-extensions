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
		VolleyerLog.debug("DEBUG", new Exception("Debug Test"));
		VolleyerLog.warn("WARN");
		VolleyerLog.warn("WARN", new Exception("Warn Test"));
		VolleyerLog.info("INFO");
		VolleyerLog.info("INFO", new Exception("Info Test"));
		VolleyerLog.error("ERROR");
		VolleyerLog.error("ERROR", new Exception("Error Test"));
		VolleyerLog.verbose("VERBOSE");
		VolleyerLog.verbose("VERBOSE", new Exception("Verbose Test"));
		VolleyerLog.info("**************************");
		VolleyerLog.info("The end of VolleyerLogTest");
		VolleyerLog.info("**************************");
	}

}
