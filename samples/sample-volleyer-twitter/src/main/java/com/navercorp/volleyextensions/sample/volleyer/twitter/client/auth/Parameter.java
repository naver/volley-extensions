package com.navercorp.volleyextensions.sample.volleyer.twitter.client.auth;

class Parameter implements Comparable<Parameter> {
	private String key;
	private String value;

	public Parameter(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public String key() {
		return key;
	}

	public String value() {
		return value;
	}

	@Override
	public int compareTo(Parameter otherParam) {
		int compared = key.compareTo(otherParam.key);

		if (compared == 0) {
			compared = value.compareTo(otherParam.value);
		}

		return compared;
	}
}
