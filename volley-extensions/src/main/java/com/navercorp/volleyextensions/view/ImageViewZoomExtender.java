package com.navercorp.volleyextensions.view;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.navercorp.volleyextensions.util.Assert;

class ImageViewZoomExtender implements Zoomable {
	public static final int DEFAULT_MATRIX_SIZE = 3 * 3;
	public static final int NONE_DEF_STYLE = 0;
	public static final float ORIGINAL_LEVEL = 1.0f;
	private static final PointF topLeftPoint = new PointF(0.0f, 0.0f);
	private static final float INFINITE_LEVEL = Float.MAX_VALUE;

	private float zoomLevel = ORIGINAL_LEVEL;
	private ImageView imageView;
	private final Matrix currentMatrix = new Matrix();
	private float initialScaleSize;
	private boolean isInitialized = false;

	private int viewWidth;
	private int viewHeight;
	private int imageWidth;
	private int imageHeight;
	private float maximumZoomLevel;
	private float minimumZoomLevel;

	public ImageViewZoomExtender(ImageView imageView) {
		Assert.notNull(imageView, "ImageView must not be null.");
		setImageView(imageView);
		setScaleTypeMatrix();
	}

	private void setImageView(ImageView imageView) {
		this.imageView = imageView;
	}

	private void setScaleTypeMatrix() {
		imageView.setScaleType(ScaleType.MATRIX);
	}

	public float getZoomLevel() {
		return zoomLevel;
	}

	protected void reset() {
		isInitialized = false;
	}
	
	public void initializeIfNeeded() {
		if (!isInitalized()) {
			initialize();
		}
	}

	public boolean isInitalized() {
		return isInitialized;
	}

	public void initialize() {
		if (isImageEmpty()) {
			return;
		}

		currentMatrix.reset();
		saveCurrentSizes();

		setMinimumZoomLevel(ORIGINAL_LEVEL);
		setMaximumZoomLevel(INFINITE_LEVEL);

		initializeScaleSize();

		zoomTo(ORIGINAL_LEVEL);
		isInitialized = true;
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

	private void setMinimumZoomLevel(float minimumLevel) {
		if (minimumLevel < ORIGINAL_LEVEL) {
			minimumLevel = ORIGINAL_LEVEL;
		}
		if (minimumLevel > maximumZoomLevel) {
			this.maximumZoomLevel = minimumLevel;
		}

		this.minimumZoomLevel = minimumLevel;
		
	}

	public void updateMaximumZoomLevel(float maximumLevel) {
		setMaximumZoomLevel(maximumLevel);
		if(getZoomLevel() > this.maximumZoomLevel) {
			zoomTo(this.maximumZoomLevel);
		}
	}

	public void updateMinimumZoomLevel(float minimumLevel) {
		setMinimumZoomLevel(minimumLevel);
		if(getZoomLevel() < this.minimumZoomLevel) {
			zoomTo(this.minimumZoomLevel);
		}
	}

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

	private float computeFitScaleOfImage() {
		float xRatio = (float)viewWidth / (float)imageWidth;
		float yRatio = (float)viewHeight / (float)imageHeight;

		// Math.min fits the image to the shorter axis. (with letterboxes around)
		// Math.max fits the image to the longer axis. (with the other axis cropped)
		return Math.min(xRatio, yRatio);
	}

	public void zoomTo(float targetLevel) {
		zoomTo(targetLevel, topLeftPoint);
	}

	public void zoomTo(float targetLevel, PointF zoomPoint) {
		Assert.notNull(zoomPoint, "The zoom point must not be null.");
		zoomTo(targetLevel, zoomPoint.x, zoomPoint.y);
	}

	@Override
	public void zoomTo(float targetLevel, float zoomX, float zoomY) {
		if(isImageEmpty()) {
			return;
		}

		targetLevel = limitLevelSizeRange(targetLevel);

		scaleSize(currentMatrix, targetLevel, zoomX, zoomY);
		boundArea(currentMatrix);
		centerImage(currentMatrix);
		setImageMatrix(currentMatrix);
		updateZoomLevel(targetLevel);
	}

	public void scaleTo(float dScale, float zoomX, float zoomY) {
		float targetLevel = dScale * zoomLevel;
		zoomTo(targetLevel, zoomX, zoomY);
	}

	private void updateZoomLevel(float targetLevel) {
		zoomLevel = targetLevel;
	}

	protected final float limitLevelSizeRange(float targetLevel) {
		if (targetLevel < minimumZoomLevel) {
			targetLevel = minimumZoomLevel;
		}
		if (targetLevel > maximumZoomLevel) {
			targetLevel = maximumZoomLevel;
		}
		return targetLevel;
	}

	private void scaleSize(Matrix matrix, float targetLevel, float zoomX, float zoomY) {
		float newScale = targetLevel / zoomLevel;
		matrix.postScale(newScale, newScale, zoomX, zoomY);
	}

	private void centerImage(Matrix matrix) {
		float[] values = getValuesOfMatrix(matrix);
		float scaledImageWidth = imageWidth * values[Matrix.MSCALE_X], scaledImageHeight = imageHeight * values[Matrix.MSCALE_Y];
		float translateX = values[Matrix.MTRANS_X], translateY = values[Matrix.MTRANS_Y];
		if (scaledImageWidth < viewWidth) {
			translateX = (float)viewWidth / 2 - (float)scaledImageWidth / 2;
		}
		if (scaledImageHeight < viewHeight) {
			translateY = (float)viewHeight / 2 - (float)scaledImageHeight / 2;
		}

		values[Matrix.MTRANS_X] = translateX;
		values[Matrix.MTRANS_Y] = translateY;

		// Use setValues instead of setTranslate() because when setTranslate() is called, rest of values in a matrix is reset too.  
		matrix.setValues(values);
	}

	private boolean isImageEmpty() {
		return imageView.getDrawable() == null;
	}

	public void panTo(PointF difference) {
		Assert.notNull(difference, "The target point must not be null.");
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

	private void translatePosition(Matrix matrix, float dx, float dy) {
		float[] values = getValuesOfMatrix(matrix);
		values[Matrix.MTRANS_X] += dx;
		values[Matrix.MTRANS_Y] += dy;
		matrix.setValues(values);
	}

	protected void boundArea(Matrix matrix) {
		float[] values = getValuesOfMatrix(matrix);

		float scaledImageWidth = imageWidth * values[Matrix.MSCALE_X], scaledImageHeight = imageHeight * values[Matrix.MSCALE_Y];
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
		// Use setValues instead of setTranslate() because when setTranslate() is called, rest of values in a matrix is reset too.  
		matrix.setValues(values);
	}

	private static float[] getValuesOfMatrix(Matrix matrix) {
		float[] values = new float[DEFAULT_MATRIX_SIZE];
		matrix.getValues(values);
		return values;
	}	
}
