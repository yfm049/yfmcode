package com.iptv.App;

import android.app.Application;
import android.content.Intent;

import com.forcetech.android.ForceTV;
import com.iptv.HRTV.R;
import com.iptv.pro.UpdateService;
import com.iptv.utils.HttpClientHelper;

public class IptvApp extends Application {

	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		ForceTV.initForceClient(); 
		HttpClientHelper.baseurl=this.getResources().getText(R.string.httpurl).toString();
		Intent service=new Intent(this,UpdateService.class);
		this.startService(service);
	}
}
