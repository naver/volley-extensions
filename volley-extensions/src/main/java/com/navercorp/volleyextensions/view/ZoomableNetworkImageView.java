package com.navercorp.volleyextensions.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.util.AttributeSet;

import com.android.volley.toolbox.NetworkImageView;

public class ZoomableNetworkImageView extends NetworkImageView implements ZoomableComponent, Scalable {
	public static final int NONE_DEF_STYLE = 0;
	public static final float INFINITE_LEVEL = Float.MAX_VALUE;
	private final ZoomableComponent zoomExtender;
	private float maximumZoomLevel;
	private float minimumZoomLevel;

	public ZoomableNetworkImageView(Context context) {
		this(context, null);
	}

	public ZoomableNetworkImageView(Context context, AttributeSet attrs) {
		this(context, attrs, NONE_DEF_STYLE);
	}

	public ZoomableNetworkImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		zoomExtender = new ImageViewZoomExtender(this);
		// Initialize a limit of levels
		setMinimumZoomLevel(ImageViewZoomExtender.ORIGINAL_LEVEL);
		setMaximumZoomLevel(INFINITE_LEVEL);		
		onInitialized();
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		// NOTE: restore() and store() methods are not implemented yet, but it will be implemented.
		//       Calling restore() method with null parameter is too dangerous. So this code must be modified when implemented.
		restore(null);
	}

	@Override
	protected Parcelable onSaveInstanceState () {
		save();
		return super.onSaveInstanceState();
	}

	/**
	 * Please override this method when you want to set any other options
	 */
	protected void onInitialized() {
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		super.setImageBitmap(bm);
	}

	@Override
	public void setImageResource(int resId) {
		super.setImageResource(resId);
	}

	@Override
	public void zoomTo(float targetLevel, float zoomX, float zoomY) {
		targetLevel = limitLevelSizeRange(targetLevel);
		zoomExtender.zoomTo(targetLevel, zoomX, zoomY);
	}

	@Override
	public void scaleTo(float dScale, float zoomX, float zoomY) {
		// Convert the change in scale to a target level
		float targetLevel = dScale * getZoomLevel();
		zoomTo(targetLevel, zoomX, zoomY);
	}

	@Override
	public void panTo(float dx, float dy) {
		zoomExtender.panTo(dx, dy);
	}

	@Override
	public void restore(ZoomInfo zoomInfo) {
		zoomExtender.restore(zoomInfo);
	}

	@Override
	public ZoomInfo save() {
		return zoomExtender.save();
	}

	@Override
	public float getZoomLevel() {
		return zoomExtender.getZoomLevel();
	}

	protected final float limitLevelSizeRange(float targetLevel) {
		if (targetLevel < minimumZoomLevel) {
			targetLevel = minimumZoomLevel;
		}
		if (targetLevel > maximumZoomLevel) {
			targetLevel = maximumZoomLevel;
		}
		return targetLevel;
	}

	protected void setMaximumZoomLevel(float maximumLevel) {
		if(maximumLevel < ImageViewZoomExtender.ORIGINAL_LEVEL) {
			maximumLevel = ImageViewZoomExtender.ORIGINAL_LEVEL;
		}
		if (minimumZoomLevel > maximumLevel) {
			this.minimumZoomLevel = maximumLevel;
		}

		this.maximumZoomLevel = maximumLevel;
	}

	protected void setMinimumZoomLevel(float minimumLevel) {
		if (minimumLevel < ImageViewZoomExtender.ORIGINAL_LEVEL) {
			minimumLevel = ImageViewZoomExtender.ORIGINAL_LEVEL;
		}
		if (minimumLevel > maximumZoomLevel) {
			this.maximumZoomLevel = minimumLevel;
		}

		this.minimumZoomLevel = minimumLevel;
	}	
}
