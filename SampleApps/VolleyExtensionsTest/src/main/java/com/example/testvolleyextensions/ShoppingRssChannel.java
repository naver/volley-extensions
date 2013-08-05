package com.example.testvolleyextensions;

import java.util.Arrays;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;


@Root
public class ShoppingRssChannel {
	@Element
	private String title;
	@Element
	private String link;
	@Element(required = false)
	private String description;
	@Element
	private String lastBuildDate;
	@Element
	private long total;
	@Element
	private int start;
	@Element
	private int display;

	@ElementList(inline=true)
	private List<ShoppingItem> items;

	public List<ShoppingItem> getShoppingItems() {
		return items;
	}
}