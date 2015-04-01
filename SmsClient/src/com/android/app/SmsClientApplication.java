package com.android.app;

import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;

import com.android.smsclient.BroadcastReceiverImpl;

public class SmsClientApplication extends Application {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		IntentFilter filter = new IntentFilter(Intent.ACTION_TIME_TICK); 
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(Integer.MAX_VALUE);
		BroadcastReceiverImpl receiver = new BroadcastReceiverImpl(); 
		registerReceiver(receiver, filter); 
		
	}

}
