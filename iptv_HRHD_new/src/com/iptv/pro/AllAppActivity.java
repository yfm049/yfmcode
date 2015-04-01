package com.iptv.pro;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.iptv.adapter.AllAppAdapter;
import com.iptv.utils.ComUtils;
import com.iptv.utils.LogUtils;
import com.iptv.HRTV.R;

public class AllAppActivity extends Activity {


	private List<ApplicationInfo> lapp=new ArrayList<ApplicationInfo>();
	private GridView appgird;
	private PackageManager pm;
	private AllAppAdapter adapter;
	private ProgressDialog pd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_allapp);
		appgird=(GridView)super.findViewById(R.id.appgird);
		
		adapter=new AllAppAdapter(this, lapp);
		appgird.setAdapter(adapter);
		appgird.setOnItemClickListener(new OnItemClickListenerImpl());
		pd=new ProgressDialog(this);
		pd.setMessage("正在获取应用列表...");
		pd.show();
		new AllAppThread().start();
	}
	class OnItemClickListenerImpl implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			ApplicationInfo appinfo=lapp.get(arg2);
			Intent  intent=pm.getLaunchIntentForPackage(appinfo.packageName);
			if(intent!=null){
				AllAppActivity.this.startActivity(intent);
			}
		}
		
	}
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(pd!=null){
				pd.dismiss();
			}
			adapter.notifyDataSetChanged();
		}
		
	};
	public void init(){
		pm=this.getPackageManager();
		List<PackageInfo> packs = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES); 
		for(PackageInfo info:packs){
			if((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0){
				lapp.add(info.applicationInfo);
			}
			
		}
	}

	public boolean openCLD(String packageName) {
		PackageManager packageManager = this.getPackageManager();
		PackageInfo pi = null;
		try {
			pi = packageManager.getPackageInfo(packageName, 0);
		} catch (NameNotFoundException e) {
			LogUtils.write("tvinfo", "packageName不存在"+packageName);
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
	class AllAppThread extends Thread{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			init();
			handler.sendEmptyMessage(1);
		}
		
	}
}
