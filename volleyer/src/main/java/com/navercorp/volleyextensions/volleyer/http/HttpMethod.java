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
package com.navercorp.volleyextensions.volleyer.http;

import com.android.volley.Request;
/**
 * A wrapper for {@link com.android.volley.Request.Method}
 * @author Wonjun Kim
 *
 */
public enum HttpMethod {
	GET(Request.Method.GET), POST(Request.Method.POST), PUT(Request.Method.PUT), DELETE(Request.Method.DELETE);
	private int methodCode;

	HttpMethod(int methodCode) {
		this.methodCode = methodCode;
	}

	public int getMethodCode() {
		return methodCode;
	}
}
