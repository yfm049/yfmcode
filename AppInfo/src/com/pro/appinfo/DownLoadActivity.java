package com.pro.appinfo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.pro.adapter.DownLoadAdapter;
import com.pro.model.Appinfo;
//软件下载界面
public class DownLoadActivity extends Activity {

	
	private ListView datalist;
	private List<Appinfo> fl = new ArrayList<Appinfo>();
	private DownLoadAdapter adapter;
	private downloadReceiver receiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_download);
		datalist = (ListView) super.findViewById(R.id.datalist);
		super.findViewById(R.id.sname);
		adapter = new DownLoadAdapter(this, fl);
		datalist.setAdapter(adapter);
		receiver=new downloadReceiver();
		this.registerReceiver(receiver, new IntentFilter("com.pro.appinfo.download"));
		datalist.setOnItemClickListener(new OnItemClickListenerImpl());
	}
	//绑定数据点击事件
	class OnItemClickListenerImpl implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			Appinfo info=fl.get(arg2);
			Intent intent = new Intent(Intent.ACTION_VIEW); 
			intent.setDataAndType(Uri.fromFile(info.getFile()), "application/vnd.android.package-archive"); 
			startActivity(intent); 
		}
		
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initdate();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		this.unregisterReceiver(receiver);
	}
	//初始化数据
	private void initdate(){
		fl.clear();
		if(DownLoadService.lai!=null){
			fl.addAll(DownLoadService.lai.keySet());
		}
		adapter.notifyDataSetChanged();
	}
	//软件下载广播接收
	class downloadReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			initdate();
		}
		
	}
	


}
