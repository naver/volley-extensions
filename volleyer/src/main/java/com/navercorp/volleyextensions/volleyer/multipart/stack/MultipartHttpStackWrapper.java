package com.navercorp.volleyextensions.volleyer.multipart.stack;

import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpResponse;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.HttpStack;
import com.navercorp.volleyextensions.volleyer.multipart.MultipartContainer;
import com.navercorp.volleyextensions.volleyer.util.Assert;
/**
 * <pre>
 * A wrapper for extending a multipart feature into original {@code HttpStack}.
 * </pre>
 */
public class MultipartHttpStackWrapper implements HttpStack {

	private final HttpStack stack;
	private MultipartHttpStack multipartStack;
	/**
	 * Constructor with default multipart stack.
	 * @param stack original stack which performs for a common case.
	 */
	public MultipartHttpStackWrapper(HttpStack stack) {
		this(stack, new DefaultMultipartHttpStack());
	}
	/**
	 * Constructor with custom multipart stack.
	 * @param stack original stack which performs for a common case.
	 * @param multipartStack Multipart stack which performs for a multipart case.
	 */
	public MultipartHttpStackWrapper(HttpStack stack, MultipartHttpStack multipartStack) {
		Assert.notNull(stack, "HttpStack");
		Assert.notNull(multipartStack, "MultipartHttpStack");
		this.stack = stack;
		this.multipartStack = multipartStack;
	}
	/**
	 * <pre>
	 * Delegate a perform to {@code MultipartHttpStack} if {@code Request} has a {@code Multipart}.
	 * If not, This delegate to original {@code HttpStack}.
	 * </pre>
	 */
	@Override
	public HttpResponse performRequest(Request<?> request, Map<String, String> additionalHeaders) throws IOException, AuthFailureError {
		if (hasMultipart(request)) {
			return multipartStack.performRequest(request, additionalHeaders);
		}

		return stack.performRequest(request, additionalHeaders);
	}

	private boolean hasMultipart(Request<?> request) {
		boolean hasMultipart = false;
		if (!(request instanceof MultipartContainer)) {
			return hasMultipart;
		}

		MultipartContainer container = (MultipartContainer) request;
		if (!container.hasMultipart()) {
			return hasMultipart;
		}

		hasMultipart = true;
		return hasMultipart;
	}

}
