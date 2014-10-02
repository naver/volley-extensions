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
