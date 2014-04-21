package com.navercorp.volleyextensions.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
/**
 * <pre>
 * A ZoomableNIV which can zoom from first level to sixth level.
 *  
 * It zooms in when single tap occurs, or zooms out when double-tap occurs. Also it supports pinch zooms.
 * </pre>
 * @author Wonjun Kim
 *
 */
public class MultiLevelSingleTapZoomNetworkImageView extends LimitedLevelZoomNetworkImageView {

	private static final float MAXIMUM_LEVEL = 6.0f;
	private static final boolean EVENT_DONE = true;
	/**
	 * The gesture detector for single taps and double taps
	 */
	private GestureDetector gestureDetector;
	/**
	 * The gesture detector for pinch zooms
	 */
	private ScaleGestureDetector scaleGestureDetector;

	public MultiLevelSingleTapZoomNetworkImageView(Context context) {
		this(context, null);
	}

	public MultiLevelSingleTapZoomNetworkImageView(Context context, AttributeSet attrs) {
		this(context, attrs, ZoomableNetworkImageView.NONE_DEF_STYLE);
	}

	public MultiLevelSingleTapZoomNetworkImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// Assign a scale gesture detector for pinch zooms
		scaleGestureDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.OnScaleGestureListener() {
			
			@Override
			public void onScaleEnd(ScaleGestureDetector detector) {
				// do nothing
			}
			
			@Override
			public boolean onScaleBegin(ScaleGestureDetector detector) {
				return EVENT_DONE;
			}

			/**
			 * Scale(zoom by distance of pinch) when a pinch occurs 
			 */
			@Override
			public boolean onScale(ScaleGestureDetector detector) {
				float zoomX = detector.getFocusX(), zoomY = detector.getFocusY();
				float dScale = detector.getScaleFactor();
				MultiLevelSingleTapZoomNetworkImageView.this.scaleTo(dScale, zoomX, zoomY);
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
			/**
			 * Zoom in when single-tapping
			 */		
			@Override
			public boolean onSingleTapConfirmed(MotionEvent event) {
				float zoomX = event.getX();
				float zoomY = event.getY();
				MultiLevelSingleTapZoomNetworkImageView.this.zoomIn(zoomX, zoomY);
				return EVENT_DONE;
			}
			
			@Override
			public boolean onDoubleTapEvent(MotionEvent event) {
				// Do nothing
				return EVENT_DONE;
			}

			/**
			 * Zoom out when double-tapping
			 */
			@Override
			public boolean onDoubleTap(MotionEvent event) {
				float zoomX = event.getX();
				float zoomY = event.getY();
				MultiLevelSingleTapZoomNetworkImageView.this.zoomOut(zoomX, zoomY);
				return EVENT_DONE;
			}
		});
	}

	@Override
	protected void onInitialized() {
	}

	/**
	 * Zoom out from one deeper level 
	 * @param zoomX X position of zoom point
	 * @param zoomY Y position of zoom point
	 */
	protected void zoomOut(float zoomX, float zoomY) {
		float newLevel = Math.round(getZoomLevel()) - 1f;
		zoomTo(newLevel, zoomX, zoomY);
	}

	/**
	 * Zoom into one deeper level 
	 * @param zoomX X position of zoom point
	 * @param zoomY Y position of zoom point
	 */
	protected void zoomIn(float zoomX, float zoomY) {
		float newLevel = Math.round(getZoomLevel()) + 1f;
		zoomTo(newLevel, zoomX, zoomY);
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