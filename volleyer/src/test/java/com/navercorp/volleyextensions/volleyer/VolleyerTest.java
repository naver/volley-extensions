package com.navercorp.volleyextensions.volleyer;

import static org.hamcrest.CoreMatchers.*;

import org.junit.*;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import com.android.volley.RequestQueue;
import com.navercorp.volleyextensions.volleyer.builder.*;

import static com.navercorp.volleyextensions.volleyer.Volleyer.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class VolleyerTest {
	RequestQueue requestQueue;

	@Before
	public void setUp() {
		requestQueue = mock(RequestQueue.class);
	}

	@Test(expected = NullPointerException.class)
	public void volleyerShouldThrowNpeWhenRequestQueueIsNull() {
		volleyer(null);
	}

	@Test
	public void volleyerShouldReturnVolleyerSameInstanceBySameRequestQueue() {
		assertThat(volleyer(requestQueue), is(volleyer(requestQueue)));
	}

	@Test
	public void volleyerShouldReturnInstanceWhenDefaultVolleyerIsSetOrReturnDummyIfNot() {
		// When default volleyer is not set yet
		// Then
		assertEquals(volleyer().getClass(), DummyVolleyer.class);

		// When default volleyer is set
		volleyer(requestQueue).settings().setAsDefault().done();
		// Then
		assertNotNull(volleyer());
		assertThat(volleyer(), is(volleyer(requestQueue)));
	}

	@Test
	public void volleyerShouldReturnBuildersCorrectly() {
		// Given
		String url = "http://test";
		// When
		GetBuilder get = volleyer(requestQueue).get(url);
		PostBuilder post = volleyer(requestQueue).post(url);
		PutBuilder put = volleyer(requestQueue).put(url);
		DeleteBuilder delete = volleyer(requestQueue).delete(url);
		// Then
		assertNotNull(get);
		assertNotNull(post);
		assertNotNull(put);
		assertNotNull(delete);
	}
}
