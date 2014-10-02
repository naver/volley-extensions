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
