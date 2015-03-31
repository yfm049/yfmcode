package com.yfm.pro;

import java.util.List;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.IBinder;
import android.widget.Toast;

public class AutoOpenService extends Service {

	private SharedPreferences sp;
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		String value=sp.getString("key", "ÇëÑ¡Ôñä¯ÀÀÆ÷");
		System.out.println(value);
		if(!"ÇëÑ¡Ôñä¯ÀÀÆ÷".equals(value)){
			openApp(this,value);
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		sp=this.getSharedPreferences("config", MODE_PRIVATE);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	public static void openApp(Context context,String pkg) {
		try {
			PackageInfo pi = context.getPackageManager().getPackageInfo(pkg, 0);

			Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
			resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
			resolveIntent.setPackage(pi.packageName);

			List<ResolveInfo> apps = context.getPackageManager().queryIntentActivities(resolveIntent, 0);

			ResolveInfo ri = apps.iterator().next();
			if (ri != null) {
				String packageName = ri.activityInfo.packageName;
				String className = ri.activityInfo.name;

				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_LAUNCHER);

				ComponentName cn = new ComponentName(packageName, className);

				intent.setComponent(cn);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
			}
				
		} catch (NameNotFoundException e) {
			
		}
	}
}
