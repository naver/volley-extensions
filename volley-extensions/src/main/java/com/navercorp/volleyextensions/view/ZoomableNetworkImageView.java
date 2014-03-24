package com.navercorp.volleyextensions.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;

import com.android.volley.toolbox.NetworkImageView;

public class ZoomableNetworkImageView extends NetworkImageView implements ZoomableComponent, Scalable {
	public static final int NONE_DEF_STYLE = 0;
	public static final float INFINITE_LEVEL = Float.MAX_VALUE;
	private final ZoomableComponent zoomExtender;
	private float maximumZoomLevel;
	private float minimumZoomLevel;
	private final ZoomInfo savedZoomInfo = new ZoomInfo();
	private boolean imageChanged = false;  

	static class ZoomInfoState extends BaseSavedState {
		public static final String STATE_KEY = ZoomInfoState.class.getSimpleName();
	    private final ZoomInfo zoomInfo;

	    public ZoomInfoState(Parcelable superState, ZoomInfo zoomInfo) {
	        super(superState);
	        this.zoomInfo = zoomInfo;
	    }

	    public ZoomInfo getZoomInfo(){
	        return zoomInfo;
	    }
	}

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
		if (isNewImageLoaded()) {
			restore(savedZoomInfo);
		}
	}

	/**
	 * <pre>
	 * Check whether an image is actually changed and loaded
	 * </pre>
	 * @return true if an image is actually changed and loaded.
	 */
	private boolean isNewImageLoaded() {
		boolean isChanged = imageChanged && !isImageEmpty();
		imageChanged = false;
		return isChanged;
	}

	private boolean isImageEmpty() {
		Drawable drawable = getDrawable();
		boolean imageEmpty = true;

		if (drawable == null) {
			return imageEmpty;
		}
		if (drawable instanceof BitmapDrawable == false) {
			return imageEmpty;
		}

		Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
		if (bitmap == null) {
			return imageEmpty;
		}

		return false;
	}

	@Override
	protected Parcelable onSaveInstanceState () {
		ZoomInfo savedZoomInfo = save();
		Parcelable savedZoomInfoState = new ZoomInfoState(super.onSaveInstanceState(), savedZoomInfo);
		Bundle bundle = new Bundle();
		bundle.putParcelable(ZoomInfoState.STATE_KEY, savedZoomInfoState);
		return bundle;
	}

	@Override
	public void onRestoreInstanceState(Parcelable parcelable) {
		if (!isParceableBundle(parcelable)) {
			super.onRestoreInstanceState(BaseSavedState.EMPTY_STATE);
			return;
		}

		Bundle bundle = (Bundle) parcelable;
		ZoomInfoState savedZoomInfoState = (ZoomInfoState) bundle.getParcelable(ZoomInfoState.STATE_KEY);
		ZoomInfo savedZoomInfo = savedZoomInfoState.getZoomInfo();
		// It saves the zoom info, and do not restore immediately when onRestoreInstanceState() is called. Restoring will be called later.
		setSavedZoomInfo(savedZoomInfo);
		super.onRestoreInstanceState(savedZoomInfoState.getSuperState());
	}

	private static boolean isParceableBundle(Parcelable state) {
		return state instanceof Bundle;
	}

	private void setSavedZoomInfo(ZoomInfo savedZoomInfo) {
		this.savedZoomInfo.setZoomInfo(savedZoomInfo);
	}

	/**
	 * Please override this method when you want to set any other options
	 */
	protected void onInitialized() {
	}

	@Override
	public void setImageBitmap(Bitmap bitmap) {
		updateImageChanged(bitmap);
		super.setImageBitmap(bitmap);
	}

	private void updateImageChanged(Bitmap bitmap) {
		imageChanged = checkImageChanged(bitmap);
	}

	private boolean checkImageChanged(Bitmap bitmap) {
		Drawable drawable = getDrawable();
		boolean imageChanged = true;
		// Assume that an image is changed if current drawable is not a instance of BitmapDrawable.
		if (drawable instanceof BitmapDrawable == false) {
			return imageChanged;
		}

		Bitmap oldBitmap = ((BitmapDrawable)drawable).getBitmap();
		// Check whether the bitmaps are same instance. if true, we know that an image is not changed.
		if (oldBitmap != null && oldBitmap.equals(bitmap)) {
			imageChanged = false;
		}
		return imageChanged;
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
		resetZoomInfo();
	}

	private void resetZoomInfo() {
		savedZoomInfo.reset();
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
