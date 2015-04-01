package com.iptv.pro;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.iptv.pro.R;
import com.iptv.thread.GongGaoThread;
import com.iptv.thread.UserInfoThread;
import com.iptv.utils.ComUtils;
import com.iptv.utils.LogUtils;

public class HomeActivity extends Activity {

	private ImageView livetv, playback, webmovie, market, systemsetup, info;
	private ProgressDialog pd;
	private SharedPreferences sp;
	private TextView gonggao;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		gonggao=(TextView)super.findViewById(R.id.gonggao);
		livetv = (ImageView) super.findViewById(R.id.livetv);
		playback = (ImageView) super.findViewById(R.id.playback);
		webmovie = (ImageView) super.findViewById(R.id.webmovie);
		market = (ImageView) super.findViewById(R.id.market);
		systemsetup = (ImageView) super.findViewById(R.id.systemsetup);
		info = (ImageView) super.findViewById(R.id.info);
		OnClickListener listener = new OnClickListenerImpl();

		livetv.setOnClickListener(listener);
		playback.setOnClickListener(listener);
		webmovie.setOnClickListener(listener);
		market.setOnClickListener(listener);
		systemsetup.setOnClickListener(listener);
		info.setOnClickListener(listener);
		new GongGaoThread(handler).start();
	}

	class OnClickListenerImpl implements OnClickListener {

		@Override
		public void onClick(View but) {
			// TODO Auto-generated method stub

			if (but.getId() == R.id.livetv) {
				Intent intent = new Intent(HomeActivity.this,
						NoticeActivity.class);
				HomeActivity.this.startActivity(intent);
			} else if (but.getId() == R.id.playback) {
				Intent intent = new Intent(HomeActivity.this,
						BackDataActivity.class);
				HomeActivity.this.startActivity(intent);
			} else if (but.getId() == R.id.webmovie) {
				openCLD("com.android.browser");
			} else if (but.getId() == R.id.market) {
				Openhome();
			} else if (but.getId() == R.id.systemsetup) {
				openCLD("com.android.settings");
			} else if (but.getId() == R.id.info) {
				show("获取", "正在获取用户信息...");
				new UserInfoThread(handler, "getUserDate.jsp?active="+ComUtils.getConfig(HomeActivity.this, "name", "")).start();

			}
		}

	}
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what==UserInfoThread.userinfosuc){
				pd.dismiss();
				showinfo("到期时间："+msg.obj+"\r\n登录标识："+ComUtils.getLocalMacAddressFromIp(HomeActivity.this));
			}else if(msg.what==UserInfoThread.userinfofail){
				pd.dismiss();
				Toast.makeText(HomeActivity.this, "获取用户信息失败", Toast.LENGTH_SHORT).show();
			}else if(msg.what==GongGaoThread.gonggaosuc){
				gonggao.setText(msg.obj.toString());
			}
		}
		
	};
	private void Openhome(){
		Intent intent=new Intent(this,AllAppActivity.class);
		this.startActivity(intent);
	}
	private void show(String title, String msg) {
		pd = new ProgressDialog(this, ProgressDialog.THEME_HOLO_DARK);
		pd.setTitle(title);
		pd.setMessage(msg);
		pd.show();
	}
	private void showinfo(String message){
		Builder builder=new Builder(this, AlertDialog.THEME_HOLO_DARK);
		builder.setTitle("消息");
		View view=LayoutInflater.from(this).inflate(R.layout.userinfo, null);
		RadioGroup decode=(RadioGroup)view.findViewById(R.id.decode);
		if("system".equals(ComUtils.getConfig(this, "decode", "system"))){
			decode.check(R.id.sysdecode);
		}else{
			decode.check(R.id.viamiodecode);
		}
		decode.setOnCheckedChangeListener(new OnCheckedChangeListenerImpl());
		TextView msg=(TextView)view.findViewById(R.id.message);
		CheckBox autostart=(CheckBox)view.findViewById(R.id.autostart);
		autostart.setChecked(ComUtils.getConfig(this, "isautostart", false));
		autostart.setOnCheckedChangeListener(new OnCheckedChangeListenerImpl());
		msg.setText(message);
		builder.setView(view);
		builder.setPositiveButton("关闭", null);
		builder.create().show();
	}
	class OnCheckedChangeListenerImpl implements CompoundButton.OnCheckedChangeListener,RadioGroup.OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			// TODO Auto-generated method stub
			ComUtils.setConfig(HomeActivity.this, "isautostart", arg1);
		}

		@Override
		public void onCheckedChanged(RadioGroup group, int arg1) {
			// TODO Auto-generated method stub
			int cid=group.getCheckedRadioButtonId();
			if(cid==R.id.sysdecode){
				ComUtils.setConfig(HomeActivity.this, "decode", "system");
			}
			if(cid==R.id.viamiodecode){
				ComUtils.setConfig(HomeActivity.this, "decode", "viamio");
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
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		showExit();
	}
	private void showExit(){
		Builder builder=new Builder(this,AlertDialog.THEME_HOLO_DARK);
		builder.setTitle("退出");
		builder.setMessage("确定要退出应用");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				HomeActivity.this.finish();
			}
		});
		builder.setNegativeButton("取消", null);
		builder.create().show();
	}
}
