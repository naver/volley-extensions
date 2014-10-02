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

import com.navercorp.volleyextensions.volleyer.builder.DeleteBuilder;
import com.navercorp.volleyextensions.volleyer.builder.GetBuilder;
import com.navercorp.volleyextensions.volleyer.builder.PostBuilder;
import com.navercorp.volleyextensions.volleyer.builder.PutBuilder;
/**
 * <pre>
 * Sub Volleyer class which throws exceptions when APIs are called.
 * This class is made for which prevents users from calling 'volleyer()' method without setting the default volleyer.
 * </pre>
 * @author Wonjun Kim
 *
 */
class DummyVolleyer extends Volleyer {

	DummyVolleyer() {
		super(null /* RequestQueue */);
	}

	private void throwError() {
		throw new UnsupportedOperationException();
	}

	@Override
	public GetBuilder get(String url) {
		throwError();
		return null;
	}

	@Override
	public PostBuilder post(String url) {
		throwError();
		return null;
	}

	@Override
	public PutBuilder put(String url) {
		throwError();
		return null;
	}

	@Override
	public DeleteBuilder delete(String url) {
		throwError();
		return null;
	}

	@Override
	public Settings settings() {
		throwError();
		return null;
	}

}
