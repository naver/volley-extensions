package com.navercorp.volleyextensions.volleyer.response.parser;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
/**
 * An interface for helping to parse data of various response types. 
 * 
 */
public interface NetworkResponseParser {
	<T> Response<T> parseNetworkResponse(NetworkResponse response, Class<T> clazz);
}