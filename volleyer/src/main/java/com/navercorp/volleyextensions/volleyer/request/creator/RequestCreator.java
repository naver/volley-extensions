package com.navercorp.volleyextensions.volleyer.request.creator;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.navercorp.volleyextensions.volleyer.http.HttpContent;
import com.navercorp.volleyextensions.volleyer.response.parser.NetworkResponseParser;
/**
 * <pre>
 * An interface that determines how to create a Request instance after completion of settings by volleyer.
 * To apply the implementation for this interface, you need to set it for {@link VolleyerConfiguration}.
 * </pre>
 */
public interface RequestCreator {
	/**
	 * <pre>
	 * Create a {@code Request} instance.
	 * All of the parameters are delivered from {@code ResponseBuilder}.
	 * </pre>
	 * @return Request<T> newly created instance 
	 */
	<T> Request<T> createRequest(HttpContent httpContent, Class<T> clazz,
			NetworkResponseParser responseParser, Listener<T> listener,
			ErrorListener errorListener);
}
