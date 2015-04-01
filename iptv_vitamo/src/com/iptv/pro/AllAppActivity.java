package com.iptv.pro;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.iptv.adapter.AllAppAdapter;
import com.iptv.season.R;
import com.iptv.pojo.AllAppinfo;

public class AllAppActivity extends Activity {

	private List<AllAppinfo> lapp=new ArrayList<AllAppinfo>();
	private GridView appgird;
	private PackageManager pm;
	private AllAppAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_allapp);
		appgird=(GridView)super.findViewById(R.id.appgird);
		init();
		adapter=new AllAppAdapter(this, lapp);
		appgird.setAdapter(adapter);
		appgird.setOnItemClickListener(new OnItemClickListenerImpl());
	}
	class OnItemClickListenerImpl implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			AllAppinfo app=lapp.get(arg2);
			Intent  intent=pm.getLaunchIntentForPackage(app.getPackagename());
			if(intent!=null){
				AllAppActivity.this.startActivity(intent);
			}
		}
		
	}
	public void init(){
		pm=this.getPackageManager();
		List<PackageInfo> packs = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES); 
		for(PackageInfo info:packs){
			AllAppinfo app=new AllAppinfo();
			app.setIconid(info.applicationInfo.icon);
			app.setIcon(info.applicationInfo.loadIcon(pm));
			app.setName(info.applicationInfo.loadLabel(pm).toString());
			app.setPackagename(info.applicationInfo.packageName);
			lapp.add(app);
		}
	}

	public boolean openCLD(String packageName) {
		PackageManager packageManager = this.getPackageManager();
		PackageInfo pi = null;
		try {
			pi = packageManager.getPackageInfo(packageName, 0);
		} catch (NameNotFoundException e) {
			Log.i("tvinfo", "packageName²»´æÔÚ"+packageName);
		}
		if(pi==null){
			return false;
		}
		Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
		resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		resolveIntent.setPackage(pi.packageName);

		List<ResolveInfo> apps = packageManager.queryIntentActivities(
				resolveIntent, 0);
		ResolveInfo ri = apps.iterator().next();
		if (ri != null) {
			String className = ri.activityInfo.name;

			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);

			ComponentName cn = new ComponentName(packageName, className);

			intent.setComponent(cn);
			this.startActivity(intent);
			return true;
		}
		return false;
		
	}
}
