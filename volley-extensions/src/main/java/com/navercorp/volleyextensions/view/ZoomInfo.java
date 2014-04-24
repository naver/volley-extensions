package com.navercorp.volleyextensions.view;

import android.graphics.PointF;

import com.navercorp.volleyextensions.util.Assert;
/**
 * Model class containing a zoom level and a translate point.
 * 
 * NOTE : {@code ZoomInfo} has default properties. This can be reset to the default.
 */
class ZoomInfo {
	public static final float DEFAULT_ZOOM_LEVEL = 1.0f;
	public static final ZoomInfo DEFAULT_ZOOM_INFO = new ZoomInfo(DEFAULT_ZOOM_LEVEL);

	private float zoomLevel;
	private float translateX;
	private float translateY;
	/**
	 * Construct by default settings.
	 */
	public ZoomInfo() {
		this(DEFAULT_ZOOM_LEVEL);
	}
	/**
	 * Construct by including a zoom level.
	 * @param zoomLevel
	 */
	public ZoomInfo(float zoomLevel) {
		this(zoomLevel, createDefaultTranslatePoint());
	}
	/**
	 * Construct by including a zoom level and a translate point.
	 * @param zoomLevel
	 * @param translatePoint
	 */
	public ZoomInfo(float zoomLevel, PointF translatePoint) {
		Assert.notNull(translatePoint, "The translate point must not be null");
		setInfo(zoomLevel, translatePoint);
	}
	/**
	 * Set all properties of ZoomInfo.
	 * @param zoomLevel
	 * @param translatePoint
	 */
	private void setInfo(float zoomLevel, PointF translatePoint) {
		this.zoomLevel = zoomLevel;
		setTranslatePoint(translatePoint);
	}

	private void setTranslatePoint(PointF translatePoint) {
		if (isPointInvalid(translatePoint)) {
			// set to default point
			translatePoint = createDefaultTranslatePoint();
		}
		this.translateX = translatePoint.x;
		this.translateY = translatePoint.y;
	}

	/**
	 * Check whether translate point contains NaN
	 * @param translatePoint
	 * @return true if the point contains NaN
	 */
	private boolean isPointInvalid(PointF translatePoint) {
		return Float.isNaN(translatePoint.x) || Float.isNaN(translatePoint.y);
	}

	/**
	 * Copy all of ZoomInfo into this object.
	 *  
	 * @param zoomInfo ZoomInfo instance to be copied
	 */
	public void setZoomInfo(ZoomInfo zoomInfo) {
		Assert.notNull(zoomInfo, "The zoom info must not be null");
		setInfo(zoomInfo.getZoomLevel(), new PointF(zoomInfo.getTranslateX(), zoomInfo.getTranslateY()));
	}
	/**
	 * Reset to default zoom info.
	 */
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
