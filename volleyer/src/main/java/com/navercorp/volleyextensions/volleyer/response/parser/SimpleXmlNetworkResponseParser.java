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
package com.navercorp.volleyextensions.volleyer.response.parser;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import org.simpleframework.xml.core.ElementException;
import org.simpleframework.xml.core.Persister;
import org.xmlpull.v1.XmlPullParserException;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.navercorp.volleyextensions.volleyer.http.ContentType;
import com.navercorp.volleyextensions.volleyer.http.ContentTypes;
import com.navercorp.volleyextensions.volleyer.util.Assert;
import com.navercorp.volleyextensions.volleyer.util.IoUtils;
/**
 * <pre>
 * A parser class which converts xml data to T object.
 *
 * NOTE : If this class is added into {@link IntegratedNetworkResponseParser},
 * and the content type of a response is "application/xml" or "text/xml" type,
 * integrated parser automatically delegates to this class.
 *
 * WARN : You have to import simple xml library to use this class.
 * If not, this class throws an error when initializing.
 * </pre>
 */
public class SimpleXmlNetworkResponseParser implements TypedNetworkResponseParser {
	/** Default {@link Persister} is singleton */
	private static class PersisterHolder {
		private static final Persister persister = new Persister();

		private static Persister getPersister() {
			return persister;
		}
	}
	/** {@code persister} is immutable(but not severely). */
	private Persister persister;

	public SimpleXmlNetworkResponseParser() {
		this(PersisterHolder.getPersister());
	}

	public SimpleXmlNetworkResponseParser(Persister persister) {
		Assert.notNull(persister, "Persister");		
		this.persister = persister;
	}

	@Override
	public <T> Response<T> parseNetworkResponse(NetworkResponse response,
			Class<T> clazz) {
		String charset = HttpHeaderParser.parseCharset(response.headers);
		Reader reader = null;
		try {
			reader = new InputStreamReader(new ByteArrayInputStream(response.data), charset);
			T result = persister.read(clazz, reader);
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

	@Override
	public ContentTypes getContentTypes() {
		return new ContentTypes(ContentType.CONTENT_TYPE_APPLICATION_XML, ContentType.CONTENT_TYPE_TEXT_XML);
	}
}
