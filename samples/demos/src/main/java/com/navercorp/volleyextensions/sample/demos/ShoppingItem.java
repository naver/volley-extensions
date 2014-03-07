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
package com.navercorp.volleyextensions.sample.demos;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import android.os.Parcel;
import android.os.Parcelable;

@Root(name = "item")
public class ShoppingItem implements Parcelable {

	private static final String TAG_STRING_REGEX = "<[A-Za-z /=\"\']+>";

	public static final long ITEM_NOT_ASSIGNED_IN_BASKET = -1;

	@Element(name = "title")
	private String title;
	@Element(name = "link")
	private String linkUrl;
	@Element(name = "image")
	private String imageUrl;
	@Element(name = "lprice")
	private int lowPrice;
	@Element(name = "hprice")
	private int highPrice;
	@Element(name = "mallName")
	private String mallName;
	@Element
	private long productId;
	@Element
	private int productType;

	private String formatedLowPrice;
	private String formatedHighPrice;

	private boolean isPlainTitle = false;

	private long basketId = ITEM_NOT_ASSIGNED_IN_BASKET;

	private boolean hasFormatedLowPrice;

	private boolean hasFormatedHighPrice;

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public String getTitle() {
		updateIfTitleNotPlain();

		return title;
	}

	private synchronized void updateIfTitleNotPlain() {
		if (isPlainTitle == false) {
			setTitle(getPlainString(title));
			isPlainTitle = true;
		}
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public int getLowPrice() {
		updateIfLowPriceNotFormated();
		return lowPrice;
	}

	private synchronized void updateIfLowPriceNotFormated() {
		if (hasFormatedLowPrice == false) {
			setFormatedLowPrice(lowPrice);
			hasFormatedLowPrice = true;
		}
	}

	private synchronized void updateIfHighPriceNotFormated() {
		if (hasFormatedHighPrice == false) {
			setFormatedHighPrice(highPrice);
			hasFormatedHighPrice = true;
		}
	}

	public void setLowPrice(int lowPrice) {
		this.lowPrice = lowPrice;

	}

	public int getHighPrice() {
		updateIfHighPriceNotFormated();
		return highPrice;
	}

	public void setHighPrice(int highPrice) {
		this.highPrice = highPrice;
	}

	public String getMallName() {
		return mallName;
	}

	public void setMallName(String mallName) {
		this.mallName = mallName;
	}

	public int getProductType() {
		return productType;
	}

	public void setProductType(int productType) {
		this.productType = productType;
	}

	public long getBasketId() {
		return basketId;
	}

	public void setBasketId(long basketId) {
		this.basketId = basketId;
	}

	public boolean isSavedInBasket() {
		return (ITEM_NOT_ASSIGNED_IN_BASKET != getBasketId());
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel destParcel, int flags) {
		destParcel.writeString(title);
		destParcel.writeString(linkUrl);
		destParcel.writeString(imageUrl);
		destParcel.writeInt(lowPrice);
		destParcel.writeInt(highPrice);
		destParcel.writeString(mallName);
		destParcel.writeLong(productId);
		destParcel.writeInt(productType);

	}

	private static ShoppingItem createInstanceFromParcel(Parcel sourceParcel) {
		ShoppingItem item = new ShoppingItem();
		item.setTitle(sourceParcel.readString());
		item.setLinkUrl(sourceParcel.readString());
		item.setImageUrl(sourceParcel.readString());
		item.setLowPrice(sourceParcel.readInt());
		item.setHighPrice(sourceParcel.readInt());
		item.setMallName(sourceParcel.readString());
		item.setProductId(sourceParcel.readLong());
		item.setProductType(sourceParcel.readInt());
		return item;
	}

	public String getFormatedLowPrice() {
		updateIfLowPriceNotFormated();
		return formatedLowPrice;
	}

	private void setFormatedLowPrice(int lowPrice) {
		setFormatedLowPrice(String.format("%,3d", lowPrice));
	}

	private void setFormatedLowPrice(String formatedLowPrice) {
		this.formatedLowPrice = formatedLowPrice;
	}

	public String getFormatedHighPrice() {
		updateIfHighPriceNotFormated();
		return formatedHighPrice;
	}

	private void setFormatedHighPrice(int highPrice) {
		setFormatedHighPrice(String.format("%,3d", highPrice));
	}

	private void setFormatedHighPrice(String formatedHighPrice) {
		this.formatedHighPrice = formatedHighPrice;
	}

	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public ShoppingItem createFromParcel(Parcel in) {
			return ShoppingItem.createInstanceFromParcel(in);
		}

		public ShoppingItem[] newArray(int size) {
			return new ShoppingItem[size];
		}
	};

	private static String getPlainString(String dirtyString) {
		return dirtyString.replaceAll(TAG_STRING_REGEX, "");

	}
}
