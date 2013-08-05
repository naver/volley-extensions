package com.example.testvolleyextensions;

import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name = "rss")
public class ShoppingRssFeed {
	@Element(name = "channel")
	private ShoppingRssChannel channel;

	@Attribute
	private String version;
	
	public ShoppingRssChannel getChannel() {
		return channel;
	}


}
