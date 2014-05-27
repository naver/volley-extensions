package com.navercorp.volleyextensions.volleyer.builder;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.navercorp.volleyextensions.volleyer.VolleyerContext;
import com.navercorp.volleyextensions.volleyer.http.HttpMethod;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class BuilderIntegrationTest {

	@Test
	public void builderChainShouldMakeRequestInstanceFinally() {
		// Given
		VolleyerContext volleyerContext = new VolleyerContext();
		String url = "test";
		HttpMethod method = HttpMethod.GET;
		RequestBuilder builder = new RequestBuilder(volleyerContext, url,
				method);
		Class<String> clazz = String.class;
		Listener<String> listener = new Listener<String>() {

			@Override
			public void onResponse(String response) {
				ShadowLog.d("TestClass", "Result : " + response);
			}
		};
		ErrorListener errorListener = new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				ShadowLog.d("TestClass", "Error : " + error);
			}
		};
		// When
		Request<String> request = 
				builder
				.addHeader("name", "JohnDoe")
				.addHeader("age", "23")
				.addHeader("job", "student")
				.afterRequest()
					.setTargetClass(String.class)
					.setListener(listener)
					.setErrorListener(errorListener)
					.execute();

		// Then
		assertTrue(request != null);
	}
}
