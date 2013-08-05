package com.nhncorp.volleyextensions.mock;

import java.util.concurrent.Callable;

public class ListenerVerifier {
	/** Check whether {@code Listener} was called or not */
	public static Callable<Boolean> wasListenerCalled(final ResponseHoldListener<?> mock) {
		return new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return mock.getLastResponse() != null;
			}
		};
	}

	/** Check whether {@code ErrorListener} was called or not */
	public static Callable<Boolean> wasErrorListenerCalled(
			final ErrorResponseHoldListener mock) {
		return new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return mock.getLastError() != null;
			}
		};
	}

}
