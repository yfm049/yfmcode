package com.iptv.App;

import android.app.Application;
import android.content.Intent;
import android.widget.Toast;

import com.forcetech.android.ForceTV;
import com.iptv.live.play;
import com.iptv.pro.UpdateService;
import com.iptv.utils.HttpClientHelper;
import com.mediatv.R;

public class IptvApp extends Application {

	public static play play;
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
		play=new play();
		play.init(this);
		Intent service=new Intent(this,UpdateService.class);
		this.startService(service);
	}
}
