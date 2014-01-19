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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by spiros on 8/4/13.
 */
public class SmsReceiver extends BroadcastReceiver {

	private static final String TAG = SmsReceiver.class.getSimpleName();
	private CallerFlashlight callerFlashlight;

	@Override
	public void onReceive(Context context, Intent intent) {
		callerFlashlight = (CallerFlashlight) context.getApplicationContext();
		if (Flash.getRunning() < 1 && callerFlashlight.isMsgFlash() && callerFlashlight.isEnabled()) {
			new ManageFlash().execute(callerFlashlight.getMsgFlashOnDuration(), callerFlashlight.getMsgFlashOffDuration(),
					callerFlashlight.getMsgFlashDuration());
		}
	}

	public class ManageFlash extends AsyncTask<Integer, Integer, String> {


		private Flash flash = new Flash(callerFlashlight);

		public ManageFlash() {
			Flash.incRunning();
		}

		//		@Override
		//		protected void onPreExecute() {
		//			super.onPreExecute();
		//			flash = new Flash(callerFlashlight);
		//		}

		@Override
		protected String doInBackground(Integer... integers) {
			if (CallerFlashlight.LOG) Log.d(TAG, "doInBackgroung Started");
			long start = System.currentTimeMillis();
			int tries = 3;
			if (callerFlashlight.getMsgFlashType() == 1) {
				int durMillis = integers[2] * 1000;
				while (System.currentTimeMillis() - start <= durMillis && tries > 0) {
					flash.enableFlash(Long.valueOf(integers[0]), Long.valueOf(integers[1]));
					if (!Flash.gotCam) {
						if (CallerFlashlight.LOG) Log.d(TAG, "Flash failed, retrying..." + tries);
						tries = tries - 1;
					}
				}
			} else if (callerFlashlight.getMsgFlashType() == 2) {
				int times = 0;
				int repeats = integers[2];
				while (times < repeats && tries > 0) {
					flash.enableFlash(Long.valueOf(integers[0]), Long.valueOf(integers[1]));
					if (!Flash.gotCam) {
						if (CallerFlashlight.LOG) Log.d(TAG, "Flash failed, retrying..." + tries);
						tries = tries - 1;
					} else {
						times = times + 1;
					}
				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(String s) {
			super.onPostExecute(s);
			if (CallerFlashlight.LOG) Log.d(TAG, "onPostExecute Started");
			Flash.decRunning();
			if (Flash.getRunning() == 0) Flash.releaseCam();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			if (CallerFlashlight.LOG) Log.d(TAG, "onCancelled Started");
			Flash.decRunning();
			if (Flash.getRunning() == 0) Flash.releaseCam();
		}


	}
}


