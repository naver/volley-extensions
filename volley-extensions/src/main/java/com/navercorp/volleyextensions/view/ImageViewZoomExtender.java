/*
 * Copyright (C) 2014 Naver Corp.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.navercorp.volleyextensions.view;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.navercorp.volleyextensions.util.Assert;
/**
 * <pre>
 * A decorator(extender) for enabling {@code ImageView} to zoom in/out. 
 * 
 * This class is a basic {@code ZoomableComponent} as a decorator for ImageView. 
 * 
 * If you want to extend features of zoom on this class, 
 * I recommend you to create an another extender for {@code ZoomableComponent} instead of overriding it.
 * 
 * NOTE : Some codes about matrix calculations are extracted from pieces on internet.
 * I don't know where the origin of the codes is. Please someone let me know the origin.   
 * </pre>
 * 
 * @author Wonjun Kim
 * @author Kangsoo Kim
 *
 * @see ZoomableComponent
 * @see ZoomableNetworkImageView
 * 
 */
class ImageViewZoomExtender implements ZoomableComponent {
	public static final int DEFAULT_MATRIX_SIZE = 3 * 3;
	public static final int NONE_DEF_STYLE = 0;
	public static final float ORIGINAL_LEVEL = 1.0f;
	private static final PointF topLeftPoint = new PointF(0.0f, 0.0f);

	private float zoomLevel = ORIGINAL_LEVEL;
	private ImageView imageView;
	private final Matrix currentMatrix = new Matrix();
	private float initialScaleSize;

	private int viewWidth;
	private int viewHeight;
	private int imageWidth;
	private int imageHeight;

	public ImageViewZoomExtender(ImageView imageView) {
		Assert.notNull(imageView, "ImageView");
		setImageView(imageView);
		setScaleTypeMatrix();
	}

	private void setImageView(ImageView imageView) {
		this.imageView = imageView;
	}

	private void setScaleTypeMatrix() {
		imageView.setScaleType(ScaleType.MATRIX);
	}
	/**
	 * Return a current zoom level
	 */
	@Override
	public float getZoomLevel() {
		return zoomLevel;
	}
	/**
	 * Restore a zoom info from the {@code zoomInfo} parameter
	 */
	@Override
	public void restore(ZoomInfo zoomInfo) {
		if (isImageEmpty()) {
			return;
		}

		zoomInfo = createZoomInfoIfNull(zoomInfo);

		resetZoomLevel();
		resetImageMatrix();
		saveCurrentSizes();

		initializeScaleSize();

		restoreActualZoomPosition(zoomInfo);
	}

	private static ZoomInfo createZoomInfoIfNull(ZoomInfo zoomInfo) {
		if (zoomInfo == null) {
			zoomInfo = new ZoomInfo();
		}
		return zoomInfo;
	}

	private void resetZoomLevel() {
		updateZoomLevel(ORIGINAL_LEVEL);
	}

	private void resetImageMatrix() {
		currentMatrix.reset();
		// Reset imageView's matrix too
		imageView.setImageMatrix(currentMatrix);
	}
	/**
	 * Get sizes of an image and {@code ImageView} 
	 */
	private void saveCurrentSizes() {
		if(isImageEmpty()) {
			return;
		}

		Drawable drawable = imageView.getDrawable();
		viewWidth = imageView.getMeasuredWidth();
		viewHeight = imageView.getMeasuredHeight();
		imageWidth = drawable.getIntrinsicWidth();
		imageHeight = drawable.getIntrinsicHeight();
	}
	/**
	 * Set a scale size of when zoom level is {@code ORIGINAL_SIZE} 
	 */
	private void initializeScaleSize() {
		if (isImageSizeLargerThanView()) {
			initialScaleSize = computeFitScaleOfImage();
		} else {
			initialScaleSize = ORIGINAL_LEVEL;
		}
		
		scaleSize(currentMatrix, initialScaleSize, 0,0);
	}

	private boolean isImageSizeLargerThanView() {
		return (viewWidth < imageWidth || viewHeight < imageHeight);
	}

	private float computeFitScaleOfImage() {
		float xRatio = (float)viewWidth / (float)imageWidth;
		float yRatio = (float)viewHeight / (float)imageHeight;

		// Math.min fits the image to the shorter axis. (with letterboxes around)
		// Math.max fits the image to the longer axis. (with the other axis cropped)
		return Math.min(xRatio, yRatio);
	}
	/**
	 * Update a zoom level and a translate point from the {@code zoomInfo} parameter 
	 * @param zoomInfo
	 */
	private void restoreActualZoomPosition(ZoomInfo zoomInfo) {
		float oldZoomLevel = zoomInfo.getZoomLevel();
		float oldTranslateX = zoomInfo.getTranslateX();
		float oldTranslateY = zoomInfo.getTranslateY();

		float targetLevel = oldZoomLevel;
		float targetTranslateX = oldTranslateX * initialScaleSize;
		float targetTranslateY = oldTranslateY * initialScaleSize;

		zoomTo(targetLevel);
		panTo(targetTranslateX, targetTranslateY);
	}

	@Override
	public ZoomInfo save() {
		return createCurrentZoomInfo();
	}

	private ZoomInfo createCurrentZoomInfo() {
		float[] values = getValuesOfMatrix(currentMatrix);
		return new ZoomInfo(zoomLevel,
							new PointF(values[Matrix.MTRANS_X] / initialScaleSize,
									   values[Matrix.MTRANS_Y] / initialScaleSize));
	}

	public void zoomTo(float targetLevel) {
		zoomTo(targetLevel, topLeftPoint);
	}

	public void zoomTo(float targetLevel, PointF zoomPoint) {
		Assert.notNull(zoomPoint, "The zoom point");
		zoomTo(targetLevel, zoomPoint.x, zoomPoint.y);
	}

	@Override
	public void zoomTo(float targetLevel, float zoomX, float zoomY) {
		if(isImageEmpty()) {
			return;
		}

		scaleSize(currentMatrix, targetLevel, zoomX, zoomY);
		boundArea(currentMatrix);
		centerImage(currentMatrix);
		setImageMatrix(currentMatrix);
		updateZoomLevel(targetLevel);
	}

	private void updateZoomLevel(float targetLevel) {
		zoomLevel = targetLevel;
	}

	private void scaleSize(Matrix matrix, float targetLevel, float zoomX, float zoomY) {
		float newScale = targetLevel / zoomLevel;
		matrix.postScale(newScale, newScale, zoomX, zoomY);
	}
	/**
	 * Let an image always be on center of view
	 * @param matrix
	 */
	private void centerImage(Matrix matrix) {
		float[] values = getValuesOfMatrix(matrix);
		float scaledImageWidth = imageWidth * values[Matrix.MSCALE_X];
		float scaledImageHeight = imageHeight * values[Matrix.MSCALE_Y];
		float translateX = values[Matrix.MTRANS_X], translateY = values[Matrix.MTRANS_Y];

		if (scaledImageWidth < viewWidth) {
			translateX = (float)viewWidth / 2 - (float)scaledImageWidth / 2;
		}
		if (scaledImageHeight < viewHeight) {
			translateY = (float)viewHeight / 2 - (float)scaledImageHeight / 2;
		}

		values[Matrix.MTRANS_X] = translateX;
		values[Matrix.MTRANS_Y] = translateY;

		// Use setValues instead of setTranslate() because when setTranslate() is called, 
		// rest of values in a matrix is reset too.
		matrix.setValues(values);
	}

	private boolean isImageEmpty() {
		return imageView.getDrawable() == null;
	}

	public void panTo(PointF difference) {
		Assert.notNull(difference, "The target point");
		panTo(difference.x, difference.y);
	}

	@Override
	public void panTo(float dx, float dy) {
		if(isImageEmpty()) {
			return;
		}

		translatePosition(currentMatrix, dx, dy);
		boundArea(currentMatrix);
		centerImage(currentMatrix);
		setImageMatrix(currentMatrix);
	}

	private void setImageMatrix(Matrix matrix) {
		imageView.setImageMatrix(matrix);
	}
	/**
	 * Move a position by difference of {@code dx} and {@code dy}
	 * @param matrix
	 * @param dx
	 * @param dy
	 */
	private void translatePosition(Matrix matrix, float dx, float dy) {
		float[] values = getValuesOfMatrix(matrix);
		values[Matrix.MTRANS_X] += dx;
		values[Matrix.MTRANS_Y] += dy;
		matrix.setValues(values);
	}
	/**
	 * Don't let an image go outside
	 * @param matrix
	 */
	protected void boundArea(Matrix matrix) {
		float[] values = getValuesOfMatrix(matrix);

		float scaledImageWidth = imageWidth * values[Matrix.MSCALE_X];
		float scaledImageHeight = imageHeight * values[Matrix.MSCALE_Y];
		float translateX = values[Matrix.MTRANS_X], translateY = values[Matrix.MTRANS_Y];

		// don't let the image go outside
		if (translateX < viewWidth - scaledImageWidth) {
			translateX = viewWidth - scaledImageWidth;
		}
		if (translateY < viewHeight - scaledImageHeight) {
			translateY = viewHeight - scaledImageHeight;
		}
		if (translateX > 0.0f) {
			translateX = 0.0f;
		}
		if (translateY > 0.0f) {
			translateY = 0.0f;
		}
		
		values[Matrix.MTRANS_X] = translateX;
		values[Matrix.MTRANS_Y] = translateY;
		// Use setValues instead of setTranslate() because when setTranslate() is called, 
		// rest of values in a matrix is reset too.
		matrix.setValues(values);
	}

	private static float[] getValuesOfMatrix(Matrix matrix) {
		float[] values = new float[DEFAULT_MATRIX_SIZE];
		matrix.getValues(values);
		return values;
	}	
}
