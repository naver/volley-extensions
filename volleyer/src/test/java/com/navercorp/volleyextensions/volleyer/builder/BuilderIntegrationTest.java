package com.navercorp.volleyextensions.volleyer.builder;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.navercorp.volleyextensions.volleyer.DefaultVolleyerContextFactory;
import com.navercorp.volleyextensions.volleyer.VolleyerContext;
import com.navercorp.volleyextensions.volleyer.http.HttpMethod;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class BuilderIntegrationTest {
	RequestQueue requestQueue = mock(RequestQueue.class);

	@Test
	public void builderChainShouldMakeRequestInstanceFinally() throws AuthFailureError {
		// Given
		VolleyerContext volleyerContext = DefaultVolleyerContextFactory.create();
		String url = "http://test";
		HttpMethod method = HttpMethod.GET;
		TestPurposeRequestBuilder builder = new TestPurposeRequestBuilder(volleyerContext, url,
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
				.setTargetClass(String.class)
					.setListener(listener)
					.setErrorListener(errorListener)
					.execute();

		// Then
		assertTrue(request != null);
		assertThat(request.getUrl(), is(url));
		assertThat(request.getMethod(), is(method.getMethodCode()));
		assertThat(request.getHeaders().get("name"), is("JohnDoe"));
		assertThat(request.getHeaders().get("age"), is("23"));
		assertThat(request.getHeaders().get("job"), is("student"));
	}
}
