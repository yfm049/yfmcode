package com.pro.phonemessage;

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
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.pro.utils.ComUtils;
import com.pro.utils.DownExcelTask;
import com.pro.view.RingLayout;

public class PhoneService extends Service {

	

	

	private PhoneReceiver receiver;
	private DownExcelTask dtask;
	private String[] params;
	private TelephonyManager manager;
	private PhoneStateListenerImpl listener;
	private WindowManager wm;
	private View tv;
	private DisplayMetrics dm;
	private RingLayout rl;
	private Notification notification;
	private AlarmManager am;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		params=new String[]{"tel_download_text_"+ComUtils.getImei(this)+".txt","tel_query_xx_"+ComUtils.getImei(this)+".txt"};
		startForeground();
		dm=this.getResources().getDisplayMetrics();
		wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		receiver=new PhoneReceiver();
		IntentFilter filter=new IntentFilter();
		filter.addAction("com.pro.phonemessage.download");
		filter.addAction("com.pro.phonemessage.closewindow");
		this.registerReceiver(receiver, filter);
		manager = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
		listener=new PhoneStateListenerImpl();
		manager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
		Intent intent=new Intent("com.pro.phonemessage.download");
		am.setRepeating(AlarmManager.RTC_WAKEUP, 0, 1000*60*60, PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT));
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		closewindow();
		this.unregisterReceiver(receiver);
		stopForeground(true);
	}
	class PhoneStateListenerImpl extends PhoneStateListener{

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// TODO Auto-generated method stub
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:  
                //" 手机空闲起来了  "; 
				closewindow();
                break;  
            case TelephonyManager.CALL_STATE_RINGING:  
            	showwindow(incomingNumber);
                //"  手机铃声响了，来电号码:"+incomingNumber;  
                break;  
            case TelephonyManager.CALL_STATE_OFFHOOK:  
            	
                //" 电话被挂起了 ";  
            default:  
                break;  
            }  

		}
		
	}
	public void showwindow(String pn){
		closewindow();
		WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		params.type = WindowManager.LayoutParams.TYPE_PHONE;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE; 
		params.gravity=Gravity.TOP;
		params.width = dm.widthPixels;  
        params.height = dm.heightPixels/2;
        tv= LayoutInflater.from(this).inflate(R.layout.activity_ring, null);
        rl=(RingLayout)tv.findViewById(R.id.ring);
        rl.SetPhone(pn);
        wm.addView(tv, params);
	}
	public void closewindow(){
		if(tv!=null){
			wm.removeView(tv);
			tv=null;
		}
		
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
	class PhoneReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action=intent.getAction();
			if("com.pro.phonemessage.download".equals(action)){
				if(dtask!=null){
					dtask.cancel(true);
				}
				dtask=new DownExcelTask(PhoneService.this);
				dtask.execute(params);
			}
			if("com.pro.phonemessage.closewindow".equals(action)){
				closewindow();
			}
			
		}
		
	}
}
