package com.njbst.pro;


import android.app.Application;

import com.njbst.utils.CrashHandler;

public class CrashApplication extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
//		CrashHandler crashHandler = CrashHandler.getInstance();
//		crashHandler.init(getApplicationContext());
	}
}

