package com.navercorp.volleyextensions.view;

public interface ZoomableComponent extends Zoomable, Restorable<ZoomInfo> {
	float getZoomLevel();
}
