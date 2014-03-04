/*
 * Copyright (C) 2014 Naver Business Platform Corp.
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
package com.example.testvolleyextensions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Person {
	@JsonProperty("openid")
	private String openid;
	@JsonProperty("nickname")
	private String nickname;
	@JsonProperty("description")
	private String description;

	@JsonProperty("location")
	private Location location;

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("openid : " + openid + "\n");
		b.append("nickname : " + getNickname() + "\n");
		b.append("descrpition" + getDescription() + "\n");
		
		if (getLocation() != null) {
			b.append("location.name : " + getLocation().name + "\n");
			b.append("location.timezone" + getLocation().timezone + "\n");
		}
		
		return b.toString();
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getNickname() {
		return nickname;
	}


	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
}
