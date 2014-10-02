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
package com.navercorp.volleyextensions.volleyer;

import static com.jayway.awaitility.Awaitility.*;
import static com.navercorp.volleyextensions.volleyer.mock.ListenerVerifier.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.*;
import org.junit.runner.RunWith;
import org.robolectric.*;
import org.robolectric.annotation.Config;
import org.simpleframework.xml.Element;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.NoCache;
import com.github.kristofa.test.http.MockHttpServer;
import com.github.kristofa.test.http.SimpleHttpResponseProvider;
import com.navercorp.volleyextensions.volleyer.exception.UnsupportedContentTypeException;
import com.navercorp.volleyextensions.volleyer.factory.DefaultVolleyerConfigurationFactory;
import com.navercorp.volleyextensions.volleyer.http.HttpContent;
import com.navercorp.volleyextensions.volleyer.mock.ErrorResponseHoldListener;
import com.navercorp.volleyextensions.volleyer.mock.MockExecutorDelivery;
import com.navercorp.volleyextensions.volleyer.mock.ResponseHoldListener;
import com.navercorp.volleyextensions.volleyer.request.creator.RequestCreator;
import com.navercorp.volleyextensions.volleyer.request.executor.RequestExecutor;
import com.navercorp.volleyextensions.volleyer.response.parser.JacksonNetworkResponseParser;
import com.navercorp.volleyextensions.volleyer.response.parser.NetworkResponseParser;

import static com.navercorp.volleyextensions.volleyer.Volleyer.*;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, shadows = {MyShadowSystemClock.class})
public class VolleyerIntegrationTest {
	private static final int THREAD_POOL_SIZE = 4;
	private static final int PORT = 51234;
	private static final String url = "http://localhost:" + PORT;
	private RequestQueue requestQueue;
	private SimpleHttpResponseProvider responseProvider;
	private MockHttpServer server;
	private ErrorResponseHoldListener errorListener;

	@Before
	public void setUp() throws Exception {
		// init a Volley RequestQueue
		requestQueue = new RequestQueue(new NoCache(), new BasicNetwork(
				new HurlStack()), THREAD_POOL_SIZE, new MockExecutorDelivery());
		requestQueue.start();
		// init error listener
		errorListener = new ErrorResponseHoldListener();
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
	public void volleyerShouldDoImmediatelyExecute() {
		volleyer(requestQueue).get(url).execute();
	}

	@Test
	public void volleyerShouldListenError() throws Exception {
		// Given
		responseProvider.expect(com.github.kristofa.test.http.Method.GET, "/")
						.respondWith(400, "", "");
		// When
		volleyer(requestQueue)
							.get(url)
							.withErrorListener(errorListener)
							.execute();
		// Then
		with().await("400Error").until(
				wasErrorListenerCalled(errorListener));
	}

	@Test
	public void postMethodShouldListenResponse() throws Exception {
		// Given
		responseProvider.expect(com.github.kristofa.test.http.Method.POST, "/")
						.respondWith(200, "text/plain", "Hello World");
		ResponseHoldListener<String> listener = new ResponseHoldListener<String>();
		// When
		volleyer(requestQueue)
							.post(url)
							.withListener(listener)
							.withErrorListener(errorListener)
							.execute();
		// Then
		with().await("postMethodShouldListenResponse").until(
				wasListenerCalled(listener));
	}

	@Test
	public void putMethodShouldListenResponse() throws Exception {
		// Given
		responseProvider.expect(com.github.kristofa.test.http.Method.PUT, "/")
						.respondWith(200, "text/plain", "Hello World");
		ResponseHoldListener<String> listener = new ResponseHoldListener<String>();
		// When
		volleyer(requestQueue)
							.put(url)
							.withListener(listener)
							.withErrorListener(errorListener)
							.execute();
		// Then
		with().await("putMethodShouldListenResponse").until(
				wasListenerCalled(listener));
	}

	@Test
	public void deleteMethodShouldListenResponse() throws Exception {
		// Given
		responseProvider.expect(com.github.kristofa.test.http.Method.DELETE, "/")
						.respondWith(200, "text/plain", "Hello World");
		ResponseHoldListener<String> listener = new ResponseHoldListener<String>();
		// When
		volleyer(requestQueue)
							.delete(url)
							.withListener(listener)
							.withErrorListener(errorListener)
							.execute();
		// Then
		with().await("deleteMethodShouldListenResponse").until(
				wasListenerCalled(listener));
	}

	@Test
	public void responseShouldBeParsedString() throws Exception {
		// Given
		String responseBody = "Hello World";
		responseProvider.expect(com.github.kristofa.test.http.Method.GET, "/")
						.respondWith(200, "application/json", responseBody);
		ResponseHoldListener<String> listener = new ResponseHoldListener<String>();
		// When
		volleyer(requestQueue)
							.get(url)
							.withListener(listener)
							.execute();
		// Then
		with().await("responseShouldBeParsedString").until(
				wasListenerCalled(listener));
		String result = listener.getLastResponse();
		assertThat(result, is(responseBody));
	}

	@Test
	public void jsonResponseShouldBeParsedObject() throws Exception {
		// Given
		String responseBody = "{\"name\":\"Hello World\"}";
		responseProvider.expect(com.github.kristofa.test.http.Method.GET, "/")
						.respondWith(200, "application/json", responseBody);
		ResponseHoldListener<Person> listener = new ResponseHoldListener<Person>();
		Class<Person> clazz = Person.class;

		// When
		volleyer(requestQueue)
							.get(url)
							.withTargetClass(clazz)
							.withListener(listener)
							.execute();
		// Then
		with().await("jsonResponseShouldBeParsedObject").until(
				wasListenerCalled(listener));
		Person person = listener.getLastResponse();
		assertNotNull(person.name);
	}


	@Test
	public void xmlResponseShouldBeParsedObject() throws Exception {
		// Given
		String responseBody = "<xml><name>Hello World</name></xml>";
		responseProvider.expect(com.github.kristofa.test.http.Method.GET, "/")
						.respondWith(200, "application/xml", responseBody);
		ResponseHoldListener<Person> listener = new ResponseHoldListener<Person>();
		Class<Person> clazz = Person.class;

		// When
		volleyer(requestQueue)
							.get(url)
							.withTargetClass(clazz)
							.withListener(listener)
							.execute();
		// Then
		with().await("xmlResponseShouldBeParsedObject").until(
				wasListenerCalled(listener));
		Person person = listener.getLastResponse();
		assertNotNull(person.name);
	}

	@Test
	public void errorShouldBeListenedWhenParseInvalidJsonFormat() throws Exception {
		// Given
		String responseBody = "{\"json\":, \"invalid\"}";
		responseProvider.expect(com.github.kristofa.test.http.Method.GET, "/")
				.respondWith(200, "application/json", responseBody);
		Class<Person> clazz = Person.class;
		// When
		volleyer(requestQueue)
							.get(url)
							.withTargetClass(clazz)
							.withErrorListener(errorListener)
							.execute();
		// Then
		with().await("errorShouldBeListenedWhenParseInvalidFormat").until(
				wasErrorListenerCalled(errorListener));
		VolleyError error = errorListener.getLastError();
		assertThat(error, is(instanceOf(ParseError.class)));
	}

	@Test
	public void errorShouldBeListenedWhenParseInvalidXmlFormat() throws Exception {
		// Given
		String responseBody = "<xml><name>Hello World</name><xml>";
		responseProvider.expect(com.github.kristofa.test.http.Method.GET, "/")
				.respondWith(200, "application/xml", responseBody);
		Class<Person> clazz = Person.class;
		// When
		volleyer(requestQueue)
							.get(url)
							.withTargetClass(clazz)
							.withErrorListener(errorListener)
							.execute();
		// Then
		with().await("errorShouldBeListenedWhenParseInvalidXmlFormat").until(
				wasErrorListenerCalled(errorListener));
		VolleyError error = errorListener.getLastError();
		assertThat(error, is(instanceOf(ParseError.class)));
	}

	@Test
	public void errorShouldBeListenedWhenResponseContentTypeIsUnknown() throws Exception {
		// Given
		String responseBody = "{\"json\":, \"invalid\"}";
		responseProvider.expect(com.github.kristofa.test.http.Method.GET, "/")
				.respondWith(200, "application/unknowntype", responseBody);
		Class<Person> clazz = Person.class;
		// When
		volleyer(requestQueue)
							.get(url)
							.withTargetClass(clazz)
							.withErrorListener(errorListener)
							.execute();
		// Then
		with().await("errorShouldBeListenedWhenResponseContentTypeIsUnknown").until(
				wasErrorListenerCalled(errorListener));
		VolleyError error = errorListener.getLastError();
		assertThat(error, is(instanceOf(ParseError.class)));
		assertThat(error.getCause(), is(instanceOf(UnsupportedContentTypeException.class)));
	}

	@Test
	public void volleyerShouldUseCustomReponseParserIfSet() throws Exception {
		// Given
		String responseBody = "{\"name\":\"Hello World\"}";
		responseProvider.expect(com.github.kristofa.test.http.Method.GET, "/")
						.respondWith(200, "application/unknown", responseBody);
		ResponseHoldListener<Person> listener = new ResponseHoldListener<Person>();
		Class<Person> clazz = Person.class;

		// When
		volleyer(requestQueue)
							.get(url)
							.withTargetClass(clazz)
							.withResponseParser(new JacksonNetworkResponseParser())
							.withListener(listener)
							.execute();
		// Then
		with().await("volleyerShouldUseCustomReponseParserIfSet").until(
				wasListenerCalled(listener));
		Person person = listener.getLastResponse();
		assertNotNull(person.name);
	}

	@Test
	public void fallbackRequestExecutorShouldBeConsumedByVolleyer() throws Exception {
		// Given
		RequestQueue mockRequestQueue = mock(RequestQueue.class);
		RequestExecutor noExecutor = new RequestExecutor(){

			@Override
			public <T> void executeRequest(RequestQueue requestQueue,
					Request<T> request) {
				// Do nothing
			}};
		VolleyerConfiguration configuration = new VolleyerConfiguration(DefaultVolleyerConfigurationFactory.createRequestCreator(), 
																		noExecutor,
																		DefaultVolleyerConfigurationFactory.createNetworkResponseParser(),
																		DefaultVolleyerConfigurationFactory.createErrorListener());
		volleyer(mockRequestQueue).settings()
									.setConfiguration(configuration)
									.done();
		// When
		Request<Void> request = volleyer(mockRequestQueue)
														.get(url)
														.execute();
		// Then
		verify(mockRequestQueue, never()).add(request);
	}

	@Test
	public void fallbackRequestCreatorShouldBeConsumedByVolleyer() throws Exception {
		// Given
		RequestQueue mockRequestQueue = mock(RequestQueue.class);
		RequestCreator noCreator = new RequestCreator(){

			@Override
			public <T> Request<T> createRequest(HttpContent httpContent,
					Class<T> clazz, NetworkResponseParser responseParser,
					Listener<T> listener, ErrorListener errorListener) {
				return null;
			}};

		VolleyerConfiguration configuration = new VolleyerConfiguration(noCreator,
																		DefaultVolleyerConfigurationFactory.createRequestExecutor(),
																		DefaultVolleyerConfigurationFactory.createNetworkResponseParser(),
																		DefaultVolleyerConfigurationFactory.createErrorListener());
		volleyer(mockRequestQueue).settings()
									.setConfiguration(configuration)
									.done();
		// When
		Request<Void> request = volleyer(mockRequestQueue)
														.get(url)
														.execute();
		// Then
		assertNull(request);
	}

	@Test
	public void fallbackErrorListenerShouldBeConsumedByVolleyer() throws Exception {
		// Given
		responseProvider.expect(com.github.kristofa.test.http.Method.GET, "/")
						.respondWith(400, "", "");
		VolleyerConfiguration configuration = new VolleyerConfiguration(DefaultVolleyerConfigurationFactory.createRequestCreator(),
																		DefaultVolleyerConfigurationFactory.createRequestExecutor(),
																		DefaultVolleyerConfigurationFactory.createNetworkResponseParser(),
																		errorListener);
		volleyer(requestQueue).settings()
									.setConfiguration(configuration)
									.done();
		// When
		volleyer(requestQueue)
							.get(url)
							.execute();
		// Then
		with().await("fallbackErrorListenerShouldBeConsumedByVolleyer").until(
				wasErrorListenerCalled(errorListener));
	}

	@Test
	public void fallbackResponseParserShouldBeConsumedByVolleyer() throws Exception {
		// Given
		String responseBody = "<xml><name>Hello World</name></xml>";
		responseProvider.expect(com.github.kristofa.test.http.Method.GET, "/")
						.respondWith(200, "application/xml", responseBody);
		NetworkResponseParser noParser = new NetworkResponseParser(){

			@Override
			public <T> Response<T> parseNetworkResponse(
					NetworkResponse response, Class<T> clazz) {
				return Response.error(new ParseError());
			}};
		VolleyerConfiguration configuration = new VolleyerConfiguration(DefaultVolleyerConfigurationFactory.createRequestCreator(),
																		DefaultVolleyerConfigurationFactory.createRequestExecutor(),
																		noParser,
																		DefaultVolleyerConfigurationFactory.createErrorListener());
		volleyer(requestQueue).settings()
									.setConfiguration(configuration)
									.done();
		Class<Person> clazz = Person.class;
		// When
		volleyer(requestQueue)
							.get(url)
							.withTargetClass(clazz)
							.withErrorListener(errorListener)
							.execute();
		// Then
		with().await("fallbackResponseParserShouldBeConsumedByVolleyer").until(
				wasErrorListenerCalled(errorListener));
		VolleyError error = errorListener.getLastError();
		assertThat(error, is(instanceOf(ParseError.class)));
	}

	/** just for test */
	@org.simpleframework.xml.Root(name = "xml")
	private static class Person {
		@Element(name = "name")
		public String name;
	}	
}
