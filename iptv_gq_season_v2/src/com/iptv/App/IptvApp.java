package com.iptv.App;

import java.util.HashMap;
import java.util.Map;

import android.app.Application;
import android.util.Log;

import com.iptv.pojo.Yurl;

public class IptvApp extends Application {

	public static Map<String, Yurl> murl=new HashMap<String, Yurl>();
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.i("tvinfo", "app Æô¶¯");
		
		
	}
}
