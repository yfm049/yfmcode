package com.pro.push;

import cn.jpush.android.api.JPushInterface;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MessageServer extends Service {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		JPushInterface.setDebugMode(true); 	//≤‚ ‘ƒ£ Ω
        JPushInterface.init(this);     	
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
