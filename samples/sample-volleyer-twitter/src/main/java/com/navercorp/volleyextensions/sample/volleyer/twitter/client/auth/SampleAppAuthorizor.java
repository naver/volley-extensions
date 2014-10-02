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

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.content.Context;

import com.navercorp.volleyextensions.sample.volleyer.twitter.client.auth.token.ConsumerToken;
import com.navercorp.volleyextensions.sample.volleyer.twitter.util.FileUtils;
import com.navercorp.volleyextensions.util.Assert;
import com.navercorp.volleyextensions.volleyer.util.StringUtils;
import com.navercorp.volleyextensions.volleyer.util.VolleyerLog;

public class SampleAppAuthorizor extends Authorizor {

	private static final String CONSUMER_FILE_NAME = "cnsmrscrt";
	private static final byte[] FIXED_CIPHER_KEY = "Zi20kgnamEf$L;wm1etDgYd'".getBytes();
	private static final byte[] DEFAULT_IV = "iv-param".getBytes();

	public SampleAppAuthorizor(Context context) {
		super(createSampleAppConsumerToken(context));
	}

	private static ConsumerToken createSampleAppConsumerToken(Context context) {
		Assert.notNull(context, "Context");
		ConsumerToken consumerToken = null;
		byte[] bytes = FileUtils.getBytesFrom(context, CONSUMER_FILE_NAME);
		String message = extractMessage(bytes);
		consumerToken = parseMessage(message);
		return consumerToken;
	}

	private static String extractMessage(byte[] bytes) {
		String decryptedMessage = "";
		Exception exception = null;
		try {
			Cipher cipher = initializeCipher();
			decryptedMessage = new String(cipher.doFinal(bytes));
		} catch (IllegalBlockSizeException e) {
			exception = e;
		} catch (BadPaddingException e) {
			exception = e;
		} catch (InvalidKeyException e) {
			exception = e;
		} catch (NoSuchAlgorithmException e) {
			exception = e;
		} catch (NoSuchPaddingException e) {
			exception = e;
		} catch (InvalidAlgorithmParameterException e) {
			exception = e;
		}

		logMessageIfExceptionExists(exception, "An error occured while decryption.");
		return decryptedMessage;
	}

	private static Cipher initializeCipher() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
		SecretKey key = new SecretKeySpec(FIXED_CIPHER_KEY, "DESede");
		IvParameterSpec iv = new IvParameterSpec(DEFAULT_IV);
		Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, key, iv);
		return cipher;
	}

	private static void logMessageIfExceptionExists(Exception e, String message) {
		if (e == null) {
			return;
		}

		VolleyerLog.error(e, message);
	}

	private static ConsumerToken parseMessage(String message) {
		if (StringUtils.isEmpty(message)) {
			return null;
		}

		String[] pair = message.split("\n");
		return new ConsumerToken(pair[1], pair[0]);
	}
}
