package com.example.testvolleyextensions;

import java.lang.ref.WeakReference;
import java.util.List;

import com.android.volley.toolbox.ImageLoader;


//import com.android.volley.toolbox.ImageLoader;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NaverShopListAdapter extends BaseAdapter {
	List<ShoppingItem> items;
	private ImageLoader loader;
	private WeakReference<Context> contextReference;
	private LayoutInflater inflater;

	private int LAYOUT_LISTVIEW_ITEM_SHOPPING = R.layout.list_item;

	public NaverShopListAdapter(Context context, List<ShoppingItem> items) {
		super();
		this.contextReference = new WeakReference<Context>(context);
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
		return getItem(position).getProductId();
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
