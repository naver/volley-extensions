package com.nhncorp.volleyextensions.request;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import javax.xml.stream.XMLStreamException;

import org.simpleframework.xml.core.ElementException;
import org.simpleframework.xml.core.Persister;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.nhncorp.volleyextensions.util.Assert;
import com.nhncorp.volleyextensions.util.IoUtils;
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
	 * @param method HTTP method. See here : {@link Request.Method} 
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
	 * @param method HTTP method. See here : {@link Request.Method} 
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
		} catch (XMLStreamException e) {
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
