package com.navercorp.volleyextensions.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

public class MultiLevelZoomNetworkImageView extends ZoomableNetworkImageView {

	private static final float MAXIMUM_LEVEL = 6.0f;
	private static final boolean EVENT_DONE = true;
	private GestureDetector gestureDetector;
	private ScaleGestureDetector scaleGestureDetector;

	public MultiLevelZoomNetworkImageView(Context context) {
		this(context, null);
	}

	public MultiLevelZoomNetworkImageView(Context context, AttributeSet attrs) {
		this(context, attrs, ZoomableNetworkImageView.NONE_DEF_STYLE);
	}

	public MultiLevelZoomNetworkImageView(Context context, AttributeSet attrs, int defStyle) {
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
				MultiLevelZoomNetworkImageView.this.scaleTo(dScale, zoomX, zoomY);
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
				float zoomX = event.getX();
				float zoomY = event.getY();
				MultiLevelZoomNetworkImageView.this.zoomIn(zoomX, zoomY);
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
				MultiLevelZoomNetworkImageView.this.zoomOut(zoomX, zoomY);
				return EVENT_DONE;
			}
		});
	}

	@Override
	protected void onInitialized() {
		getZoomExtender().updateMaximumZoomLevel(MAXIMUM_LEVEL);
	}

	protected void zoomOut(float zoomX, float zoomY) {
		float newLevel = Math.round(getZoomExtender().getZoomLevel()) - 1f;
		zoomTo(newLevel, zoomX, zoomY);
	}

	protected void zoomIn(float zoomX, float zoomY) {
		float newLevel = Math.round(getZoomExtender().getZoomLevel()) + 1f;
		zoomTo(newLevel, zoomX, zoomY);
	}

	@Override 
	public boolean dispatchTouchEvent(MotionEvent event) { 
		boolean isEventDone = scaleGestureDetector.onTouchEvent(event);
	    isEventDone = gestureDetector.onTouchEvent(event) || isEventDone;
	    return isEventDone || super.onTouchEvent(event);
	} 
}