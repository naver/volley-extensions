package com.navercorp.volleyextensions.sample.volleyer.twitter.client;

import static com.navercorp.volleyextensions.volleyer.Volleyer.volleyer;

import com.android.volley.NetworkResponse;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

import com.navercorp.volleyextensions.sample.volleyer.twitter.client.auth.Authorizor;
import com.navercorp.volleyextensions.sample.volleyer.twitter.client.auth.token.RequestToken;
import com.navercorp.volleyextensions.sample.volleyer.twitter.client.auth.token.Token;
import com.navercorp.volleyextensions.sample.volleyer.twitter.client.auth.token.TokenImpl;
import com.navercorp.volleyextensions.volleyer.util.Assert;
import com.navercorp.volleyextensions.volleyer.util.VolleyerLog;

public class TwitterLogin {

	private static final NetworkResponse EMPTY_NETWORK_RESPONSE = new NetworkResponse(new byte[]{});
	private static final String OAUTH_TOKEN_OF_RESPONSE = "oauth_token=";
	private static final String OAUTH_TOKEN_SECRET_OF_RESPONSE = "oauth_token_secret=";

	private Authorizor authorizor;
	private RequestToken requestToken;

	public TwitterLogin(Authorizor authorizor) {
		Assert.notNull(authorizor, "Authorizor");
		this.authorizor = authorizor;
	}

	public void requestToken(final Callback callback) {
		Assert.notNull(callback, "Callback");

		String authorization = authorizor.createRequestTokenHeader();
		Callback internalCallback = new Callback() {

			@Override
			public void success(Token token) {
				requestToken = new RequestToken(token.key(), token.secret());
				callback.success(token);
			}

			@Override
			public void error(NetworkResponse networkResponse) {
				callback.error(networkResponse);
			}};

		request(TwitterApis.REQUEST_TOKEN_URL, internalCallback, authorization);
	}

	public void accessToken(String pinNumber, final AccessTokenCallback callback) {
		Assert.notNull(callback, "Callback");
		Assert.notNull(pinNumber, "Pin number");

		if (requestToken == null) {
			VolleyerLog.error("Please call requestToken() first.");
			return;
		}

		String authorization = authorizor.createVerifierHeader(requestToken, pinNumber);
		Callback internalCallback = new Callback() {

			@Override
			public void success(Token accessToken) {
				authorizor.setAccessToken(accessToken);
				Twitter twitter = new Twitter(authorizor);
				callback.success(twitter);
			}

			@Override
			public void error(NetworkResponse networkResponse) {
				callback.error(networkResponse);
			}};

		request(TwitterApis.ACCESS_TOKEN_URL, internalCallback, authorization);
	}

	private void request(String url, final Callback callback, String authorization) {
		volleyer().post(url)
					.addHeader("Authorization", authorization)
					.withListener(new Listener<String>(){

						@Override
						public void onResponse(String str) {
							String oauthTokenKey = extractOAuthToken(str);
							String oauthTokenSecret = extractOAuthTokenSecret(str);
							Token token = new TokenImpl(oauthTokenKey, oauthTokenSecret);
							callback.success(token);
						}})
					.withErrorListener(new ErrorListener(){

							@Override
							public void onErrorResponse(VolleyError volleyerError) {
								NetworkResponse networkResponse;
								if (volleyerError == null) {
									networkResponse = EMPTY_NETWORK_RESPONSE;
								} else {
									networkResponse = volleyerError.networkResponse;									
								}
								callback.error(networkResponse);
							}})
					.execute();
	}

	public static String extractOAuthToken(String str) {
		int startIndex = str.indexOf(OAUTH_TOKEN_OF_RESPONSE) + OAUTH_TOKEN_OF_RESPONSE.length();
		int endIndex = str.indexOf("&", startIndex);
		String oauthToken = str.substring(startIndex, endIndex);
		return oauthToken;
	}

	public static String extractOAuthTokenSecret(String str) {
		int startIndex = str.indexOf(OAUTH_TOKEN_SECRET_OF_RESPONSE) + OAUTH_TOKEN_SECRET_OF_RESPONSE.length();
		int endIndex = str.indexOf("&", startIndex);
		String oauthTokenSecret = str.substring(startIndex, endIndex);
		return oauthTokenSecret;
	}

	public interface Callback {
		void success(Token token);
		void error(NetworkResponse networkResponse);
	}

	public interface AccessTokenCallback {
		void success(Twitter twitter);
		void error(NetworkResponse networkResponse);
	}
}
