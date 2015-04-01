package com.iptv.App;

import android.app.Application;

import com.forcetech.android.ForceTV;

public class IptvApp extends Application {

	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		ForceTV.initForceClient();
		
	}
}
