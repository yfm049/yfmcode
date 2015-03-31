package com.pro.appinfo;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import com.pro.model.Appinfo;
import com.pro.net.HttpUtils;
import com.pro.utils.Utils;
//软件后天更新服务
public class UpdateService extends Service {

	private PackageManager pm;
	private AlarmManager am;
	//软件每天检查一次更新 毫秒*秒*分*时
	public static long tosend=1000*60*60*24;
	public static List<Appinfo> fl = new ArrayList<Appinfo>();
	private List<NameValuePair> lnp = new ArrayList<NameValuePair>();
	private String uri = "app!getupdate.action";
	private NotificationManager nm;
	private Notification noti;
	private JSONArray ja;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		this.registerReceiver(new AlarmReceiver(), new IntentFilter("com.pro.appinfo"));
		nm=(NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
		am=(AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
		pm = this.getPackageManager();
		long triggerAtTime = SystemClock.elapsedRealtime();
        Intent send=new Intent("com.pro.appinfo");
        PendingIntent pi=PendingIntent.getBroadcast(this, 0, send, PendingIntent.FLAG_UPDATE_CURRENT);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, tosend, pi);
		//定时器 每天发送一次广播 更新软件
	}
	//广播接收器 接收到广播后 开始更新
	class AlarmReceiver extends BroadcastReceiver {  
	    @Override  
	    public void onReceive(Context context, Intent intent) {  
	    	String action=intent.getAction();
	    	Log.i("com.pro.appinfo", action);
	    	//开始检查更新
	    	if(action!=null&&"com.pro.appinfo".equals(action)){
	    		getDate();
	    	}
	    	
	    }  
	}
	//软件更新方法
	private void getDate() {
		new GetDate().start();
	}
	//软件界面更新
	private Handler hanlder=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(fl.size()>0){
				Intent intent = new Intent(UpdateService.this, UpdateListActivity.class);
				PendingIntent pi = PendingIntent.getActivity(UpdateService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
				
				noti=new Notification(R.drawable.ic_launcher, "软件更新提示", System.currentTimeMillis());
				noti.flags=Notification.FLAG_AUTO_CANCEL;
				noti.setLatestEventInfo(UpdateService.this, "软件更新", "有"+fl.size()+"软件需要更新", pi);
				nm.notify(0, noti);
			}
		}
		
	};
	//获取数据线程
	class GetDate extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			lnp.clear();
			//获取已安装软件的更新
			GetAllInstalled();
			lnp.add(new BasicNameValuePair("name",ja.toString()));
			String html = HttpUtils.PostCon(uri, lnp);
			if (html != null) {
				List<Appinfo> lf = Utils.getallApp(html);
				fl.clear();
				fl.addAll(lf);
			} else {
				fl.clear();
			}
			hanlder.sendEmptyMessage(1);
		}

	}
	//获取已安装的软件
	private void GetAllInstalled(){
		ja=new JSONArray();
		List<PackageInfo> lpi=pm.getInstalledPackages(0);
		if(lpi!=null&&lpi.size()>0){
			for(PackageInfo pi:lpi){
				if((pi.applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)<=0){
					//已安装程序
					try {
						JSONObject jo=new JSONObject();
						jo.put("pkname", pi.packageName);
						jo.put("vs", pi.versionCode);
						ja.put(jo);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
