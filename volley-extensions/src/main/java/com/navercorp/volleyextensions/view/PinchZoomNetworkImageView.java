package com.navercorp.volleyextensions.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.MotionEvent;

public class PinchZoomNetworkImageView extends ZoomableNetworkImageView {
	private static final int DOUBLE_TAP_INTERVAL = 300;
	private static final int DEFAULT_ZOOM_IN_LEVEL = 2;
	private static final float DEFAULT_ZOOM_OUT_LEVEL = 1.0f;
	private static final float MAXIMUM_LEVEL = 2.0f;
	private static final boolean EVENT_DONE = true;

	private enum ZoomState {
		NONE, DRAG, ZOOM, PINCH_ZOOM;

		public boolean isNone() {
			return NONE.equals(this);
		}

		public boolean isDrag() {
			return DRAG.equals(this);
		}

		public boolean isZoom() {
			return ZOOM.equals(this);
		}

		public boolean isPinchZoom() {
			return PINCH_ZOOM.equals(this);
		}
	}

	private ZoomState currentZoomState = ZoomState.NONE;
	private ZoomState previousZoomState;

	private PointF startPoint = new PointF();
	private PointF midPoint = new PointF();
	private PointF previousPoint = new PointF();
	private float pinchDistance = 1.0f;

	private long timeStamp;
	private boolean alreadyZoomedIn = false;

	public PinchZoomNetworkImageView(Context context) {
		this(context, null);
	}

	public PinchZoomNetworkImageView(Context context, AttributeSet attrs) {
		this(context, attrs, ZoomableNetworkImageView.NONE_DEF_STYLE);
	}

	public PinchZoomNetworkImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onInitialized() {
		getZoomExtender().updateMaximumZoomLevel(MAXIMUM_LEVEL);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:
				savePointAsStart(event);
				savePointAsPrevious(event);

				setState(ZoomState.DRAG);

				if (isEventDoubleTap()) {
					zoomOnDoubleTap();
					resetCurrentTimeStamp();
					return EVENT_DONE;
				}

				saveCurrentTimeStamp();
				break;

			case MotionEvent.ACTION_POINTER_DOWN:
				savePinchDistance(event);

				if (canStartZooming(pinchDistance)) {
					saveMidPoint(event);
					setState(ZoomState.PINCH_ZOOM);
				}
				break;

			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
				setState(ZoomState.NONE);
				break;

			case MotionEvent.ACTION_MOVE:
				if (currentZoomState.isDrag()) {
					float dx = event.getX() - previousPoint.x;
					float dy = event.getY() - previousPoint.y;
					panTo(dx, dy);
					savePointAsPrevious(event);
				}
				else if (currentZoomState.isPinchZoom()) {
					float newPinchDistance = computePinchDistance(event);

					if (canStartZooming(newPinchDistance)) {
						float dScale = prepareScale(newPinchDistance);
						scaleTo(dScale, midPoint.x, midPoint.y);
						saveMidPoint(event);
						savePinchDistance(newPinchDistance);
					}
				}
				break;
		}
 
		return EVENT_DONE;
	}

	private void savePinchDistance(float newDistance) {
		pinchDistance = newDistance;
	}

	private boolean canStartZooming(float newDistance) {
		return newDistance > 10f;
	}

	private void savePinchDistance(MotionEvent event) {
		pinchDistance = computePinchDistance(event);
	}

	private float computePinchDistance(MotionEvent event) {
		float distanceX = event.getX(0) - event.getX(1);
		float distanceY = event.getY(0) - event.getY(1);
		float newDistance = computeDistance(distanceX, distanceY);
		return newDistance;
	}

	@SuppressLint("FloatMath")
	private float computeDistance(float x, float y) {
		return FloatMath.sqrt(x * x + y * y);
	}

	private void savePointAsStart(MotionEvent event) {
		startPoint.set(event.getX(), event.getY());
	}

	private void savePointAsPrevious(MotionEvent event) {
		previousPoint.set(event.getX(), event.getY());
	}

	private void resetCurrentTimeStamp() {
		timeStamp = 0;
	}

	private void saveCurrentTimeStamp() {
		timeStamp = System.currentTimeMillis();
	}
 
	private void saveMidPoint(MotionEvent event) {
		float firstPointX = event.getX(0), secondPointX = event.getX(1);
		float firstPointY = event.getY(0), secondPointY = event.getY(1);
		midPoint.set((firstPointX + secondPointX) / 2, (firstPointY + secondPointY) / 2);
	}

	private float prepareScale(float newPinchDistance) {
		float scale = newPinchDistance / pinchDistance;
		if (scale > 1.0f) {
			setAlreadyZoomedIn(true);
		}
		return scale;
	}

	private void setState(ZoomState nextZoomState) {
		currentZoomState = nextZoomState;
	}

	private void saveState() {
		previousZoomState = currentZoomState;
	}

	private void restoreState() {
		currentZoomState = previousZoomState;
	}
	/**
	 * @return true if it has a short term between taps.
	 */
	private boolean isEventDoubleTap() {
		boolean isDoubleTapped = false;
		long newTimeStamp = System.currentTimeMillis();
		if (newTimeStamp - timeStamp < DOUBLE_TAP_INTERVAL) {
			isDoubleTapped = true;
		}
		return isDoubleTapped;
	}
 
	protected void zoomOnDoubleTap() {
		saveState();
		setState(ZoomState.ZOOM);
		float targetLevel;
		if (isAlreadyZoomedIn()) {
			// Reset image to original size if it's already zoomed in
			setAlreadyZoomedIn(false);
			targetLevel = DEFAULT_ZOOM_OUT_LEVEL;
		} else {
			setAlreadyZoomedIn(true);
			targetLevel = DEFAULT_ZOOM_IN_LEVEL;
		}

		zoomTo(targetLevel, startPoint.x, startPoint.y);
		restoreState();
	}

	private boolean isAlreadyZoomedIn() {
		return alreadyZoomedIn == true;
	}

	private void setAlreadyZoomedIn(boolean alreadyZoomedIn) {
		this.alreadyZoomedIn = alreadyZoomedIn;
	}
}
