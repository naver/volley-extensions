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
package com.navercorp.volleyextensions.volleyer.builder;

import java.io.File;

import com.android.volley.RequestQueue;
import com.navercorp.volleyextensions.volleyer.VolleyerConfiguration;
import com.navercorp.volleyextensions.volleyer.http.HttpMethod;
import com.navercorp.volleyextensions.volleyer.multipart.FilePart;
import com.navercorp.volleyextensions.volleyer.multipart.Part;
import com.navercorp.volleyextensions.volleyer.multipart.StringPart;
import com.navercorp.volleyextensions.volleyer.util.VolleyerLog;
/**
 * A builder for request which can contain some content such as body or a multipart.
 * {@code POST} and {@code PUT} methods can contain these. See links below,
 * 
 * @see PostBuilder
 * @see PutBuilder
 *
 * @author Wonjun Kim
 *
 */
abstract class BodyBuilder<B extends BodyBuilder<B>> extends RequestBuilder<B> {

	public BodyBuilder(RequestQueue requestQueue, VolleyerConfiguration configuration, String url, HttpMethod method) {
		super(requestQueue, configuration, url, method);
	}
	/**
	 * <pre>
	 * NOTE : Volleyer ignores body when some part is added.
	 * Do not add an part if body will not be ignored.
	 * </pre>
	 * @param body byte array of body. It does not set if body is null.
	 *
	 */
	@SuppressWarnings("unchecked")
	public B withBody(byte[] body) {
		if (isBodyNull(body)) {
			return (B) this;
		}

		httpContent.setBody(body);
		return (B) this;
	}
	/**
	 * <pre>
	 * NOTE : Volleyer ignores body when some part is added.
	 * Do not add an part if body will not be ignored.
	 * </pre>
	 * @param body string of body. It does not set if body is null.
	 *
	 */
	@SuppressWarnings("unchecked")
	public B withBody(String body) {
		if (isBodyNull(body)) {
			return (B) this;
		}

		byte[] bytes = body.getBytes();
		return withBody(bytes);
	}

	private boolean isBodyNull(Object body) {
		boolean isNull = false;

		if (body == null) {
			VolleyerLog.warn("You have to set the body which is not null.");
			isNull = true;
		}

		return isNull;
	}
	/**
	 * <pre>
	 * Add content of a file as a part of multipart.
	 * NOTE#1 : a name of this part will be set to a filename.
	 * NOTE#2 : Volleyer ignores body when some part is added.
	 * Do not add any part unless body should be ignored.
	 * </pre>
	 * @param file {@code File} object which has an actual path.
	 *
	 */
	public B addFilePart(File file) {
		return addFilePart(file.getName(), file);
	}
	/**
	 * <pre>
	 * Add content of a file as a part of multipart.
	 * NOTE : Volleyer ignores body when some part is added.
	 * Do not add any part unless body should be ignored.
	 * </pre>
	 * @param name Part name, must not be null.
	 * @param file {@code File} object which has an actual path.
	 *
	 */
	@SuppressWarnings("unchecked")
	public B addFilePart(String name, File file) {
		httpContent.addPart(new FilePart(name, file));
		return (B) this;
	}
	/**
	 * <pre>
	 * Add string as a part of multipart.
	 * NOTE : Volleyer ignores body when some part is added.
	 * Do not add any part unless body should be ignored.
	 * </pre>
	 * @param name Part name, must not be null.
	 * @param file {@code File} object which has an actual path.
	 *
	 */
	@SuppressWarnings("unchecked")
	public B addStringPart(String name, String value) {
		httpContent.addPart(new StringPart(name, value));
		return (B) this;
	}
	/**
	 * <pre>
	 * Add a part.
	 * NOTE : Volleyer ignores body when some part is added.
	 * Do not add any part unless body should be ignored.
	 * </pre>
	 * @param part {@code Part} object, must not be null.
	 *
	 */
	@SuppressWarnings("unchecked")
	public B addPart(Part part) {
		httpContent.addPart(part);
		return (B) this;
	}
}
