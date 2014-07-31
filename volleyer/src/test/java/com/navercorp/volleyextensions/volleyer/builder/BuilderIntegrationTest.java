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
import com.navercorp.volleyextensions.volleyer.VolleyerConfiguration;
import com.navercorp.volleyextensions.volleyer.factory.DefaultVolleyerConfigurationFactory;
import com.navercorp.volleyextensions.volleyer.http.HttpMethod;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class BuilderIntegrationTest {
	static VolleyerConfiguration configuration = DefaultVolleyerConfigurationFactory.create();
	static String url = "http://test";
	static String body = "Test body";

	static Listener<String> listener = new Listener<String>() {

		@Override
		public void onResponse(String response) {
			ShadowLog.d("TestClass", "Result : " + response);
		}
	};

	static ErrorListener errorListener = new ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError error) {
			ShadowLog.d("TestClass", "Error : " + error);
		}
	};

	RequestQueue requestQueue = mock(RequestQueue.class);

	@Test
	public void getBuilderChainShouldMakeRequestInstanceFinally() throws AuthFailureError {
		GetBuilder getBuilder = new GetBuilder(requestQueue, configuration, url);
		Request<String> request = createRequest(url, getBuilder);
		assertRequest(url, HttpMethod.GET, request);
	}

	@Test
	public void postBuilderChainShouldMakeRequestInstanceFinally() throws AuthFailureError {
		PostBuilder postBuilder = new PostBuilder(requestQueue, configuration, url);
		buildBodyOptionFor(postBuilder);
		Request<String> request = createRequest(url, postBuilder);
		assertRequest(url, HttpMethod.POST, request);
		assertBodyOption(request);
	}

	private static void buildBodyOptionFor(BodyBuilder<?> builder) {
		builder.setBody(body);
	}

	private static void assertBodyOption(Request<String> request) throws AuthFailureError {
		assertThat(request.getBody(), is(body.getBytes()));
	}

	@Test
	public void putBuilderChainShouldMakeRequestInstanceFinally() throws AuthFailureError {
		PutBuilder putBuilder = new PutBuilder(requestQueue, configuration, url);
		buildBodyOptionFor(putBuilder);
		Request<String> request = createRequest(url, putBuilder);
		assertRequest(url, HttpMethod.PUT, request);
		assertBodyOption(request);
	}

	@Test
	public void deleteBuilderChainShouldMakeRequestInstanceFinally() throws AuthFailureError {
		DeleteBuilder deleteBuilder = new DeleteBuilder(requestQueue, configuration, url);
		Request<String> request = createRequest(url, deleteBuilder);
		assertRequest(url, HttpMethod.DELETE, request);
	}

	private static <B extends RequestBuilder<B>> Request<String> createRequest(String url, RequestBuilder<B> builder) throws AuthFailureError {
		Request<String> request = 
				builder
				.addHeader("name", "JohnDoe")
				.addHeader("age", "23")
				.addHeader("job", "student")
				.setTargetClass(String.class)
					.setListener(listener)
					.setErrorListener(errorListener)
					.execute();

		return request;
	}

	private static void assertRequest(String url, HttpMethod method, Request<String> request) throws AuthFailureError {
		// Then
		assertTrue(request != null);
		assertThat(request.getUrl(), is(url));
		assertThat(request.getMethod(), is(method.getMethodCode()));
		assertThat(request.getHeaders().get("name"), is("JohnDoe"));
		assertThat(request.getHeaders().get("age"), is("23"));
		assertThat(request.getHeaders().get("job"), is("student"));
	}
}
