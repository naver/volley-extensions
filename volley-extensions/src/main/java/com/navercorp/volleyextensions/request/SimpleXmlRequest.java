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

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import org.simpleframework.xml.core.ElementException;
import org.simpleframework.xml.core.Persister;
import org.xmlpull.v1.XmlPullParserException;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.navercorp.volleyextensions.util.Assert;
import com.navercorp.volleyextensions.util.IoUtils;
/**
 * A Request{@literal <T>} for which make a request and convert response data by Simple XML.
 * @param <T> Specific type of an converted object from response data
 * 
 * @see AbstractConverterRequest
 * @see Request
 */
public class SimpleXmlRequest<T> extends AbstractConverterRequest<T> {
	/** Default {@link Persister} is singleton */
	private static class PersisterHolder {
		private static final Persister persister = new Persister();

		private static Persister getPersister() {
			return persister;
		}
	}
	/** {@code persister} is immutable(but not severely). */
	private Persister persister;
	
	/**
	 * Make a GET request 
	 * @param url URL of the request to make
	 * @param clazz Specific type object of an converted object from response data
	 * @param listener listener for response
	 */
	public SimpleXmlRequest(String url, Class<T> clazz, Listener<T> listener) {
		this(url, clazz, PersisterHolder.getPersister(), listener);
	}
	/**
	 * Make a GET request with custom {@code persister}
	 * @param url URL of the request to make
	 * @param clazz Specific type object of an converted object from response data
	 * @param persister {@link Persister} to convert
	 * @param listener listener for response
	 */
	public SimpleXmlRequest(String url, Class<T> clazz, Persister persister, Listener<T> listener) {
		super(url, clazz, listener);
		setPersister(persister);
	}	
	/**
	 * Make a GET request with {@code errorListener}
	 * @param url URL of the request to make
	 * @param clazz Specific type object of an converted object from response data
	 * @param listener listener for response
	 * @param errorListener listener for errors
	 */
	public SimpleXmlRequest(String url, Class<T> clazz, Listener<T> listener,
			ErrorListener errorListener) {
		this(url, clazz, PersisterHolder.getPersister(), listener, errorListener);
	}
	/**
	 * Make a GET request with custom {@code persister} and {@code errorListener}
	 * @param url URL of the request to make
	 * @param clazz Specific type object of an converted object from response data
	 * @param listener listener for response
	 * @param persister {@link Persister} to convert
	 * @param errorListener listener for errors
	 */
	public SimpleXmlRequest(String url, Class<T> clazz, Persister persister, Listener<T> listener, 
			ErrorListener errorListener) {
		super(url, clazz, listener, errorListener);
		setPersister(persister);
	}
	/**
	 * Make a request with {@code errorListener}
	 * @param method HTTP method. See here : {@link com.android.volley.Request.Method} 
	 * @param url URL of the request to make
	 * @param clazz
	 * @param listener listener for response
	 * @param errorListener listener for errors
	 */
	public SimpleXmlRequest(int method, String url, Class<T> clazz,
			Listener<T> listener, ErrorListener errorListener) {
		this(method, url, clazz, PersisterHolder.getPersister(), listener, errorListener);
	}
	/**
	 * Make a request with custom {@code persister} and {@code errorListener}
	 * @param method HTTP method. See here : {@link com.android.volley.Request.Method} 
	 * @param url URL of the request to make
	 * @param clazz Specific type object of an converted object from response data
	 * @param persister {@link Persister} to convert
	 * @param listener listener for response
	 * @param errorListener listener for errors
	 */
	public SimpleXmlRequest(int method, String url, Class<T> clazz, Persister persister,
			Listener<T> listener, ErrorListener errorListener) {
		super(method, url, clazz, listener, errorListener);
		setPersister(persister);

	}
	/**
	 * Set a {@code persister} to convert.
	 * @param persister {@link Persister} to convert
	 * @throws NullPointerException if {@code persister} is null 
	 */
	private void setPersister(Persister persister) {
		Assert.notNull(persister, "persister");		
		this.persister = persister;
	}
	
	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {
		String charset = HttpHeaderParser.parseCharset(response.headers);
		Reader reader = null;
		try {
			reader = new InputStreamReader(new ByteArrayInputStream(response.data), charset);
			T result = persister.read(getTargetClass(),	reader);
			return Response.success(result,
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (XmlPullParserException e) {
			return Response.error(new ParseError(e));
		} catch (ElementException e) {
			return Response.error(new ParseError(e));
		} catch (Exception e) {
			return Response.error(new VolleyError(e));
		} finally {
			IoUtils.closeQuietly(reader);
		}
	}
}
