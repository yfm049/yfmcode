package com.example.wnotes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.wnotes.db.DBService;

public class CallAlarm extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		DBService dbService = new DBService(context);
		Remind remind = dbService.getRemindMsg();
		dbService.close();// 关闭数据库连接
		if (remind != null) {
			Intent myIntent = new Intent(context, AlarmAlert.class);
			Bundle bundleRet = new Bundle();
			bundleRet.putString("remindMsg", remind.msg);
			bundleRet.putBoolean("shake", remind.shake);
			bundleRet.putBoolean("ring", remind.ring);
			myIntent.putExtras(bundleRet);
			myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(myIntent);
		}

	}

}

