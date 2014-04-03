package com.navercorp.volleyextensions.view;

import com.navercorp.volleyextensions.util.Assert;

class LimitedLevelZoomableComponent implements ZoomableComponent {

	public static final float ORIGINAL_LEVEL = 1.0f;
	public static final float INFINITE_LEVEL = Float.MAX_VALUE;

	private final ZoomableComponent delegate;
	private float maximumZoomLevel;
	private float minimumZoomLevel;

	public LimitedLevelZoomableComponent(ZoomableComponent delegate, float minimumZoomLevel, float maximumZoomLevel) {
		Assert.notNull(delegate, "The delegated ZoomableComponent");
		setMinimumZoomLevel(minimumZoomLevel);
		setMaximumZoomLevel(maximumZoomLevel);
		this.delegate = delegate;
	}

	@Override
	public void zoomTo(float targetLevel, float zoomX, float zoomY) {
		targetLevel = limitLevelSizeRange(targetLevel);
		delegate.zoomTo(targetLevel, zoomX, zoomY);
	}

	@Override
	public void panTo(float dx, float dy) {
		delegate.panTo(dx, dy);
	}

	@Override
	public void restore(ZoomInfo value) {
		delegate.restore(value);
	}

	@Override
	public ZoomInfo save() {
		return delegate.save();
	}

	@Override
	public float getZoomLevel() {
		return delegate.getZoomLevel();
	}

	private final float limitLevelSizeRange(float targetLevel) {
		if (targetLevel < minimumZoomLevel) {
			targetLevel = minimumZoomLevel;
		}
		if (targetLevel > maximumZoomLevel) {
			targetLevel = maximumZoomLevel;
		}
		return targetLevel;
	}

	private void setMinimumZoomLevel(float minimumLevel) {
		if (minimumLevel < ORIGINAL_LEVEL) {
			minimumLevel = ORIGINAL_LEVEL;
		}
		if (minimumLevel > maximumZoomLevel) {
			this.maximumZoomLevel = minimumLevel;
		}

		this.minimumZoomLevel = minimumLevel;
	}

	private void setMaximumZoomLevel(float maximumLevel) {
		if(maximumLevel < ORIGINAL_LEVEL) {
			maximumLevel = ORIGINAL_LEVEL;
		}
		if (minimumZoomLevel > maximumLevel) {
			this.minimumZoomLevel = maximumLevel;
		}

		this.maximumZoomLevel = maximumLevel;
	}
}
