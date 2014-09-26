package com.navercorp.volleyextensions.sample.volleyer.twitter.client.auth.token;

public class TokenImpl implements Token {
	private String key;
	private String secret;

	public TokenImpl(String key, String secret) {
		this.key = key;
		this.secret = secret;
	}

	@Override
	public String key() {
		return key;
	}

	@Override
	public String secret() {
		return secret;
	}

}
