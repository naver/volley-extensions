package com.navercorp.volleyextensions.volleyer.response.parser;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
/**
 * <pre>
 * An interface for helping to parse data of {@code NetworkResponse} to a target class.
 * 
 * NOTE : If you implement new NetworkResponseParser,
 *        it's recommend you to make it thread-safe and use it as a singleton.
 * </pre>
 * @see NetworkResponse
 * 
 */
public interface NetworkResponseParser {
	/**
	 * Parse data of {@code NetworkResponse} to T object.
	 * @param <T> Target class that data will be parsed to.
	 * @return Response which contains parsed T object or contains some error if it happened.
	 */
	<T> Response<T> parseNetworkResponse(NetworkResponse response, Class<T> clazz);
}
