package com.iptv.App;

import android.app.Application;

import com.forcetech.android.ForceTV;
import com.iptv.utils.SqliteUtils;
import com.iptv.utils.Utils;

public class IptvApp extends Application {

	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		SqliteUtils.version=Utils.getversioncode(this);
		ForceTV.initForceClient();
	}
}
