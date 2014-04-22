package com.navercorp.volleyextensions.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;

import com.android.volley.toolbox.NetworkImageView;
/**
 * <pre>
 * A NetworkImageView which can zoom in/out the image.
 * 
 * ZoomableNetworkImageView just supports zoom APIs only as methods.
 * So you have to override this class and bind events to APIs if you want that ZoomableNIV interacts with UI. 
 * </pre>
 * 
 * @author Wonjun Kim
 *
 */
public class ZoomableNetworkImageView extends NetworkImageView implements ZoomableComponent, Scalable {
	public static final int NONE_DEF_STYLE = 0;
	/**
	 * ZoomableComponent enabling ImageView to zoom in/out.
	 */
	private ZoomableComponent zoomExtender;
	/**
	 * ZoomInfo to be restored or saved.
	 */
	private final ZoomInfo savedZoomInfo = new ZoomInfo();
	/**
	 * The flag for checking whether an image in this view actually has been changed.
	 */
	private boolean imageChanged = false;  
	/**
	 * A listener for receiving that an image is changed.
	 */
	private OnImageChangedListener onImageChangedListener = null;
	/**
	 * <pre>
	 * {@code SavedState} for restoring or saving a zoom information.
	 * 
	 * (I don't like that this heavy class has to be used only for that case.)
	 * </pre> 
	 */
	static class ZoomInfoState extends BaseSavedState {
		public static final String STATE_KEY = ZoomInfoState.class.getSimpleName();
		/**
		 * Embedded ZoomInfo
		 */
	    private final ZoomInfo zoomInfo;

	    public ZoomInfoState(Parcelable superState, ZoomInfo zoomInfo) {
	        super(superState);
	        // Save a ZoomInfo
	        this.zoomInfo = zoomInfo;
	    }

	    public ZoomInfo getZoomInfo(){
	        return zoomInfo;
	    }
	}
	/**
	 * Default Constructors
	 */
	public ZoomableNetworkImageView(Context context) {
		this(context, null);
	}

	public ZoomableNetworkImageView(Context context, AttributeSet attrs) {
		this(context, attrs, NONE_DEF_STYLE);
	}

	public ZoomableNetworkImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// Initialize ZoomExtender
		setZoomExtender(new ImageViewZoomExtender(this));
		onInitialized();
	}

	/**
	 * <pre>
	 * Set {@code zoomExtender} which is enabling ImageView to zoom.
	 * 
	 * You can override this method and decorate or alter the ZoomableComponent.
	 * </pre>
	 * @param zoomableComponent
	 */
	protected void setZoomExtender(ZoomableComponent zoomableComponent) {
		zoomExtender = zoomableComponent;
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		// Restore a zoom info when an image has been changed.
		if (isNewImageLoaded()) {
			restore(savedZoomInfo);
		}
	}

	/**
	 * <pre>
	 * Check whether an image is actually changed and loaded.
	 * 
	 * NOTE : this method causes a side effect because it consumes {@code imageChanged} by setting the flag to false. 
	 * </pre>
	 * @return true if an image is actually changed and loaded.
	 */
	private boolean isNewImageLoaded() {
		boolean isChanged = imageChanged && !isImageEmpty();
		// Set imageChanged to false.
		imageChanged = false;
		return isChanged;
	}
	/**
	 * Check (very detailedly) whether an image in this view is empty.
	 * @return true if it is empty
	 */
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
	/**
	 * Save a zoom information
	 */
	@Override
	protected Parcelable onSaveInstanceState () {
		ZoomInfo savedZoomInfo = save();
		Parcelable savedZoomInfoState = new ZoomInfoState(super.onSaveInstanceState(), savedZoomInfo);
		Bundle bundle = new Bundle();
		bundle.putParcelable(ZoomInfoState.STATE_KEY, savedZoomInfoState);
		return bundle;
	}
	/**
	 * Restore a zoom information
	 */
	@Override
	public void onRestoreInstanceState(Parcelable parcelable) {
		// If the parcelable is invalid, then it doesn't restore a zoom info.
		if (!isParceableBundle(parcelable)) {
			super.onRestoreInstanceState(BaseSavedState.EMPTY_STATE);
			return;
		}
		// Restore from the parcelable
		Bundle bundle = (Bundle) parcelable;
		ZoomInfoState savedZoomInfoState = (ZoomInfoState) bundle.getParcelable(ZoomInfoState.STATE_KEY);
		// Get saved ZoomInfo from the SavedState 
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
	/**
	 * <pre>
	 * <b>WARNING!</b>
	 * Don't call this method directly. 
	 * This method doesn't work normally because {@link NetworkImageView} was made with some bugs. 
	 * Instead you better call {@link #setImageUrl(String, com.android.volley.toolbox.ImageLoader)}.
	 * </pre>
	 */
	@Override
	public void setImageBitmap(Bitmap bitmap) {
		updateImageChanged(bitmap);
		super.setImageBitmap(bitmap);
		callListenerIfImageChanged();
	}

	private void callListenerIfImageChanged() {
		// Call a listener only when image is actually changed and listener exists 
		if (imageChanged == true && onImageChangedListener != null) {
			// Don't call a listener immediately, because needed ui processes may remains before listener is called.
			post(new Runnable(){
				@Override
				public void run() {
					onImageChangedListener.onImageChanged(ZoomableNetworkImageView.this);
				}});
		}
	}

	/**
	 * Turn on imageChanged flag if an image is changed to new.
	 * @param bitmap
	 */
	private void updateImageChanged(Bitmap bitmap) {
		imageChanged = checkImageChanged(bitmap);
	}
	/**
	 * Check whether {@code bitmap} is new and different from currently contained bitmap.
	 * @param bitmap
	 * @return true if {@code bitmap} is new.
	 */
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

	/**
	 * Assign OnImageChangedListener to receive an event of which an image is changed.
	 */
	public void setOnImageChangedListener(OnImageChangedListener listener) {
		this.onImageChangedListener = listener;
	}

	public void removeOnImageChangedListener() {
		this.onImageChangedListener = null;
	}

	@Override
	public void setImageResource(int resId) {
		super.setImageResource(resId);
	}

	@Override
	public void zoomTo(float targetLevel, float zoomX, float zoomY) {
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
	/**
	 * Interface that is called when an image in {@code ZoomableImageView} is changed.
	 */
	public interface OnImageChangedListener {
		public void onImageChanged(ZoomableNetworkImageView zoomableImageView);
	}
}
