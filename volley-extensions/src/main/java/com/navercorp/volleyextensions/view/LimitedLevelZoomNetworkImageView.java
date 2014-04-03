package com.navercorp.volleyextensions.view;

import android.content.Context;
import android.util.AttributeSet;

public abstract class LimitedLevelZoomNetworkImageView extends ZoomableNetworkImageView {
	public LimitedLevelZoomNetworkImageView(Context context) {
		this(context, null);
	}

	public LimitedLevelZoomNetworkImageView(Context context, AttributeSet attrs) {
		this(context, attrs, NONE_DEF_STYLE);
	}

	public LimitedLevelZoomNetworkImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void setZoomExtender(ZoomableComponent zoomableComponent) {
		ZoomableComponent limitedLevelZoomableComponent = new LimitedLevelZoomableComponent(zoomableComponent,
																							determineMinimumZoomLevel(),
																							determineMaximumZoomLevel());
		super.setZoomExtender(limitedLevelZoomableComponent);
	}

	protected float determineMinimumZoomLevel() {
		return LimitedLevelZoomableComponent.ORIGINAL_LEVEL;
	}

	protected abstract float determineMaximumZoomLevel();
}
