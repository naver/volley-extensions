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
package com.navercorp.volleyextensions.sample.demos.amazon.listview;

import java.util.List;

import com.android.volley.toolbox.ImageLoader;
import com.navercorp.volleyextensions.sample.demos.R;
import com.navercorp.volleyextensions.sample.demos.R.id;
import com.navercorp.volleyextensions.sample.demos.R.layout;
import com.navercorp.volleyextensions.sample.demos.amazon.model.ShoppingItem;
import com.navercorp.volleyextensions.sample.demos.application.volley.MyVolley;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AmazonListAdapter extends BaseAdapter {
	List<ShoppingItem> items;
	private ImageLoader loader;
	private LayoutInflater inflater;

	private int LAYOUT_LISTVIEW_ITEM_SHOPPING = R.layout.list_item;

	public AmazonListAdapter(Context context, List<ShoppingItem> items) {
		super();
		this.inflater = LayoutInflater.from(context);
		this.items = items;
		
		loader = MyVolley.getImageLoader();
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public ShoppingItem getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = getInflatedView();
			setContentToHolder(convertView, holder);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		ShoppingItem item = getItem(position);
		holder.title.setText(item.getTitle());
		holder.itemImage.setImageBitmap(null);
		loader.get(item.getImageUrl(),
				ImageLoader.getImageListener(holder.itemImage, 0, 0), 30, 30);
		return convertView;
	}

	protected void setContentToHolder(View convertView, ViewHolder holder) {
		holder.itemImage = (ImageView) convertView.findViewById(R.id.itemImage);
		holder.title = (TextView) convertView.findViewById(R.id.itemTitle);
	}

	protected View getInflatedView() {
		return (View) inflater.inflate(getItemLayoutId(), null);
	}

	protected int getItemLayoutId() {
		return LAYOUT_LISTVIEW_ITEM_SHOPPING;
	}
}
