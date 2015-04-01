package com.pro.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import cn.jpush.android.api.JPushInterface;

public class MessageReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Intent server=new Intent(context,MessageServer.class);
		context.startService(server);
		Bundle bundle = intent.getExtras();
		if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Intent i = new Intent(context, MainActivity.class);
        	i.putExtras(bundle);
        	i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        	i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        	context.startActivity(i);
        }
	}

}
