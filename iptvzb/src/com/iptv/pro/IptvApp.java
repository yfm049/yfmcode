package com.iptv.pro;

import android.app.Application;
import android.util.Log;

import com.forcetech.android.ForceTV;
import com.iptv.utils.User;

public class IptvApp extends Application {

	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.i("tvinfo", "app create");
		user=new User();
		ForceTV.initForceClient();
		
	}

	public User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
