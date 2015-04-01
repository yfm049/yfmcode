package com.iptv.App;

import android.app.Application;
import android.content.Intent;
import android.widget.Toast;

import com.forcetech.android.ForceTV;
import com.tvbox.tvplay.R;
import com.iptv.pro.UpdateService;
import com.iptv.utils.HttpClientHelper;

public class IptvApp extends Application {

	
	private boolean islogin=false;
	public boolean isIslogin() {
		return islogin;
	}

	public void setIslogin(boolean islogin) {
		this.islogin = islogin;
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
		Toast.makeText(this, "ÄÚ´æºÜµÍ", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		HttpClientHelper.baseurl=this.getResources().getText(R.string.httpurl).toString();
		Intent service=new Intent(this,UpdateService.class);
		this.startService(service);
	}
}
