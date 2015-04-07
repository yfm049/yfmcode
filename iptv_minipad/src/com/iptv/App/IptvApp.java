package com.iptv.App;

import com.iptv.utils.SqliteUtils;
import com.iptv.utils.Utils;
import com.pro.vidio.VideoApplication;

public class IptvApp extends VideoApplication {

	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		SqliteUtils.version=Utils.getversioncode(this);
		
	}
}
