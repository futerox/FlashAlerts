/***
 Copyright (c) 2013-2014 SpirosBond

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program. If not, see http://www.gnu.org/licenses

 ***

 https://github.com/spirosbond/CallerFlashlight

 ***

 */


package com.futerox.flashlight;

import android.app.Service;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.bugsense.trace.BugSenseHandler;

/**
 * Created by spiros on 8/7/13.
 */
public class Flash {

	private final static String TAG = Flash.class.getSimpleName();
	public static boolean gotCam = false;
	private static Camera cam;
	private static int running;
	//	private static SurfaceView preview;
	private static CallerFlashlight cf;
	//	private static SurfaceHolder mHolder;
	private static Camera.Parameters pon, poff;
	private static LinearLayout orientationChanger;


	public Flash(CallerFlashlight cf) {
		Flash.cf = cf;
		if (!gotCam) {
			try {
				cam = Camera.open();
				if (CallerFlashlight.LOG) Log.d(TAG, "Constructor: Camera opened successfully");
				gotCam = true;

			} catch (NullPointerException e) {
				e.printStackTrace();
				gotCam = false;
				return;
			} catch (RuntimeException e) {
				if (CallerFlashlight.LOG) Log.d(TAG, "Constructor: Unable to open the camera. Is already Opened??");
				gotCam = false;
				return;

			}
		}

		loadParameters();
	}

	public static int getRunning() {
		return running;
	}

	public static void incRunning() {

		Flash.running = Flash.running + 1;
		if (CallerFlashlight.LOG) Log.d(TAG, "running: " + running);
	}

	public static void decRunning() {

		Flash.running = Flash.running - 1;
		if (CallerFlashlight.LOG) Log.d(TAG, "running: " + running);
	}

	public static void releaseCam() {
		if (CallerFlashlight.LOG) Log.d(TAG, "releaseCam");
		if (cf.getType() == CallerFlashlight.TYPE_ALTERNATIVE || cf.getType() == CallerFlashlight.TYPE_ALTERNATIVE_2) {
			try {
				orientationChanger.setVisibility(View.GONE);
			} catch (Exception e) {
				e.printStackTrace();
				if (CallerFlashlight.LOG) Log.d(TAG, "Failed to destroy SurfaceView");
			}
		}
		if (gotCam) {
			try {
				//				cam.stopPreview();
				cam.release();
				gotCam = false;
				if (CallerFlashlight.LOG) Log.d(TAG, "Cam released");
			} catch (Exception e) {
				e.printStackTrace();
				if (CallerFlashlight.LOG) Log.d(TAG, "failed to release cam");
			}
		}
	}

	private void loadParameters() {
		if (cf.getType() == CallerFlashlight.TYPE_ALTERNATIVE || cf.getType() == CallerFlashlight.TYPE_ALTERNATIVE_2) {

			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(1, 1);

			if (cf.getType() == CallerFlashlight.TYPE_ALTERNATIVE) {
				layoutParams.setMargins(1000, 1000, 1000, 1000);
			} else if (cf.getType() == CallerFlashlight.TYPE_ALTERNATIVE_2) {
				layoutParams.setMargins(cf.getScreenWidth() - 1, cf.getScreenHeight() - 50, 0, 0);
			}

			//		preview.setLayoutParams(layoutParams);
			//			FrameLayout frame = new FrameLayout(cf.getApplicationContext());

			orientationChanger = new LinearLayout(cf.getApplicationContext());

			WindowManager.LayoutParams orientationLayout = new WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY, 0,
					PixelFormat.RGBA_8888);

			WindowManager wm = (WindowManager) cf.getApplicationContext().getSystemService(Service.WINDOW_SERVICE);

			FrameLayout frame = new FrameLayout(cf.getApplicationContext());
			frame.setLayoutParams(layoutParams);
			CameraSurface preview = new CameraSurface(cf.getApplicationContext(), cam);
			preview.setLayoutParams(layoutParams);

			frame.addView(preview);

			orientationChanger.addView(frame);
			orientationChanger.setVisibility(View.VISIBLE);

			wm.addView(orientationChanger, orientationLayout);

		}
		try {
			//			if (cf.getType() == CallerFlashlight.TYPE_NORMAL || cf.getType() == CallerFlashlight.TYPE_ALTERNATIVE) {
			pon = cam.getParameters();
			poff = cam.getParameters();
			pon.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
			poff.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
			//			} else if (cf.getType() == CallerFlashlight.TYPE_ALTERNATIVE_2) {
			//				pon = cam.getParameters();
			//				poff = cam.getParameters();
			//				pon.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
			//				poff.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
			//			}
		} catch (Exception e) {
			if (CallerFlashlight.LOG) Log.d(TAG, "unable to get parameters");
			e.printStackTrace();
			BugSenseHandler.sendException(e);
		}
	}

	public void enableFlash(long onMillis, long offMillis) {
		if (CallerFlashlight.LOG) Log.d(TAG, "enableFlash. ON: " + onMillis + " OFF: " + offMillis + " gotCam= " + gotCam);

		if (!gotCam) {
			try {
				cam = Camera.open();
				if (CallerFlashlight.LOG) Log.d(TAG, "enableFlash: Camera opened successfully");
				gotCam = true;

			} catch (NullPointerException e) {
				e.printStackTrace();
				gotCam = false;
				try {
					Thread.sleep(onMillis + offMillis);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				return;
			} catch (RuntimeException e) {
				if (CallerFlashlight.LOG) Log.d(TAG, "enableFlash: Unable to open the camera. Is already Opened??");
				gotCam = false;
				try {
					Thread.sleep(onMillis + offMillis);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				return;

			}
			loadParameters();
		}

		try {
			cam.setParameters(pon);
			//			cam.startPreview();
		} catch (Exception e) {
			e.printStackTrace();
			if (CallerFlashlight.LOG) Log.d(TAG, "failed to set parameters");
			BugSenseHandler.sendException(e);
		}

		try {
			Thread.sleep(onMillis);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//disable flash

		try {
			cam.setParameters(poff);
			//			cam.stopPreview();
		} catch (Exception e) {
			e.printStackTrace();
			if (CallerFlashlight.LOG) Log.d(TAG, "failed to set parameters");
			BugSenseHandler.sendException(e);
		}

		try {
			Thread.sleep(offMillis);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}


	}

	public void disableFlash() {
		if (CallerFlashlight.LOG) Log.d(TAG, "disableFlash");

		try {
			cam.setParameters(poff);
		} catch (Exception e) {
			e.printStackTrace();
			if (CallerFlashlight.LOG) Log.d(TAG, "failed to set parameters");
		}

	}


}
