package com.navercorp.volleyextensions.volleyer;

import com.navercorp.volleyextensions.volleyer.builder.DeleteBuilder;
import com.navercorp.volleyextensions.volleyer.builder.GetBuilder;
import com.navercorp.volleyextensions.volleyer.builder.PostBuilder;
import com.navercorp.volleyextensions.volleyer.builder.PutBuilder;
/**
 * <pre>
 * Sub Volleyer class which throws exceptions when APIs are called.
 * This class is made for which prevents users from calling 'volleyer()' method without setting the default volleyer.
 * </pre>
 * @author Wonjun Kim
 *
 */
class DummyVolleyer extends Volleyer {

	DummyVolleyer() {
		super(null /* RequestQueue */);
	}

	private void throwError() {
		throw new UnsupportedOperationException();
	}

	@Override
	public GetBuilder get(String url) {
		throwError();
		return null;
	}

	@Override
	public PostBuilder post(String url) {
		throwError();
		return null;
	}

	@Override
	public PutBuilder put(String url) {
		throwError();
		return null;
	}

	@Override
	public DeleteBuilder delete(String url) {
		throwError();
		return null;
	}

	@Override
	public Settings settings() {
		throwError();
		return null;
	}

}
