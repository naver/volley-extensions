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
package com.navercorp.volleyextensions.sample.demos.amazon.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import android.os.Parcel;
import android.os.Parcelable;

@Root(name = "item")
public class ShoppingItem implements Parcelable {
	@Element
	private String title;
	@Element
	private String link;
	private String imageUrl;
	@Element
	private String guid;
	@Element
	private String pubDate;
	@Element
	private String description;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getImageUrl() {
		if ( imageUrl == null ) {
			imageUrl = AmazonFeedUtils.extractImageFrom(description);
		}
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getPubDate() {
		return pubDate;
	}

	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel destParcel, int flags) {
		destParcel.writeString(title);
		destParcel.writeString(link);
		destParcel.writeString(imageUrl);
		destParcel.writeString(guid);
		destParcel.writeString(pubDate);
		destParcel.writeString(description);
	}

	private static ShoppingItem createInstanceFromParcel(Parcel sourceParcel) {
		ShoppingItem item = new ShoppingItem();
		item.setTitle(sourceParcel.readString());
		item.setLink(sourceParcel.readString());
		item.setImageUrl(sourceParcel.readString());
		item.setGuid(sourceParcel.readString());
		item.setPubDate(sourceParcel.readString());
		item.setDescription(sourceParcel.readString());
		return item;
	}

	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public ShoppingItem createFromParcel(Parcel in) {
			return ShoppingItem.createInstanceFromParcel(in);
		}

		public ShoppingItem[] newArray(int size) {
			return new ShoppingItem[size];
		}
	};
}
