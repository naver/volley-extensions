package com.navercorp.volleyextensions.view;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class ImageViewZoomExtenderTest {
	
	@Test(expected=NullPointerException.class)
	public void extenderShouldthrowNpeWhenImageViewIsNull() {
		// Given
		ImageView nullImageView = null;
		// When 
		ZoomableComponent extender = new ImageViewZoomExtender(nullImageView);
	}

	@Test
	public void imageViewShouldbeMatrixTypeWhenExtenderIsCreated() {
		// Given
		ImageView imageView = new ImageView(Robolectric.application);
		// When 
		ZoomableComponent extender = new ImageViewZoomExtender(imageView);
		// Then
		assertTrue(ScaleType.MATRIX.equals(imageView.getScaleType()));
	}

	@Test
	public void zoomLevelShouldBeOriginalLevelWhenExtenderIsCreated() {
		// Given
		ImageView imageView = new ImageView(Robolectric.application);
		// When 
		ZoomableComponent extender = new ImageViewZoomExtender(imageView);
		// Then
		assertTrue(extender.getZoomLevel() == ImageViewZoomExtender.ORIGINAL_LEVEL);
	}

	@Test
	public void zoomLevelShouldBeZoomInfoLevelWhenExtenderRestores() {
		// Given
		float zoomLevel = 7.0f;
		ZoomInfo zoomInfo = new ZoomInfo(zoomLevel);
		ImageView imageView = new ImageView(Robolectric.application);
		imageView.setImageBitmap(createTestBitmap());
		ZoomableComponent extender = new ImageViewZoomExtender(imageView);
		// When
		extender.restore(zoomInfo);
		// Then
		assertTrue(extender.getZoomLevel() == zoomLevel);
	}
	
	@Test
	public void extenderShouldCheckDrawableExistsWhenExtenderRestores() {
		// Given
		float zoomLevel = 7.0f;
		ZoomInfo zoomInfo = new ZoomInfo(zoomLevel);
		ImageView imageView = mock(ImageView.class);
		imageView.setImageBitmap(createTestBitmap());
		ZoomableComponent extender = new ImageViewZoomExtender(imageView);
		// When
		extender.restore(zoomInfo);
		// Then
		verify(imageView).getDrawable();
	}

	@Test
	public void extenderShouldCheckDrawableExistsWhenExtenderZooms() {
		// Given
		float zoomLevel = 7.0f;
		float zoomX = 0.0f;
		float zoomY = 0.0f;
		ImageView imageView = mock(ImageView.class);
		imageView.setImageBitmap(createTestBitmap());
		ZoomableComponent extender = new ImageViewZoomExtender(imageView);
		// When
		extender.zoomTo(zoomLevel, zoomX, zoomY);
		// Then
		verify(imageView).getDrawable();
	}

	@Test
	public void extenderShouldCheckDrawableExistsWhenExtenderPans() {
		// Given
		float dX = 0.0f;
		float dY = 0.0f;
		ImageView imageView = mock(ImageView.class);
		imageView.setImageBitmap(createTestBitmap());
		ZoomableComponent extender = new ImageViewZoomExtender(imageView);
		// When
		extender.panTo(dX, dY);
		// Then
		verify(imageView).getDrawable();
	}

	@Test
	public void zoomLevelShouldBeTargetLevelWhenExtenderZooms() {
		// Given
		float targetLevel = 7.0f;
		float zoomX = 0.0f;
		float zoomY = 0.0f;
		ImageView imageView = new ImageView(Robolectric.application);
		imageView.setImageBitmap(createTestBitmap());
		ZoomableComponent extender = new ImageViewZoomExtender(imageView);
		// When
		extender.zoomTo(targetLevel, zoomX, zoomY);
		// Then
		assertTrue(extender.getZoomLevel() == targetLevel);
	}

	private static Bitmap createTestBitmap() {
		int[] colors = {Color.BLACK};
		int width = 300;
		int height = 300;
		android.graphics.Bitmap.Config config = android.graphics.Bitmap.Config.ARGB_8888;
		Bitmap bitmap = Bitmap.createBitmap(colors , width, height, config );
		return bitmap;
	}	
}
