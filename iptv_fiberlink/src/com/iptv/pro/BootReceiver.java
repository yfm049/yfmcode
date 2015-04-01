package com.iptv.pro;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class BootReceiver extends BroadcastReceiver {

	
	@Override
	public void onReceive(Context context, Intent arg1) {
		// TODO Auto-generated method stub
		SharedPreferences sp=context.getSharedPreferences("key", Context.MODE_PRIVATE);
		if(sp.getBoolean("autostart", false)){
			Intent intent=new Intent(context,LoginActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		}
	}

}
