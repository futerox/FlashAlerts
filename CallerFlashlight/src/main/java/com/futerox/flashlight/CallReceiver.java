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
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by spiros on 8/4/13.
 */
public class CallReceiver extends BroadcastReceiver {

	private static final String TAG = CallReceiver.class.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {

		if (CallerFlashlight.LOG) Log.d(TAG, "onReceived");

		CallerFlashlight callerFlashlight = (CallerFlashlight) context.getApplicationContext();
		MyPhoneStateListener phoneListener = new MyPhoneStateListener(callerFlashlight);
		TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		telephony.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);

	}

	static class MyPhoneStateListener extends PhoneStateListener {


		private static String callState = "";
		private final CallerFlashlight callerFlashlight;

		public MyPhoneStateListener(CallerFlashlight cf) {
			callerFlashlight = cf;

		}

		public void onCallStateChanged(int state, String incomingNumber) {

			if (callerFlashlight.isCallFlash()) {
				switch (state) {
					case TelephonyManager.CALL_STATE_IDLE:

						callState = "IDLE";
						if (CallerFlashlight.LOG) Log.d(TAG, callState);
						break;

					case TelephonyManager.CALL_STATE_OFFHOOK:

						callState = "OFFHOOK";
						if (CallerFlashlight.LOG) Log.d(TAG, callState);
						break;

					case TelephonyManager.CALL_STATE_RINGING:

						callState = "RINGING";
						if (CallerFlashlight.LOG) Log.d(TAG, callState);
						if (Flash.getRunning() < 1 && callerFlashlight.isEnabled()) {
							callerFlashlight.registerVolumeButtonReceiver();
							new ManageFlash().execute(callerFlashlight.getCallFlashOnDuration(), callerFlashlight.getCallFlashOffDuration());
						}
						break;
					default:
						callState = "DEFAULT";
						break;
				}
			}
		}

		public class ManageFlash extends AsyncTask<Integer, Integer, String> {


			private Flash flash = new Flash(callerFlashlight);

			public ManageFlash() {
				Flash.incRunning();

			}

			//			@Override
			//			protected void onPreExecute() {
			//				super.onPreExecute();
			//				flash = new Flash(callerFlashlight);
			//			}

			@Override
			protected String doInBackground(Integer... integers) {
				if (CallerFlashlight.LOG) Log.d(TAG, "doInBackgroung Started");
				int tries = 3;
				while (callState.equals("RINGING") && tries > 0 && !callerFlashlight.isVolumeButtonPressed()) {
					flash.enableFlash(Long.valueOf(integers[0]), Long.valueOf(integers[1]));
					if (!Flash.gotCam) {
						if (CallerFlashlight.LOG) Log.d(TAG, "Flash failed, retrying..." + tries);
						tries = tries - 1;
					}
				}
				return null;
			}

			@Override
			protected void onPostExecute(String s) {
				super.onPostExecute(s);
				if (CallerFlashlight.LOG) Log.d(TAG, "onPostExecute Started");
				Flash.decRunning();
				if (Flash.getRunning() == 0) {
					Flash.releaseCam();
					callerFlashlight.unregisterVolumeButtonReceiver();
				}
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
}