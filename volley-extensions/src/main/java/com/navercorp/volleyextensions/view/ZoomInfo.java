package com.navercorp.volleyextensions.view;

import android.graphics.PointF;

import com.navercorp.volleyextensions.util.Assert;

class ZoomInfo {
	public static final float DEFAULT_ZOOM_LEVEL = 1.0f;
	public static final ZoomInfo DEFAULT_ZOOM_INFO = new ZoomInfo(DEFAULT_ZOOM_LEVEL);

	private float zoomLevel;
	private float translateX;
	private float translateY;

	public ZoomInfo() {
		this(DEFAULT_ZOOM_LEVEL);
	}

	public ZoomInfo(float zoomLevel) {
		this(zoomLevel, createDefaultTranslatePoint());
	}

	public ZoomInfo(float zoomLevel, PointF translatePoint) {
		Assert.notNull(translatePoint, "The translate point must not be null");
		setInfo(zoomLevel, translatePoint);
	}

	private void setInfo(float zoomLevel, PointF translatePoint) {
		this.zoomLevel = zoomLevel;
		this.translateX = translatePoint.x;
		this.translateY = translatePoint.y;
	}

	public void setZoomInfo(ZoomInfo zoomInfo) {
		Assert.notNull(zoomInfo, "The zoom info must not be null");
		setInfo(zoomInfo.getZoomLevel(), new PointF(zoomInfo.getTranslateX(), zoomInfo.getTranslateY()));
	}

	public void reset() {
		setZoomInfo(DEFAULT_ZOOM_INFO);
	}

	public float getZoomLevel() {
		return zoomLevel;
	}

	public float getTranslateX() {
		return translateX;
	}

	public float getTranslateY() {
		return translateY;
	}

	private static PointF createDefaultTranslatePoint() {
		return new PointF(0f, 0f);
	}
}
