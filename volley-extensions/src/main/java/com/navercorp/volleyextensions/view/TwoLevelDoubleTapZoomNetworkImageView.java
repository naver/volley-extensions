package com.navercorp.volleyextensions.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.GestureDetector.OnGestureListener;
/**
 * <pre>
 * A ZoomableNIV which can zoom in/out by double-tapping.
 *  
 * The maximum zoom level is 2 on this view. Also it supports pinch zooms.
 * 
 * </pre>
 * @author Wonjun Kim
 * @author Kangsoo Kim
 *
 */
public class TwoLevelDoubleTapZoomNetworkImageView extends LimitedLevelZoomNetworkImageView {

	private static final float MAXIMUM_LEVEL = 2.0f;
	private static final boolean EVENT_DONE = true;
	/**
	 * The gesture detector for double taps
	 */
	private GestureDetector gestureDetector;
	/**
	 * The gesture detector for pinch zooms
	 */
	private ScaleGestureDetector scaleGestureDetector;

	public TwoLevelDoubleTapZoomNetworkImageView(Context context) {
		this(context, null);
	}

	public TwoLevelDoubleTapZoomNetworkImageView(Context context, AttributeSet attrs) {
		this(context, attrs, ZoomableNetworkImageView.NONE_DEF_STYLE);
	}

	public TwoLevelDoubleTapZoomNetworkImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// Assign a scale gesture detector for pinch zooms 
		scaleGestureDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.OnScaleGestureListener() {
			
			@Override
			public void onScaleEnd(ScaleGestureDetector detector) {
				// do nothing
			}
			
			@Override
			public boolean onScaleBegin(ScaleGestureDetector detector) {
				// Do nothing
				return EVENT_DONE;
			}

			/**
			 * Scale(zoom by distance of pinch) when a pinch occurs 
			 */
			@Override
			public boolean onScale(ScaleGestureDetector detector) {
				float zoomX = detector.getFocusX(), zoomY = detector.getFocusY();
				float dScale = detector.getScaleFactor();
				TwoLevelDoubleTapZoomNetworkImageView.this.scaleTo(dScale, zoomX, zoomY);
				return EVENT_DONE;
			}
		});
		// Assign a gesture detector for double taps
		gestureDetector = new GestureDetector(context, new OnGestureListener() {

			@Override
			public boolean onDown(MotionEvent e) {
				// Do nothing
				return EVENT_DONE;
			}

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				// Do nothing
				return EVENT_DONE;
			}

			@Override
			public void onLongPress(MotionEvent e) {
				// Do nothing
			}

			/**
			 * Pan when scrolling
			 */
			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2,
					float distanceX, float distanceY) {
				panTo(-distanceX, -distanceY);
				return EVENT_DONE;
			}

			@Override
			public void onShowPress(MotionEvent e) {
				// Do nothing
			}

			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				// Do nothing
				return EVENT_DONE;
			}
		});
		// Create a setting for double taps
		gestureDetector.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
			
			@Override
			public boolean onSingleTapConfirmed(MotionEvent event) {
				// Do nothing
				return EVENT_DONE;
			}
			
			@Override
			public boolean onDoubleTapEvent(MotionEvent event) {
				// Do nothing
				return EVENT_DONE;
			}

			/**
			 * Zoom when double-tapping
			 */
			@Override
			public boolean onDoubleTap(MotionEvent event) {
				float zoomX = event.getX();
				float zoomY = event.getY();

				float targetLevel = computeTargetZoomLevel();

				TwoLevelDoubleTapZoomNetworkImageView.this.zoomTo(targetLevel, zoomX, zoomY);
				return EVENT_DONE;
			}

		});
	}
	/**
	 * Switch a zoom level between 1 and 2.
	 * @return Target zoom level
	 */
	private float computeTargetZoomLevel() {
		// Toggle the zoom level between 1 and 2.
		float targetLevel = Math.round(getZoomLevel()) % 2 + 1;
		return targetLevel;
	}

	@Override
	protected void onInitialized() {
	}

	/**
	 * Binds gesture detectors and route touch events to it
	 */
	@Override 
	public boolean dispatchTouchEvent(MotionEvent event) { 
		boolean isEventDone = scaleGestureDetector.onTouchEvent(event);
	    isEventDone = gestureDetector.onTouchEvent(event) || isEventDone;
	    return isEventDone || super.onTouchEvent(event);
	}

	@Override
	protected float determineMaximumZoomLevel() {
		return MAXIMUM_LEVEL;
	}
}
