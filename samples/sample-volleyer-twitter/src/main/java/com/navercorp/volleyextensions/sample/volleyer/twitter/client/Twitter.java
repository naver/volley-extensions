package com.navercorp.volleyextensions.sample.volleyer.twitter.client;

import static com.navercorp.volleyextensions.volleyer.Volleyer.volleyer;

import java.io.File;
import java.util.Arrays;

import com.android.volley.NetworkResponse;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

import com.navercorp.volleyextensions.sample.volleyer.twitter.client.auth.Authorizor;
import com.navercorp.volleyextensions.sample.volleyer.twitter.client.model.*;
import com.navercorp.volleyextensions.sample.volleyer.twitter.util.EncodeUtils;
import com.navercorp.volleyextensions.volleyer.response.parser.Jackson2NetworkResponseParser;
import com.navercorp.volleyextensions.volleyer.util.Assert;
import com.navercorp.volleyextensions.volleyer.util.StringUtils;
import com.navercorp.volleyextensions.volleyer.util.VolleyerLog;

public class Twitter {
	private static final Jackson2NetworkResponseParser JACKSON_2_NETWORK_RESPONSE_PARSER = new Jackson2NetworkResponseParser();
	private final Authorizor authorizor;

	public Twitter(Authorizor authorizor) {
		this.authorizor = authorizor;
	}

	public void getHomeTimeline(final Callback<Timeline> callback) {
		Assert.notNull(callback, "Callback");
		// Update status
		String url = TwitterApis.HOME_TIMELINE_URL;
		String authorization = authorizor.createApiCallHeader("GET", url);

		// Make a volleyer request
		volleyer().get(url)
				.addHeader("Authorization", authorization)
				.withTargetClass(Status[].class)
				.withResponseParser(JACKSON_2_NETWORK_RESPONSE_PARSER)
				.withListener(new Listener<Status[]>() {

					@Override
					public void onResponse(Status[] statuses) {
						Timeline timeline = new Timeline(Arrays.asList(statuses));
						callback.success(timeline);
					}
				})
				.withErrorListener(new ErrorListener(){

					@Override
					public void onErrorResponse(VolleyError volleyError) {
						if (volleyError == null) {
							return;
						}

						callback.error(volleyError.networkResponse);
					}
				})
				.execute();
	}

	public void updateStatus(String text, final Callback<Status> callback) {
		Assert.notNull(text, "Text");
		Assert.notNull(callback, "Callback");

		if(StringUtils.isEmpty(text)) {
			VolleyerLog.error("Text must not be null.");
			return;
		}

		// Update status
		String url = TwitterApis.UPDATE_STATUS_URL + "?status=" + text;
		String authorization = authorizor.createApiCallHeader("POST", url);

		String encodedParameterUrl = TwitterApis.UPDATE_STATUS_URL + "?status=" + EncodeUtils.encodeParameter(text);

		// Make a volleyer request
		volleyer().post(encodedParameterUrl)
				.addHeader("Authorization", authorization)
				.withTargetClass(Status.class)
				.withResponseParser(JACKSON_2_NETWORK_RESPONSE_PARSER)
				.withListener(new Listener<Status>() {

					@Override
					public void onResponse(Status status) {
						callback.success(status);
					}
				})
				.withErrorListener(new ErrorListener(){

					@Override
					public void onErrorResponse(VolleyError volleyError) {
						if (volleyError == null) {
							return;
						}

						// Print error onto logcat if an error message exists.
						volleyError.printStackTrace();

						callback.error(volleyError.networkResponse);
					}
				})
				.execute();
	}

	public void updateStatusWithMedia(String text, File mediaFile, final Callback<Status> callback) {
		Assert.notNull(text, "Text");
		Assert.notNull(mediaFile, "Media file");
		Assert.notNull(callback, "Callback");

		if(StringUtils.isEmpty(text)) {
			VolleyerLog.error("Text must not be null.");
			return;
		}

		// update status
		String url = TwitterApis.UPDATE_WITH_MEDIA_URL;
		String authorization = authorizor.createApiCallHeader("POST", url);

		// Make a volleyer request
		volleyer().post(url)
				.addHeader("Authorization", authorization)
				.addStringPart("status", text)
				.addFilePart("media[]", mediaFile)
				.withTargetClass(Status.class)
				.withResponseParser(JACKSON_2_NETWORK_RESPONSE_PARSER)
				.withListener(new Listener<Status>() {

					@Override
					public void onResponse(Status status) {
						callback.success(status);
					}
				})
				.withErrorListener(new ErrorListener(){

					@Override
					public void onErrorResponse(VolleyError volleyError) {
						if (volleyError == null) {
							return;
						}
						volleyError.printStackTrace();
						callback.error(volleyError.networkResponse);
					}
				})
				.execute();

	}

	public interface Callback<T> {
		void success(T response);
		void error(NetworkResponse networkResponse);
	}

}
