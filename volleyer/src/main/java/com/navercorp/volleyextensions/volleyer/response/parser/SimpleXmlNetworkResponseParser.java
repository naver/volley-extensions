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
import com.navercorp.volleyextensions.volleyer.util.Assert;
import com.navercorp.volleyextensions.volleyer.util.IoUtils;

public class SimpleXmlNetworkResponseParser implements NetworkResponseParser {
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
}
