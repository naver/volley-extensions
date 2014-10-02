package com.navercorp.volleyextensions.sample.volleyer.twitter.client.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Timeline {

	private List<Status> statuses = new ArrayList<Status>();

	public Timeline(List<Status> statuses) {
		this.statuses.addAll(statuses);
	}

	public List<Status> getStatuses() {
		return Collections.unmodifiableList(statuses);
	}
}
