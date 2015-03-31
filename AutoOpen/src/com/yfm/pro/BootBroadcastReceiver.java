package com.yfm.pro;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class BootBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		System.out.println("Æô¶¯¹ã²¥¡£¡£¡£¡£");
		if(getNetWorkState(arg0)){
			Intent intent=new Intent(arg0,AutoOpenService.class);
			arg0.startService(intent);
		}
		
	}
	public boolean getNetWorkState(Context context){
		ConnectivityManager cm=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo=cm.getActiveNetworkInfo();
		if(networkinfo!=null&&networkinfo.isAvailable()){
			return true;
		}else{
			return false;
		}
	}

}
