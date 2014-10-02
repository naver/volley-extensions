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
package com.navercorp.volleyextensions.request;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.navercorp.volleyextensions.mock.ResponseHoldListener;
import com.navercorp.volleyextensions.request.AbstractConverterRequest;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class AbstractConverterRequestTest {
	
	String url = "http://test";
	Class<TestClass> clazz = TestClass.class;
	ResponseHoldListener<TestClass> listener = new ResponseHoldListener<TestClass>();

	@Test(expected = NullPointerException.class)
	public void converterShouldThrowNpeWhenClassIsNull() {
		createConcreteClassInstance(url, null, listener);
	}
	
	@Test(expected = NullPointerException.class) 
	public void converterShouldThrowNpeWhenListenerIsNull() {
		createConcreteClassInstance(url, clazz, null);		
	}

	@Test
	public void targetClassShouldBeValid() {
		// When
		ConcreteConverterRequest<TestClass> request= createConcreteClassInstance(url, clazz, listener);
		// Then
		assertThat(request.getTargetClassExternal(), equalTo(TestClass.class));
	}

	@Test
	public void responseShouldBeDelivered() {
		//Given
		ConcreteConverterRequest<TestClass> request = createConcreteClassInstance(url, clazz, listener);
		// When
		request.deliverResponseExternal(new TestClass());
		// Then
		assertNotNull(listener.getLastResponse());
	}

	@Test
	public void bodyStringShouldBeParsed() {
		//Given
		byte[] bytes = new byte[] {'a', 'b' , 'c'};
		String bytesString = new String(bytes);
		NetworkResponse response = new NetworkResponse(bytes);
		ConcreteConverterRequest<TestClass> request = createConcreteClassInstance(url, clazz, listener);
		
		// When
		String bodyString = null;
		try {
			bodyString = request.getBodyStringExternal(response);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// Then
		assertTrue(bytesString.equals(bodyString));
	}
	
	private <T> ConcreteConverterRequest<T> createConcreteClassInstance(String url,
			Class<T> clazz, Listener<T> listener) {
		return new ConcreteConverterRequest<T>(url, clazz, listener);		
	}
	
	/** a concrete class of AbstractConverterRequest. this class is for test. */
	private static class ConcreteConverterRequest<T> extends AbstractConverterRequest<T>{
		public ConcreteConverterRequest(String url, Class<T> clazz,
				Listener<T> listener) {
			super(url, clazz, listener);
		}
		public ConcreteConverterRequest(String url, Class<T> clazz,
				Listener<T> listener, ErrorListener errorListener) {
			super(url, clazz, listener, errorListener);
		}
		
		public ConcreteConverterRequest(int method, String url, Class<T> clazz,
				Listener<T> listener, ErrorListener errorListener) {
			super(method, url, clazz, listener, errorListener);
		}
		
		public Class<T> getTargetClassExternal() {
			return getTargetClass();
		}
		
		public void deliverResponseExternal(T result) {
			deliverResponse(result);
		}
		
		public final String getBodyStringExternal(NetworkResponse response) throws UnsupportedEncodingException {
			return getBodyString(response);
		}
		
		@Override
		protected Response<T> parseNetworkResponse(NetworkResponse response) {
			return null;
		}
		
	}
	
	/** a class for test */
	private static class TestClass {
	}
}
