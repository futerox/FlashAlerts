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
 * Created by spiros on 11/7/13.
 */
public class BatteryLevelReceiver extends BroadcastReceiver {
	private static final String TAG = BatteryLevelReceiver.class.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {
		CallerFlashlight callerFlashlight = (CallerFlashlight) context.getApplicationContext();
		//		String level = BatteryManager.EXTRA_LEVEL;
		//		if (CallerFlashlight.LOG) Toast.makeText(context, intent.getAction(), Toast.LENGTH_LONG).show();
		if (CallerFlashlight.LOG) Log.d(TAG, "intent action: " + intent.getAction());
		//		if (CallerFlashlight.LOG) Log.d(TAG, "battery level:" + level);
		if (intent.getAction().equals(Intent.ACTION_BATTERY_LOW)) callerFlashlight.setLowBat(true);
		else callerFlashlight.setLowBat(false);

	}
}
