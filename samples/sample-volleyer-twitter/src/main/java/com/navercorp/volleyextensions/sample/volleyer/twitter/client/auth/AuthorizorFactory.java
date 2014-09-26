package com.navercorp.volleyextensions.sample.volleyer.twitter.client.auth;

import android.content.Context;

public class AuthorizorFactory {

	public static Authorizor createDefaultAuthorizor(Context context) {
		return new SampleAppAuthorizor(context);
	}
}
