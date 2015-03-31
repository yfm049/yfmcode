package com.android.gxnnsoft;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.utils.ComUtils;
import com.android.utils.DownLoadTask;

public class DownLoadService extends Service {

	private DownLoadTask dt;
	private Notification notification;
	private AlarmManager am;
	private PendingIntent startoperation,stopoperation,defalueoperation;
	private long total;
	private long max;
	private SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	private Receiver receiver;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		startForeground();
		receiver=new Receiver();
		IntentFilter filter=new IntentFilter();
		filter.addAction("com.android.gxnnsoft.start");
		filter.addAction("com.android.gxnnsoft.stop");
		this.registerReceiver(receiver, filter);
		am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
		defalueoperation=createoperation("com.android.gxnnsoft.default");
		am.setRepeating(AlarmManager.RTC_WAKEUP, 0, 1000, defalueoperation);
		MainActivity.type="start";
		SendBroadcast();
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		total=ComUtils.getLongConfig(this, "total", 0);
		max=ComUtils.getLongConfig(this, "lmax", 0);
		if(max>total){
			String type=ComUtils.getStringConfig(this, "type","noauto");
			if("auto".equals(type)){
				String starttime=ComUtils.getStringConfig(this, "starttime", sDateFormat.format(new Date()));
				String endtime=ComUtils.getStringConfig(this, "endtime",sDateFormat.format(new Date()));
				startoperation=createoperation("com.android.gxnnsoft.start");
				stopoperation=createoperation("com.android.gxnnsoft.stop");
				am.set(AlarmManager.RTC_WAKEUP, gettime(starttime), startoperation);
				am.set(AlarmManager.RTC_WAKEUP, gettime(endtime), stopoperation);
			}else if("repeat".equals(type)){
				String starttime=ComUtils.getStringConfig(this, "starttime", sDateFormat.format(new Date()));
				String endtime=ComUtils.getStringConfig(this, "endtime",sDateFormat.format(new Date()));
				startoperation=createoperation("com.android.gxnnsoft.start");
				stopoperation=createoperation("com.android.gxnnsoft.stop");
				am.setRepeating(AlarmManager.RTC_WAKEUP, ComUtils.getDate(starttime).getTime(),24*60*60*1000, startoperation);
				am.setRepeating(AlarmManager.RTC_WAKEUP, ComUtils.getDate(endtime).getTime(),24*60*60*1000,stopoperation);
			}else{
				dt=new DownLoadTask(this);
				dt.execute("");
			}
		}
		return START_STICKY;
	}
	public PendingIntent createoperation(String action){
		Intent intent=new Intent(action);
		return PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}
	
	public void SendBroadcast(){
		Intent intent=new Intent("com.android.gxnnsoft.butstate");
		this.sendBroadcast(intent);
	}
	public long gettime(String time){
		try {
			return sDateFormat.parse(time).getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		stopForeground(true);
		this.unregisterReceiver(receiver);
		if(startoperation!=null){
			am.cancel(startoperation);
		}
		if(stopoperation!=null){
			am.cancel(stopoperation);
		}
		if(dt!=null){
			dt.stop();
		}
		MainActivity.type="stop";
		SendBroadcast();
		
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	public void startForeground(){
		NotificationCompat.Builder mBuilder=new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.ic_launcher)
        .setContentTitle("后台运行")
        .setContentText("程序后台运行中");
        Intent resuliIntent=new Intent(this, MainActivity.class);
        resuliIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent resultPendingIntent=PendingIntent.getActivity(this, 0, resuliIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        notification=mBuilder.build();
        startForeground(1,notification);

	}
	class Receiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action=intent.getAction();
			if("com.android.gxnnsoft.start".equals(action)){
				Log.i("tag", "启动下载");
				MainActivity.type="start";
				dt=new DownLoadTask(DownLoadService.this);
				dt.execute("");
			}else if("com.android.gxnnsoft.stop".equals(action)){
				if(dt!=null){
					dt.stop();
					dt.SendBroadcast();
				}
			}else if("com.android.gxnnsoft.default".equals(action)){
				
			}
			
		}
		
	}

}
