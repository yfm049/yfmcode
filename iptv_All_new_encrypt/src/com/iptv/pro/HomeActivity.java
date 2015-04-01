package com.iptv.pro;

import java.util.List;

import android.app.Activity;
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

import com.iptv.thread.GongGaoThread;
import com.iptv.thread.UserInfoThread;
import com.iptv.utils.ComUtils;
import com.iptv.utils.LogUtils;
import com.mediatv.R;

public class HomeActivity extends Activity {

	private ImageView livetv, playback, webmovie, market, systemsetup, info;
	private ProgressDialog pd;
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
		showTestDialog();
		new GongGaoThread(handler).start();
	}
	public void showTestDialog(){
		if(LogUtils.isdebug){
			Builder test=ComUtils.createBuilder(this);
			test.setTitle(R.string.test_title);
			test.setMessage(R.string.test_msg);
			test.setNegativeButton(R.string.close, null);
			test.create().show();
			
		}
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
				show(ComUtils.getrestext(HomeActivity.this, R.string.news), ComUtils.getrestext(HomeActivity.this, R.string.userinfo));
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
				showinfo(ComUtils.getrestext(HomeActivity.this, R.string.exptime)+msg.obj+"\r\n"+ComUtils.getrestext(HomeActivity.this, R.string.loginsign)+ComUtils.getLocalMacAddressFromIp(HomeActivity.this));
			}else if(msg.what==UserInfoThread.userinfofail){
				pd.dismiss();
				Toast.makeText(HomeActivity.this, R.string.userinfofail, Toast.LENGTH_SHORT).show();
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
		Builder builder=ComUtils.createBuilder(this);
		builder.setTitle(ComUtils.getrestext(HomeActivity.this, R.string.news));
		View view=LayoutInflater.from(this).inflate(R.layout.userinfo, null);
		TextView msg=(TextView)view.findViewById(R.id.message);
		CheckBox autostart=(CheckBox)view.findViewById(R.id.autostart);
		autostart.setChecked(ComUtils.getConfig(this, "isautostart", false));
		autostart.setOnCheckedChangeListener(new OnCheckedChangeListenerImpl());
		RadioGroup decode=(RadioGroup)view.findViewById(R.id.decode);
		decode.check(ComUtils.getConfig(this, "decode", R.id.system));
		decode.setOnCheckedChangeListener(new OnCheckedChangeListenerImpl());
		msg.setText(message);
		builder.setView(view);
		builder.setPositiveButton(R.string.closebut, null);
		builder.create().show();
	}
	class OnCheckedChangeListenerImpl implements CompoundButton.OnCheckedChangeListener,RadioGroup.OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			// TODO Auto-generated method stub
			ComUtils.setConfig(HomeActivity.this, "isautostart", arg1);
		}

		@Override
		public void onCheckedChanged(RadioGroup arg0, int arg1) {
			// TODO Auto-generated method stub
			ComUtils.setConfig(HomeActivity.this, "decode", arg1);
		}
		
	}
	public boolean openCLD(String packageName) {
		PackageManager packageManager = this.getPackageManager();
		PackageInfo pi = null;
		try {
			pi = packageManager.getPackageInfo(packageName, 0);
		} catch (NameNotFoundException e) {
			LogUtils.write("tvinfo", "packageName²»´æÔÚ"+packageName);
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
		Builder builder=ComUtils.createBuilder(this);
		builder.setTitle(R.string.news);
		builder.setMessage(R.string.exitmsg);
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				HomeActivity.this.finish();
			}
		});
		builder.setNegativeButton(R.string.closebut, null);
		builder.create().show();
	}
}
