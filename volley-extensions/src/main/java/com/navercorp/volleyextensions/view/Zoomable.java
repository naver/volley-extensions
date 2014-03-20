package com.navercorp.volleyextensions.view;

public interface Zoomable {
	void zoomTo(float targetLevel, float zoomX, float zoomY);
	void panTo(float dx, float dy);
}
