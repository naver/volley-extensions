package com.navercorp.volleyextensions.view;

import android.content.Context;
import android.util.AttributeSet;
/**
 * <pre>
 * A ZoomableNIV which has a feature limiting zoom levels.
 * You have to override {@link #determineMaximumZoomLevel()} method, and define the maxmimum zoom level in it.
 * And, you can set mimimum zoom level by overriding {@link #determineMinimumZoomLevel()} if it is needed.
 * 
 * You can set a minimum level and maximum level only by overriding those methods.
 * 
 * </pre>
 * @author Wonjun Kim
 *  
 * @see ZoomableNetworkImageView
 * @see LimitedLevelZoomableComponent
 *
 */
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
		// Decorate the zoomableComponent with LimitedLevelZoomableComponent
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
