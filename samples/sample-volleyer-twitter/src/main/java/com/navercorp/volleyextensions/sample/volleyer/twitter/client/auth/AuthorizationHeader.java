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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.navercorp.volleyextensions.sample.volleyer.twitter.client.auth.token.ConsumerToken;
import com.navercorp.volleyextensions.sample.volleyer.twitter.client.auth.token.Token;
import com.navercorp.volleyextensions.sample.volleyer.twitter.util.EncodeUtils;
import com.navercorp.volleyextensions.volleyer.util.StringUtils;

class AuthorizationHeader {
	private static final String OAUTH_DEFAULT_VERSION = "1.0";

	private static final int NONCE_LENGTH = 5;

	private static final String DEFAULT_SIGNATURE_METHOD = "HMAC-SHA1";

	private SignatureEncoder signatureEncoder;
	private String method;
	private String url;
	private ConsumerToken consumerToken;
	private Token token;
	private String verifier;

	private String consumerKey;
	private String signatureMethod;
	private String timestamp;
	private String nonce;
	private String version;

	private AuthorizationHeader(Builder builder) {
		copyProperties(builder);
		initializeOAuthProperties();
	}

	private void copyProperties(Builder builder) {
		this.signatureEncoder = builder.signatureEncoder;
		this.method = builder.method;
		this.url = builder.url;
		this.consumerToken = builder.consumerToken;
		this.token = builder.token;
		this.verifier = builder.verifier;
	}

	private void initializeOAuthProperties() {
		this.consumerKey = consumerToken.key();
		this.signatureMethod = DEFAULT_SIGNATURE_METHOD;
		this.timestamp = createTimestamp();
		this.nonce = StringUtils.generateRandom(NONCE_LENGTH);
		this.version = OAUTH_DEFAULT_VERSION;
	}

	private String createTimestamp() {
		return String.valueOf(System.currentTimeMillis() / 1000L);
	}

	public String toString() {
		return generateHeaderString();
	}

	private String generateHeaderString() {
		List<Parameter> parameters = convertPropertiesToParameters();
		String signature = createSignature(parameters);
		addSignatureParameter(parameters, signature);
		StringBuilder sb = new StringBuilder();
		sb.append("OAuth");
		sb.append(' ');

		boolean hasPrevious = false;
		for(Parameter parameter : parameters) {
			if (hasPrevious) {
				sb.append(',');
			}

			hasPrevious = true;
			sb.append(parameter.key());
			sb.append('=');
			sb.append('"');
			sb.append(parameter.value());
			sb.append('"');
		}

		return sb.toString();
	}

	private void addSignatureParameter(List<Parameter> parameters, String signature) {
		parameters.add(new Parameter("oauth_signature", signature));
	}

	private List<Parameter> convertPropertiesToParameters() {
		List<Parameter> parameters = new ArrayList<Parameter>();
		parameters.add(new Parameter("oauth_consumer_key", consumerKey));
		parameters.add(new Parameter("oauth_signature_method", signatureMethod));
		parameters.add(new Parameter("oauth_timestamp", timestamp));
		parameters.add(new Parameter("oauth_version", version));
		parameters.add(new Parameter("oauth_nonce", nonce));

		if (verifier != null) {
			parameters.add(new Parameter("oauth_verifier", verifier));	
		}

		if (token != null) {
			parameters.add(new Parameter("oauth_token", token.key()));	
		}

		return parameters;
	}

	private List<Parameter> extractParametersFromUrl() {
		List<Parameter> parameters = new ArrayList<Parameter>();

		String query = extractQueryFromUrl();

		if (StringUtils.isEmpty(query)) {
			return parameters;
		}

		String[] pairStrings = query.split("&");
		for (String pairString : pairStrings) {
			Parameter parameter = extractParameter(pairString);
			parameters.add(parameter);
		}

		return parameters;
	}

	private String extractQueryFromUrl() {
		// WARN : It doesn't validate a url.
		int index = url.indexOf('?');
		// if there are no query parameters,
		if (index == -1) {
			return "";
		}

		return url.substring(index + 1, url.length());
	}

	private Parameter extractParameter(String pairString) {
		String key = null;
		String value = null;
		if (containsValueFrom(pairString)) {
			String[] pair = pairString.split("=");
			key = EncodeUtils.encodeParameter(pair[0]);
			value = EncodeUtils.encodeParameter(pair[1]);
		} else {
			key = pairString;
			value = "";
		}

		Parameter parameter = new Parameter(key, value);
		return parameter;
	}

	private boolean containsValueFrom(String pairString) {
		return pairString.indexOf("=") >= 0;
	}

	private String createSignature(List<Parameter> parameters) {
		List<Parameter> parametersForSignature = new ArrayList<Parameter>(parameters);
		List<Parameter> extraParameters = extractParametersFromUrl();

		parametersForSignature.addAll(extraParameters);

		// sort parameters
		Collections.sort(parametersForSignature);

		StringBuilder sb = new StringBuilder();
		sb.append(method); // http method
		sb.append('&');
		sb.append(extractEncodedBaseUrl()); // base url
		sb.append('&');

		boolean hasPrevious = false;
		String encodedAmpersandCharacter = EncodeUtils.encodeParameter("&");

		for (Parameter parameter : parametersForSignature) {
			if (hasPrevious) {
				sb.append(encodedAmpersandCharacter);
			}
			hasPrevious = true;
			sb.append(EncodeUtils.encodeParameter(parameter.key() + "=" + parameter.value()));
		}

		String baseString = sb.toString();
		String encodedSignature = signatureEncoder.encode(baseString);
		return EncodeUtils.encodeParameter(encodedSignature);
	}

	private String extractEncodedBaseUrl() {
		String baseUrl = extractBaseUrlFromUrl();
		String encodedUrl = EncodeUtils.encodeParameter(baseUrl);
		return encodedUrl;
	}

	private String extractBaseUrlFromUrl() {
		int index = url.indexOf('?');
		// if there are no query parameters,
		if (index == -1) {
			return url;
		}

		return url.substring(0, index);
	}

	public static class Builder {
		private SignatureEncoder signatureEncoder;
		private String method;
		private String url;
		private ConsumerToken consumerToken;
		private Token token;
		private String verifier;

		public Builder setEncoder(SignatureEncoder encoder) {
			this.signatureEncoder = encoder;
			return this;
		}

		public Builder setMethod(String method) {
			this.method = method;
			return this;
		}

		public Builder setUrl(String url) {
			this.url = url;
			return this;
		}

		public Builder setConsumerToken(ConsumerToken consumerToken) {
			this.consumerToken = consumerToken;
			return this;
		}

		public Builder setToken(Token token) {
			this.token = token;
			return this;
		}

		public Builder setVerifier(String verifier) {
			this.verifier = verifier;
			return this;
		}

		public AuthorizationHeader build() {
			return new AuthorizationHeader(this);
		}
	}



}
