package com.navercorp.volleyextensions.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.GestureDetector.OnGestureListener;

public class TwoLevelDoubleTapZoomNetworkImageView extends LimitedLevelZoomNetworkImageView {
	private static final int DEFAULT_ZOOM_IN_LEVEL = 2;
	private static final float DEFAULT_ZOOM_OUT_LEVEL = 1.0f;
	private static final float MAXIMUM_LEVEL = 2.0f;
	private static final boolean EVENT_DONE = true;
	private GestureDetector gestureDetector;
	private ScaleGestureDetector scaleGestureDetector;

	private boolean alreadyZoomedIn = false;

	public TwoLevelDoubleTapZoomNetworkImageView(Context context) {
		this(context, null);
	}

	public TwoLevelDoubleTapZoomNetworkImageView(Context context, AttributeSet attrs) {
		this(context, attrs, ZoomableNetworkImageView.NONE_DEF_STYLE);
	}

	public TwoLevelDoubleTapZoomNetworkImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		scaleGestureDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.OnScaleGestureListener() {
			
			@Override
			public void onScaleEnd(ScaleGestureDetector detector) {
				// do nothing
			}
			
			@Override
			public boolean onScaleBegin(ScaleGestureDetector detector) {
				return EVENT_DONE;
			}
			
			@Override
			public boolean onScale(ScaleGestureDetector detector) {
				float zoomX = detector.getFocusX(), zoomY = detector.getFocusY();
				float dScale = detector.getScaleFactor();
				TwoLevelDoubleTapZoomNetworkImageView.this.scaleTo(dScale, zoomX, zoomY);
				return EVENT_DONE;
			}
		});
		gestureDetector = new GestureDetector(context, new OnGestureListener() {

			@Override
			public boolean onDown(MotionEvent e) {
				return EVENT_DONE;
			}

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				return EVENT_DONE;
			}

			@Override
			public void onLongPress(MotionEvent e) {
				// Do nothing
			}

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
				return EVENT_DONE;
			}
		});

		gestureDetector.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
			
			@Override
			public boolean onSingleTapConfirmed(MotionEvent event) {
				return EVENT_DONE;
			}
			
			@Override
			public boolean onDoubleTapEvent(MotionEvent event) {
				return EVENT_DONE;
			}
			
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

	private float computeTargetZoomLevel() {
		float targetLevel;
		if (isAlreadyZoomedIn()) {
			targetLevel = DEFAULT_ZOOM_OUT_LEVEL;
			
		} else {
			targetLevel = DEFAULT_ZOOM_IN_LEVEL;
		}

		toggleAlreadyZoomedIn();
		return targetLevel;
	}

	@Override
	protected void onInitialized() {
	}

	@Override 
	public boolean dispatchTouchEvent(MotionEvent event) { 
		boolean isEventDone = scaleGestureDetector.onTouchEvent(event);
	    isEventDone = gestureDetector.onTouchEvent(event) || isEventDone;
	    return isEventDone || super.onTouchEvent(event);
	}

	private boolean isAlreadyZoomedIn() {
		return alreadyZoomedIn == true;
	}

	private void toggleAlreadyZoomedIn() {
		this.alreadyZoomedIn = !alreadyZoomedIn;
	}

	@Override
	protected float determineMaximumZoomLevel() {
		return MAXIMUM_LEVEL;
	}
}
