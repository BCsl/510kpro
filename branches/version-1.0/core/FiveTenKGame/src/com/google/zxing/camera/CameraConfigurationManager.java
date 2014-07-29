/*
 * Copyright (C) 2010 ZXing authors
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

package com.google.zxing.camera;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

final class CameraConfigurationManager {

	private static final String TAG = CameraConfigurationManager.class
			.getSimpleName();

	private static final int TEN_DESIRED_ZOOM = 27;
	private static final int DESIRED_SHARPNESS = 30;

	private static final Pattern COMMA_PATTERN = Pattern.compile(",");

	private final Context context;
	private Point screenResolution;
	private Point cameraResolution;
	private int previewFormat;
	private String previewFormatString;
	private static final float MIN_PREVIEW_PIXELS = 480 * 320; // normal screen
	private static final double MAX_ASPECT_DISTORTION = 0.15; // 最大失真率

	CameraConfigurationManager(Context context) {
		this.context = context;
	}

	/**
	 * 获取屏幕分辨率，相机分辨率 Reads, one time, values from the camera that are needed by
	 * the app.
	 */
	void initFromCameraParameters(Camera camera) {
		Camera.Parameters parameters = camera.getParameters();
		previewFormat = parameters.getPreviewFormat();
		previewFormatString = parameters.get("preview-format");
		Log.d(TAG, "Default preview format: " + previewFormat + '/'
				+ previewFormatString);
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		screenResolution = new Point(display.getWidth(), display.getHeight());
		Log.d(TAG, "Screen resolution: " + screenResolution);
		// cameraResolution = getCameraResolution(parameters, screenResolution);
		cameraResolution = findBestPreviewSizeValue(parameters,
				screenResolution);
		Log.d(TAG, "Camera resolution: " + screenResolution);
	}

	/**
	 * Sets the camera up to take preview images which are used for both preview
	 * and decoding. We detect the preview format here so that
	 * buildLuminanceSource() can build an appropriate LuminanceSource subclass.
	 * In the future we may want to force YUV420SP as it's the smallest, and the
	 * planar Y can be used for barcode scanning without a copy in some cases.
	 */
	void setDesiredCameraParameters(Camera camera) {
		Camera.Parameters parameters = camera.getParameters();
		Log.d(TAG, "Setting preview size: " + cameraResolution);
		parameters.setPreviewSize(cameraResolution.x, cameraResolution.y);
		setFlash(parameters);
		setZoom(parameters);
		// setSharpness(parameters);
		// modify here

		// camera.setDisplayOrientation(90);
		// 兼容2.1
		setDisplayOrientation(camera, 90);
		camera.setParameters(parameters);
	}

	Point getCameraResolution() {
		return cameraResolution;
	}

	Point getScreenResolution() {
		return screenResolution;
	}

	int getPreviewFormat() {
		return previewFormat;
	}

	String getPreviewFormatString() {
		return previewFormatString;
	}

	// /**
	// * 获取摄像机分辨率
	// * @param parameters
	// * @param screenResolution
	// * @return
	// */
	// private static Point getCameraResolution(Camera.Parameters parameters,
	// Point screenResolution) {
	//
	// String previewSizeValueString = parameters.get("preview-size-values");
	// // saw this on Xperia
	// if (previewSizeValueString == null) {
	// previewSizeValueString = parameters.get("preview-size-value");
	// }
	//
	// Point cameraResolution = null;
	//
	// if (previewSizeValueString != null) {
	// Log.d(TAG, "preview-size-values parameter: " + previewSizeValueString);
	// cameraResolution = findBestPreviewSizeValue(previewSizeValueString,
	// screenResolution);
	// }
	//
	// if (cameraResolution == null) {
	// // Ensure that the camera resolution is a multiple of 8, as the screen
	// may not be.
	// cameraResolution = new Point(
	// (screenResolution.x >> 3) << 3,
	// (screenResolution.y >> 3) << 3);
	// }
	//
	// return cameraResolution;
	// }
	// preview-size-values parameter:
	// 1280x720,800x480,768x432,720x480,640x480,576x432,480x320,384x288,352x288,320x240,240x160,176x144
	// etc.
	// screenResolution 480*845
	private static Point findBestPreviewSizeValue(
			CharSequence previewSizeValueString, Point screenResolution) {
		int bestX = 0;
		int bestY = 0;
		int diff = Integer.MAX_VALUE;
		for (String previewSize : COMMA_PATTERN.split(previewSizeValueString)) {

			previewSize = previewSize.trim();
			int dimPosition = previewSize.indexOf('x');
			if (dimPosition < 0) {
				Log.w(TAG, "Bad preview-size: " + previewSize);
				continue;
			}

			int newX;
			int newY;
			try {
				newX = Integer.parseInt(previewSize.substring(0, dimPosition));
				newY = Integer.parseInt(previewSize.substring(dimPosition + 1));
			} catch (NumberFormatException nfe) {
				Log.w(TAG, "Bad preview-size: " + previewSize);
				continue;
			}

			int newDiff = Math.abs(newX - screenResolution.x)
					+ Math.abs(newY - screenResolution.y);
			if (newDiff == 0) {
				bestX = newX;
				bestY = newY;
				break;
			} else if (newDiff < diff) { // 找出差异最小的
				bestX = newX;
				bestY = newY;
				diff = newDiff;
			}

		}

		if (bestX > 0 && bestY > 0) {
			return new Point(bestX, bestY);
			// return new Point(1280, 720);
		}
		return null;

	}

	/**
	 * 找出最符合屏幕的预览成像分辨率，不推荐在android 4.0以下的机器使用
	 * 
	 * @param parameters
	 * @param screenResolution
	 * @return
	 */
	public Point findBestPreviewSizeValue(Camera.Parameters parameters,
			Point screenResolution) {
		List<Camera.Size> rawSupportedSizes = parameters
				.getSupportedPreviewSizes();
		if (rawSupportedSizes == null) {
			Log.w(TAG,
					"Device returned no supported preview sizes; using default");
			Camera.Size defaultSize = parameters.getPreviewSize();
			if (defaultSize == null) {
				throw new IllegalStateException(
						"Parameters contained no preview size!");
			}
			return new Point(defaultSize.width, defaultSize.height);
		}
		// Sort by size, descending
		List<Camera.Size> supportedPreviewSizes = new ArrayList<Camera.Size>(
				rawSupportedSizes);
		Collections.sort(supportedPreviewSizes, new Comparator<Camera.Size>() {
			@Override
			public int compare(Camera.Size a, Camera.Size b) {
				int aPixels = a.height * a.width;
				int bPixels = b.height * b.width;
				if (bPixels < aPixels) {
					return -1;
				}
				if (bPixels > aPixels) {
					return 1;
				}
				return 0;
			}
		});

		StringBuilder previewSizesString = new StringBuilder();
		for (Camera.Size supportedPreviewSize : supportedPreviewSizes) {
			previewSizesString.append(supportedPreviewSize.width).append('x')
					.append(supportedPreviewSize.height).append(' ');
		}
		Log.i(TAG, "Supported preview sizes: " + previewSizesString);
		// -----Supported preview sizes such as:
		// 1280x720,800x480,768x432,720x480,640x480,576x432,480x320,384x288,352x288,320x240,240x160,176x144
		// etc.-----//
		double screenAspectRatio = (double) screenResolution.x
				/ (double) screenResolution.y;

		// Remove sizes that are unsuitable
		Iterator<Camera.Size> it = supportedPreviewSizes.iterator();
		while (it.hasNext()) {
			Camera.Size supportedPreviewSize = it.next();
			int realWidth = supportedPreviewSize.width;
			int realHeight = supportedPreviewSize.height;
			if (realWidth * realHeight < MIN_PREVIEW_PIXELS) {
				it.remove();
				continue;
			}

			boolean isCandidatePortrait = realWidth < realHeight; // 是否按照竖屏的方式计算？
			// -------------------横屏用下面两个计算方式--------------------------
//			 int maybeFlippedWidth = isCandidatePortrait ? realHeight :
//			 realWidth;
//			 int maybeFlippedHeight = isCandidatePortrait ? realWidth :
//			 realHeight;
			int maybeFlippedWidth = isCandidatePortrait ? realWidth
					: realHeight;
			int maybeFlippedHeight = isCandidatePortrait ? realHeight
					: realWidth;
			double aspectRatio = (double) maybeFlippedWidth
					/ (double) maybeFlippedHeight;
			double distortion = Math.abs(aspectRatio - screenAspectRatio);
			if (distortion > MAX_ASPECT_DISTORTION) {
				it.remove();
				continue;
			}

			if (maybeFlippedWidth == screenResolution.x
					&& maybeFlippedHeight == screenResolution.y) {
				Point exactPoint = new Point(realWidth, realHeight);
				Log.i(TAG, "Found preview size exactly matching screen size: "
						+ exactPoint);
				return exactPoint;
			}
		}// end while

		// If no exact match, use largest preview size. This was not a great
		// idea on older devices because
		// of the additional computation needed. We're likely to get here on
		// newer Android 4+ devices, where
		// the CPU is much more powerful.
		if (!supportedPreviewSizes.isEmpty()) {
			Camera.Size largestPreview = supportedPreviewSizes.get(0);
			Point largestSize = new Point(largestPreview.width,
					largestPreview.height);
			Log.i(TAG, "Using largest suitable preview size: " + largestSize);
			return largestSize;
		}

		// If there is nothing at all suitable, return current preview size
		Camera.Size defaultPreview = parameters.getPreviewSize();
		if (defaultPreview == null) {
			throw new IllegalStateException(
					"Parameters contained no preview size!");
		}
		Point defaultSize = new Point(defaultPreview.width,
				defaultPreview.height);
		Log.i(TAG, "No suitable preview sizes, using default: " + defaultSize);
		return defaultSize;
	}// findBestPreviewSizeValue

	private static int findBestMotZoomValue(CharSequence stringValues,
			int tenDesiredZoom) {
		int tenBestValue = 0;
		for (String stringValue : COMMA_PATTERN.split(stringValues)) {
			stringValue = stringValue.trim();
			double value;
			try {
				value = Double.parseDouble(stringValue);
			} catch (NumberFormatException nfe) {
				return tenDesiredZoom;
			}
			int tenValue = (int) (10.0 * value);
			if (Math.abs(tenDesiredZoom - value) < Math.abs(tenDesiredZoom
					- tenBestValue)) {
				tenBestValue = tenValue;
			}
		}
		return tenBestValue;
	}

	private void setFlash(Camera.Parameters parameters) {
		// FIXME: This is a hack to turn the flash off on the Samsung Galaxy.
		// And this is a hack-hack to work around a different value on the
		// Behold II
		// Restrict Behold II check to Cupcake, per Samsung's advice
		// if (Build.MODEL.contains("Behold II") &&
		// CameraManager.SDK_INT == Build.VERSION_CODES.CUPCAKE) {
		if (Build.MODEL.contains("Behold II") && CameraManager.SDK_INT == 3) { // 3
																				// =
																				// Cupcake
			parameters.set("flash-value", 1);
		} else {
			parameters.set("flash-value", 2);
		}
		// This is the standard setting to turn the flash off that all devices
		// should honor.
		parameters.set("flash-mode", "off");
	}

	private void setZoom(Camera.Parameters parameters) {

		String zoomSupportedString = parameters.get("zoom-supported");
		if (zoomSupportedString != null
				&& !Boolean.parseBoolean(zoomSupportedString)) {
			return;
		}

		int tenDesiredZoom = TEN_DESIRED_ZOOM;

		String maxZoomString = parameters.get("max-zoom");
		if (maxZoomString != null) {
			try {
				int tenMaxZoom = (int) (10.0 * Double
						.parseDouble(maxZoomString));
				if (tenDesiredZoom > tenMaxZoom) {
					tenDesiredZoom = tenMaxZoom;
				}
			} catch (NumberFormatException nfe) {
				Log.w(TAG, "Bad max-zoom: " + maxZoomString);
			}
		}

		String takingPictureZoomMaxString = parameters
				.get("taking-picture-zoom-max");
		if (takingPictureZoomMaxString != null) {
			try {
				int tenMaxZoom = Integer.parseInt(takingPictureZoomMaxString);
				if (tenDesiredZoom > tenMaxZoom) {
					tenDesiredZoom = tenMaxZoom;
				}
			} catch (NumberFormatException nfe) {
				Log.w(TAG, "Bad taking-picture-zoom-max: "
						+ takingPictureZoomMaxString);
			}
		}

		String motZoomValuesString = parameters.get("mot-zoom-values");
		if (motZoomValuesString != null) {
			tenDesiredZoom = findBestMotZoomValue(motZoomValuesString,
					tenDesiredZoom);
		}

		String motZoomStepString = parameters.get("mot-zoom-step");
		if (motZoomStepString != null) {
			try {
				double motZoomStep = Double.parseDouble(motZoomStepString
						.trim());
				int tenZoomStep = (int) (10.0 * motZoomStep);
				if (tenZoomStep > 1) {
					tenDesiredZoom -= tenDesiredZoom % tenZoomStep;
				}
			} catch (NumberFormatException nfe) {
				// continue
			}
		}

		// Set zoom. This helps encourage the user to pull back.
		// Some devices like the Behold have a zoom parameter
		if (maxZoomString != null || motZoomValuesString != null) {
			parameters.set("zoom", String.valueOf(tenDesiredZoom / 10.0));
		}

		// Most devices, like the Hero, appear to expose this zoom parameter.
		// It takes on values like "27" which appears to mean 2.7x zoom
		if (takingPictureZoomMaxString != null) {
			parameters.set("taking-picture-zoom", tenDesiredZoom);
		}
	}

	public static int getDesiredSharpness() {
		return DESIRED_SHARPNESS;
	}

	/**
	 * compatible 1.6
	 * 
	 * @param camera
	 * @param angle
	 */
	protected void setDisplayOrientation(Camera camera, int angle) {
		Method downPolymorphic;
		try {
			downPolymorphic = camera.getClass().getMethod(
					"setDisplayOrientation", new Class[] { int.class });
			if (downPolymorphic != null)
				downPolymorphic.invoke(camera, new Object[] { angle });
		} catch (Exception e1) {
		}
	}

}
