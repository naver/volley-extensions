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
package com.navercorp.volleyextensions.volleyer.mock;

import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;

public class ErrorResponseHoldListener implements ErrorListener {
	private VolleyError error;

	@Override
	public void onErrorResponse(VolleyError error) {
		this.error = error;
	}

	public VolleyError getLastError() {
		return this.error;
	}
}
