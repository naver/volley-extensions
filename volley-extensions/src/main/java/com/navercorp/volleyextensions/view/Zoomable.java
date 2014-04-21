package com.navercorp.volleyextensions.view;
/**
 * An Interface which has a feature zooming by a target level
 */
public interface Zoomable {
	void zoomTo(float targetLevel, float zoomX, float zoomY);
	void panTo(float dx, float dy);
}
