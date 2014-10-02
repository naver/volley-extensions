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

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.navercorp.volleyextensions.sample.volleyer.twitter.client.auth.token.ConsumerToken;
import com.navercorp.volleyextensions.sample.volleyer.twitter.client.auth.token.Token;
import com.navercorp.volleyextensions.sample.volleyer.twitter.util.EncodeUtils;
import com.navercorp.volleyextensions.volleyer.util.Assert;
import com.navercorp.volleyextensions.volleyer.util.VolleyerLog;

class SignatureEncoder {

	private static final String CRYPT_ALGORITHM = "HmacSHA1";

	private Mac mac;
	private boolean initialized = false;

	public SignatureEncoder(ConsumerToken consumerToken) {
		this(consumerToken, null /* token*/);
	}

	public SignatureEncoder(ConsumerToken consumerToken, Token token) {
		Assert.notNull(consumerToken, "ConsumerToken");
		initializeMac(consumerToken, token);
	}

	private void initializeMac(ConsumerToken consumerToken, Token token) {
		try {
			mac = Mac.getInstance(CRYPT_ALGORITHM);
			SecretKeySpec keySpec = createKeySpec(consumerToken, token);
			mac.init(keySpec);
			initialized = true;
		} catch (NoSuchAlgorithmException e) {
			VolleyerLog.error(e, "There is no algorithm.");
		} catch (InvalidKeyException e) {
			VolleyerLog.error(e, "The key is invalid.");
		}
	}

	private SecretKeySpec createKeySpec(ConsumerToken consumerToken, Token token) {
		StringBuilder sb = new StringBuilder();
		sb.append(EncodeUtils.encodeParameter(consumerToken.secret()));
		sb.append('&');

		if (token != null) {
			sb.append(EncodeUtils.encodeParameter(token.secret()));
		}

		String key = sb.toString();
		SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), CRYPT_ALGORITHM);
		return keySpec;
	}

	public String encode(String signature) {
		if (!initialized) {
			VolleyerLog.error("Initialization has been failed.");
			// return empty string
			return "";
		}

		byte[] result = mac.doFinal(signature.getBytes());
		return Base64.encodeToString(result, Base64.NO_WRAP);
	}

	public String getSignatureMethod() {
		return CRYPT_ALGORITHM;
	}
}
