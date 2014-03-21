package com.navercorp.volleyextensions.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;

import com.android.volley.toolbox.NetworkImageView;

public class ZoomableNetworkImageView extends NetworkImageView implements Zoomable {
	public static final int NONE_DEF_STYLE = 0;
	private final ImageViewZoomExtender zoomExtender;

	public ZoomableNetworkImageView(Context context) {
		this(context, null);
	}

	public ZoomableNetworkImageView(Context context, AttributeSet attrs) {
		this(context, attrs, NONE_DEF_STYLE);
	}

	public ZoomableNetworkImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		zoomExtender = new ImageViewZoomExtender(this);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		initialize();
	}

	private void initialize() {
		zoomExtender.initializeIfNeeded();
		onInitialized();
	}
	/**
	 * Please override this method when you want to set any other options
	 */
	protected void onInitialized() {
	}

	protected final ImageViewZoomExtender getZoomExtender() {
		return zoomExtender;
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		zoomExtender.reset();
		super.setImageBitmap(bm);
	}

	@Override
	public void setImageResource(int resId) {
		zoomExtender.reset();
		super.setImageResource(resId);
	}

	@Override
	public void zoomTo(float targetLevel, float zoomX, float zoomY) {
		zoomExtender.zoomTo(targetLevel, zoomX, zoomY);
	}

	public void scaleTo(float dScale, float zoomX, float zoomY) {
		zoomExtender.scaleTo(dScale, zoomX, zoomY);
	}

	@Override
	public void panTo(float dx, float dy) {
		zoomExtender.panTo(dx, dy);
	}
}
