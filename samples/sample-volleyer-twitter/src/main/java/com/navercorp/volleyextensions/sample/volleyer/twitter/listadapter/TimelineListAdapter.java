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
package com.navercorp.volleyextensions.sample.volleyer.twitter.listadapter;

import java.util.List;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.navercorp.volleyextensions.sample.volleyer.twitter.R;
import com.navercorp.volleyextensions.sample.volleyer.twitter.application.MyApplication;
import com.navercorp.volleyextensions.sample.volleyer.twitter.client.model.Media;
import com.navercorp.volleyextensions.sample.volleyer.twitter.client.model.Status;
import com.navercorp.volleyextensions.sample.volleyer.twitter.client.model.User;
import com.navercorp.volleyextensions.util.Assert;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TimelineListAdapter extends BaseAdapter {

	private Context context;
	private List<Status> statuses;
	private ImageLoader imageLoader;

	public TimelineListAdapter(Context context, List<Status> statuses) {
		super();

		Assert.notNull(context, "Context");
		Assert.notNull(statuses, "statuses");

		this.context = context;
		this.statuses = statuses;
		this.imageLoader = MyApplication.getImageLoader();
	}

	@Override
	public int getCount() {
		return statuses.size();
	}

	@Override
	public Status getItem(int position) {
		return statuses.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View inflateView, ViewGroup parent) {
		inflateView = inflateViewIfNull(inflateView);
		ViewHolder viewHolder = getViewHolder(inflateView);

		Status status = getItem(position);

		viewHolder.userCreateDatetimeView.setText(status.getCreatedAt());

		User user = status.getUser();
		if (user != null) {
			viewHolder.userIdTextView.setText(user.getName());
			viewHolder.userImageView.setImageUrl(user.getProfileImageUrl(), imageLoader);
		}

		List<Media> medias = status.getMedias();
		if (hasMedia(medias)) {
			Media media = medias.get(0);

			viewHolder.userStatusImageView.setVisibility(View.VISIBLE);
			viewHolder.userStatusImageView.setImageUrl(media.getUrl(), imageLoader);
		} else {
			viewHolder.userStatusImageView.setVisibility(View.GONE);
			viewHolder.userStatusImageView.setImageBitmap(null);
		}

		viewHolder.userStatusTextView.setText(status.getText());
		return inflateView;
	}

	private boolean hasMedia(List<Media> medias) {
		return medias != null && medias.size() > 0;
	}

	private View inflateViewIfNull(View inflateView) {
		if (inflateView == null) {
			LayoutInflater inflater = getInflater();
			inflateView = inflater.inflate(R.layout.list_item, null /* root */);
		}

		return inflateView;
	}

	private ViewHolder getViewHolder(View inflateView) {
		ViewHolder viewHolder = (ViewHolder) inflateView.getTag();
		if (viewHolder == null) {
			viewHolder = new ViewHolder();
			viewHolder.userImageView = (NetworkImageView) inflateView.findViewById(R.id.user_image);
			viewHolder.userIdTextView = (TextView) inflateView.findViewById(R.id.user_id_text);
			viewHolder.userStatusTextView = (TextView) inflateView.findViewById(R.id.user_status_text);
			viewHolder.userStatusImageView = (NetworkImageView) inflateView.findViewById(R.id.user_status_image);
			viewHolder.userCreateDatetimeView = (TextView) inflateView.findViewById(R.id.user_create_datetime);
			inflateView.setTag(viewHolder);
		}

		return viewHolder;
	}

	private LayoutInflater getInflater() {
		return LayoutInflater.from(context);
	}

	public static class ViewHolder {
		public TextView userCreateDatetimeView;
		public NetworkImageView userStatusImageView;
		public TextView userStatusTextView;
		public TextView userIdTextView;
		public NetworkImageView userImageView;
	}
}
