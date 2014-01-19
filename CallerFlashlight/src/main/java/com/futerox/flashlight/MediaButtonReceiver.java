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
import android.util.Log;

/**
 * Created by spiros on 10/18/13.
 */
public class MediaButtonReceiver extends BroadcastReceiver {

	private static final String TAG = MediaButtonReceiver.class.getSimpleName();
	private static int userVolume, newVolume;
	private static boolean lowVolume;

	public MediaButtonReceiver(int volume) {
		userVolume = volume;

	}

	@Override
	public void onReceive(Context context, Intent intent) {
		try {

			//			AudioManager am = (AudioManager) context.getSystemService(context.AUDIO_SERVICE);
			//			userVolume = am.getStreamVolume(AudioManager.STREAM_RING);

			newVolume = intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_VALUE", 0);
			if (CallerFlashlight.LOG) Log.d(TAG, "onReceived: " + intent.getAction() + " newVolume: " + newVolume + " userVolume: " + userVolume);
			if (userVolume != newVolume) {
				lowVolume = true;
				return;
			}
			//			KeyEvent event = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
			//			KeyEvent event = (KeyEvent)intent.getExtras().get("android.media.RINGER_MODE_SILENT");
			//			if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {
			//				Log.d(TAG,"KeyEvent.KEYCODE_VOLUME_DOWN");
			//			}
			//			if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
			//				Log.d(TAG,"KeyEvent.KEYCODE_VOLUME_UP");
			//			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		CallerFlashlight callerFlashlight = (CallerFlashlight) context.getApplicationContext();
		//		callerFlashlight.setVolumeButtonPressed(true);
	}
}
