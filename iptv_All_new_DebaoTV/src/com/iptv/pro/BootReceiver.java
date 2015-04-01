package com.iptv.pro;

import com.iptv.utils.ComUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class BootReceiver extends BroadcastReceiver {

	
	@Override
	public void onReceive(Context context, Intent arg1) {
		// TODO Auto-generated method stub
		if(ComUtils.getConfig(context, "isautostart", false)){
			Intent intent=new Intent(context,LoginActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		}
	}

}
