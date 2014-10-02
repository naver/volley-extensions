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
package com.navercorp.volleyextensions.sample.volleyer.twitter.client.auth;

import com.navercorp.volleyextensions.sample.volleyer.twitter.client.TwitterApis;
import com.navercorp.volleyextensions.sample.volleyer.twitter.client.auth.token.AccessToken;
import com.navercorp.volleyextensions.sample.volleyer.twitter.client.auth.token.ConsumerToken;
import com.navercorp.volleyextensions.sample.volleyer.twitter.client.auth.token.RequestToken;
import com.navercorp.volleyextensions.sample.volleyer.twitter.client.auth.token.Token;
import com.navercorp.volleyextensions.volleyer.util.Assert;
import com.navercorp.volleyextensions.volleyer.util.VolleyerLog;

public class Authorizor {

	private ConsumerToken consumerToken;
	private AccessToken accessToken = null;
	private SignatureEncoder signatureEncoder = null;

	public Authorizor(ConsumerToken consumerToken) {
		this.consumerToken = consumerToken;
	}

	public boolean isInitialized() {
		return consumerToken != null;
	}

	private void assertInitialized() {
		if(!isInitialized()) {
			throw new IllegalStateException("Consumer token is not set.");
		}
	}

	public String createRequestTokenHeader() {
		assertInitialized();
		SignatureEncoder encoder = new SignatureEncoder(consumerToken);
		String header = new AuthorizationHeader.Builder().setEncoder(encoder)
														.setMethod("POST")
														.setConsumerToken(consumerToken)
														.setUrl(TwitterApis.REQUEST_TOKEN_URL)
														.build()
														.toString();
		return header;
	}

	public String createVerifierHeader(RequestToken token, String pinNumber) {
		assertInitialized();
		Assert.notNull(token, "token");
		Assert.notNull(pinNumber, "PinNumber");

		SignatureEncoder encoder = new SignatureEncoder(consumerToken, token);
		String header = new AuthorizationHeader.Builder().setEncoder(encoder)
														.setMethod("POST")
														.setConsumerToken(consumerToken)
														.setToken(token)
														.setUrl(TwitterApis.ACCESS_TOKEN_URL)
														.setVerifier(pinNumber)
														.build()
														.toString();
		return header;
	}

	public String createApiCallHeader(String method, String url) {
		assertInitialized();
		Assert.notNull(method, "HttpMethod");
		Assert.notNull(url, "Url");

		if (accessToken == null || signatureEncoder == null) {
			VolleyerLog.warn("The accessToken is not set.");
			// return empty string
			return "";
		}

		String header = new AuthorizationHeader.Builder().setEncoder(signatureEncoder)
														.setMethod(method)
														.setConsumerToken(consumerToken)
														.setToken(accessToken)
														.setUrl(url)
														.build()
														.toString();
		return header;
	}

	public synchronized final void setAccessToken(Token token) {
		assertInitialized();
		Assert.notNull(token, "AccessToken");
		this.accessToken = new AccessToken(token.key(), token.secret());
		initializeDefaultSignatureEncoder();
	}

	private void initializeDefaultSignatureEncoder() {
		signatureEncoder = new SignatureEncoder(consumerToken, accessToken);
	}

	public synchronized final AccessToken getAccessToken() {
		return accessToken;
	}

}
