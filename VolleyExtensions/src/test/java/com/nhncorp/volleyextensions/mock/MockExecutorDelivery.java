package com.nhncorp.volleyextensions.mock;

import java.util.concurrent.Executor;

import com.android.volley.ExecutorDelivery;

public class MockExecutorDelivery extends ExecutorDelivery {

	public MockExecutorDelivery() {
		this(new Executor() {
			@Override
			public void execute(Runnable runnable) {
				runnable.run();
			}
		});
	}

	private MockExecutorDelivery(Executor executor) {
		super(executor);
	}
}
