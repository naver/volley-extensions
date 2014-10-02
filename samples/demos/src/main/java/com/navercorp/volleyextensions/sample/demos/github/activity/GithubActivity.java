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
package com.navercorp.volleyextensions.sample.demos.github.activity;

import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.navercorp.volleyextensions.request.Jackson2Request;
import com.navercorp.volleyextensions.request.JacksonRequest;
import com.navercorp.volleyextensions.sample.demos.R;
import com.navercorp.volleyextensions.sample.demos.application.volley.MyVolley;
import com.navercorp.volleyextensions.sample.demos.github.model.Person;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.android.volley.Response.ErrorListener;

public class GithubActivity extends Activity {
	private static final String GITHUB_SAMPLE_USER = "ncoolz";
	private static final String GET_PERSON_URL = "https://api.github.com/users/" + GITHUB_SAMPLE_USER;
	private RequestQueue requestQueue;
	private TextView personInfoTextForJackson2;
	private TextView personInfoTextForJackson;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestQueue = MyVolley.getRequestQueue();
		setContentView(R.layout.activity_github);
		personInfoTextForJackson = (TextView) findViewById(R.id.personInfoTextForJackson);
		personInfoTextForJackson2 = (TextView) findViewById(R.id.personInfoTextForJackson2);
		loadPersonInfo();
	}

	private void loadPersonInfo() {
		JacksonRequest<Person> jacksonRequest = new JacksonRequest<Person>(
				GET_PERSON_URL, Person.class, new Listener<Person>() {

					@Override
					public void onResponse(Person person) {
						personInfoTextForJackson.setText(person.toString());
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError volleyError) {
						Log.d(GithubActivity.class.getSimpleName(), ""
								+ volleyError.getMessage());
					}
				});

		Jackson2Request<Person> jackson2Request = new Jackson2Request<Person>(
				GET_PERSON_URL, Person.class, new Listener<Person>() {

					@Override
					public void onResponse(Person person) {
						personInfoTextForJackson2.setText(person.toString());
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError volleyError) {
						Log.d(GithubActivity.class.getSimpleName(), ""
								+ volleyError.getMessage());
					}
				});
		requestQueue.add(jacksonRequest);
		requestQueue.add(jackson2Request);
	}

	@Override
	protected void onStart() {
		super.onStart();

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

}
