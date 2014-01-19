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

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
//import com.xcs.flashalerts.R;


/**
 * Created by spiros on 10/12/13.
 */
public class FirstTimeUtilisation extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

	private static final String TAG = FirstTimeUtilisation.class.getSimpleName();
	private CallerFlashlight callerFlashlight;
	private Button contButton;
    private AdView adView;
    AdRequest adr_cancel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        try {
            AdView adView = (AdView) this.findViewById(R.id.add);
            AdRequest adRequest = new AdRequest();
            adView.loadAd(adRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
		if (CallerFlashlight.LOG) Log.d(TAG, "onCreated");
		setContentView(R.layout.first_time);
		callerFlashlight = (CallerFlashlight) getApplication();
		Button testButton = (Button) findViewById(R.id.firstFlashTest);
		testButton.setOnClickListener(this);
		contButton = (Button) findViewById(R.id.firstcontinue);
		contButton.setOnClickListener(this);
		Spinner moduleList = (Spinner) findViewById(R.id.module_list);
		moduleList.setOnItemSelectedListener(this);
		ImageView logo = (ImageView) findViewById(R.id.logo);
		logo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.round));

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (CallerFlashlight.LOG) Log.d(TAG, "onResumed");
	}

	@Override
	public void onBackPressed() {
		//		super.onBackPressed();
		return;

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.firstFlashTest:
				//					callerFlashlight.setMsgFlashTest(false);
				new ManageFlash().execute();
				//					startActivity(new Intent(this, CameraSurface.class));
				contButton.setEnabled(true);
				break;
			case R.id.firstcontinue:
				setResult(RESULT_OK, new Intent());
				finish();
				break;
		}

	}

	@Override
	public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
		if (CallerFlashlight.LOG) Log.d(TAG, "onItemClick " + i + 1);
		callerFlashlight.setType(i + 1);
		callerFlashlight.setWindowDimensions(getWindowManager());
	}

	@Override
	public void onNothingSelected(AdapterView<?> adapterView) {

	}

	public class ManageFlash extends AsyncTask<Integer, Integer, String> {


		private int flashes = 3;
		private Flash flash;

		public ManageFlash() {

			flash = new Flash(callerFlashlight);
			Flash.incRunning();
		}

		@Override
		protected String doInBackground(Integer... integers) {
			if (CallerFlashlight.LOG) Log.d(TAG, "doInBackgroung Started");

			while (flashes > 0) {
				flash.enableFlash(callerFlashlight.getCallFlashOnDuration(), callerFlashlight.getCallFlashOffDuration());
				flashes -= 1;
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
