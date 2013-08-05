package com.example.testvolleyextensions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
class Location {
	@JsonProperty("name")
	public String name;
	@JsonProperty("timezone")
	public String timezone;
}