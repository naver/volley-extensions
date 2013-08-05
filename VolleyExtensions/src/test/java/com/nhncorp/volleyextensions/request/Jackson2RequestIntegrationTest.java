package com.nhncorp.volleyextensions.request;

import static com.jayway.awaitility.Awaitility.*;
import static com.nhncorp.volleyextensions.mock.ListenerVerifier.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.NoCache;
import com.fasterxml.jackson.core.JsonParseException;
import com.github.kristofa.test.http.MockHttpServer;
import com.github.kristofa.test.http.SimpleHttpResponseProvider;
import com.nhncorp.volleyextensions.mock.ErrorResponseHoldListener;
import com.nhncorp.volleyextensions.mock.MockExecutorDelivery;
import com.nhncorp.volleyextensions.mock.ResponseHoldListener;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class Jackson2RequestIntegrationTest {
	static final int THREAD_POOL_SIZE = 4;
	static final int PORT = 51234;
	static final String url = "http://localhost:" + PORT;
	RequestQueue requestQueue;
	SimpleHttpResponseProvider responseProvider;
	MockHttpServer server;
	ResponseHoldListener<Person> listener = new ResponseHoldListener<Person>();
	ErrorResponseHoldListener errorListener = new ErrorResponseHoldListener();
	
	@BeforeClass
	public static void setUpOnce() throws Exception {
		ShadowLog.stream = System.out;
	}

	@Before
	public void setUp() throws Exception {
		// init a Volley RequestQueue
		requestQueue = new RequestQueue(new NoCache(), new BasicNetwork(new HurlStack()), 
						THREAD_POOL_SIZE, new MockExecutorDelivery());
		requestQueue.start();
		// init mock http server
		responseProvider = new SimpleHttpResponseProvider();
		server = new MockHttpServer(PORT, responseProvider);
		server.start();
	}

	@After
	public void tearDown() throws Exception {
		// stop the mock http server of running
		server.stop();
		// stop the volley
		requestQueue.stop();
	}

	@Test
	public void responseShouldBeParsedWithSpecialChars() throws Exception {
		// Given
		responseProvider.expect(com.github.kristofa.test.http.Method.GET, "/")
				.respondWith(200, "application/json",
						"{\"name\":\"å &acirc;\"}");

		// When
		Request<Person> request = new Jackson2Request<Person>(url, Person.class, listener, errorListener);
		requestQueue.add(request);
		with().await("testWhenSpecialCharactersExist").until(
				wasListenerCalled(listener));
		// Then
		Person person = listener.getLastResponse();
		assertNotNull(person.name);
	}

	@Test
	public void responseShouldBeParsedWithUnknownProperties() throws Exception {
		// Given
		responseProvider
				.expect(com.github.kristofa.test.http.Method.GET, "/")
				.respondWith(200, "application/json",
						"{\"name\":\"hello\", \"unknown\":\"unknown\", \"property\":\"unknown property\"}");

		// When
		Request<Person> request = new Jackson2Request<Person>(url,
				Person.class, listener, errorListener);
		requestQueue.add(request);
		with().await("testWhenUnknownPropertiesExist").until(
				wasListenerCalled(listener));
		// Then
		Person person = listener.getLastResponse();
		assertThat(person.name, is("hello"));
	}

	@Test
	public void errorShouldBeListenedWhenParseInvalidFormat() throws Exception {
		// Given
		responseProvider.expect(com.github.kristofa.test.http.Method.GET, "/")
				.respondWith(200, "application/json",
						"{\"json\":, \"invalid\"}");

		// When
		Request<Person> request = new Jackson2Request<Person>(url,
				Person.class, listener, errorListener);
		requestQueue.add(request);

		// Then
		with().await("testInvalidJson").until(
				wasErrorListenerCalled(errorListener));
		VolleyError error = errorListener.getLastError();
		assertThat(error, is(instanceOf(ParseError.class)));
		assertThat(error.getCause(), is(instanceOf(JsonParseException.class)));
	}

	/** just for test */
	private static class Person {
		public String name;
	}
}
