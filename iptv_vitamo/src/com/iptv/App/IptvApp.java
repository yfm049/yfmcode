package com.iptv.App;

import android.app.Application;
import android.content.Intent;

import com.forcetech.android.ForceTV;
import com.iptv.pro.UpdateService;

public class IptvApp extends Application {

	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		ForceTV.initForceClient();
		Intent service=new Intent(this,UpdateService.class);
		this.startService(service);
	}
}
