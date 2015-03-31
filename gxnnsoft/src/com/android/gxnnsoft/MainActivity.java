package com.android.gxnnsoft;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.utils.ComUtils;

public class MainActivity extends Activity {

	private RadioGroup startmode;
	private TextView totalliuliang, speed, msgbody, currtime;
	private EditText maxliuliang, starttime, endtime;
	private Button startbut, stopbut, clear, sendmsg, houtai, exit,phonebut;
	private listenerImpl listener;
	private Receiver receiver;
	public static String type = "stop";
	private SimpleDateFormat sDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat TimeFormat = new SimpleDateFormat("HH:mm:ss");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		listener = new listenerImpl();
		startmode = (RadioGroup) this.findViewById(R.id.startmode);
		totalliuliang = (TextView) this.findViewById(R.id.totalliuliang);
		speed = (TextView) this.findViewById(R.id.speed);
		msgbody = (TextView) this.findViewById(R.id.msgbody);
		currtime = (TextView) this.findViewById(R.id.currtime);
		
		
		maxliuliang = (EditText) this.findViewById(R.id.maxliuliang);
		starttime = (EditText) this.findViewById(R.id.starttime);
		endtime = (EditText) this.findViewById(R.id.endtime);
		startbut = (Button) this.findViewById(R.id.startbut);
		stopbut = (Button) this.findViewById(R.id.stopbut);
		clear = (Button) this.findViewById(R.id.clear);
		sendmsg = (Button) this.findViewById(R.id.sendmsg);
		houtai = (Button) this.findViewById(R.id.houtai);
		exit = (Button) this.findViewById(R.id.exit);

		phonebut = (Button) this.findViewById(R.id.phonebut);
		
		startmode.setOnCheckedChangeListener(listener);
		startbut.setOnClickListener(listener);
		stopbut.setOnClickListener(listener);
		clear.setOnClickListener(listener);
		sendmsg.setOnClickListener(listener);
		houtai.setOnClickListener(listener);
		exit.setOnClickListener(listener);
		phonebut.setOnClickListener(listener);
		
		long total = ComUtils.getLongConfig(MainActivity.this, "total", 0);
		totalliuliang.setText(ComUtils.formatFileSize(total));
		maxliuliang.setText(ComUtils.getStringConfig(this, "max", ""));
		receiver = new Receiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.android.gxnnsoft");
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		filter.addAction("com.android.gxnnsoft.butstate");

		this.registerReceiver(receiver, filter);
		handler.sendEmptyMessage(1);
	}

	public String getphonenum() {
		TelephonyManager tm = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);
		String phoneId = tm.getLine1Number();
		return phoneId;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		int checked = ComUtils.getIntConfig(this, "startmode", R.id.shoudong);
		startmode.check(checked);
		changemode(checked);
		updatebutstate();
		updatetime();

	}

	public void inittime() {
		starttime.setText(sDateFormat.format(ComUtils.getNextDate(1, 0, 5, 0)));
		endtime.setText(sDateFormat.format(ComUtils.getNextDate(1, 7, 50, 0)));
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		handler.removeMessages(1);
		this.unregisterReceiver(receiver);
	}

	private void changemode(int checked) {
		if (checked == R.id.shoudong) {
			starttime.setEnabled(false);
			endtime.setEnabled(false);
		} else if (checked == R.id.zidong) {
			inittime();
			starttime.setEnabled(true);
			endtime.setEnabled(true);
		} else if (checked == R.id.repeat) {
			starttime.setText(TimeFormat.format(ComUtils.getDate(0, 10, 0)));
			endtime.setText(TimeFormat.format(ComUtils.getDate(7, 50, 0)));
			starttime.setEnabled(true);
			endtime.setEnabled(true);
		}
	}

	class listenerImpl implements RadioGroup.OnCheckedChangeListener,
			OnClickListener {

		@Override
		public void onCheckedChanged(RadioGroup rg, int arg1) {
			// TODO Auto-generated method stub
			if (rg.getId() == R.id.startmode) {
				changemode(rg.getCheckedRadioButtonId());
				ComUtils.setIntConfig(MainActivity.this, "startmode",
						rg.getCheckedRadioButtonId());
			}
		}

		@Override
		public void onClick(View but) {
			// TODO Auto-generated method stub
			if (but.getId() == R.id.startbut) {
				startDown();
			} else if (but.getId() == R.id.stopbut) {
				stopDown();
			} else if (but.getId() == R.id.clear) {
				clear();
			} else if (but.getId() == R.id.sendmsg) {
				SendMsg("10086","103");
			} else if (but.getId() == R.id.houtai) {
				MainActivity.this.finish();
			} else if (but.getId() == R.id.exit) {
				stopDown();
				MainActivity.this.finish();
			}else if(but.getId()==R.id.phonebut){
				SendMsg("106981630163","A");
			}
		}

	}

	public void SendMsg(String p,String b) {
		SmsManager smsmanager = SmsManager.getDefault();
		smsmanager.sendTextMessage(p, null, b, null, null);
		Toast.makeText(this, "短信已经发送", Toast.LENGTH_LONG).show();

	}

	public void clear() {
		ComUtils.setLongConfig(this, "total", 0);
		totalliuliang.setText(ComUtils.formatFileSize(0));
		speed.setText("0.0B");
	}

	public void startDown() {
		String smax = maxliuliang.getText().toString();
		if (!"".equals(smax)) {
			Double dmax = Double.parseDouble(smax);
			long total = ComUtils.getLongConfig(this, "total", 0);
			long max = (long) (dmax * 1000 * 1000);
			if (max > total) {
				ComUtils.setStringConfig(this, "max", smax);
				if (startmode.getCheckedRadioButtonId() == R.id.zidong) {
					autostart(max);
				} else if (startmode.getCheckedRadioButtonId() == R.id.repeat) {
					repet(max);
				} else {
					ComUtils.setLongConfig(this, "lmax", max);
					ComUtils.setStringConfig(this, "type", "noauto");
					Intent service = new Intent(this, DownLoadService.class);
					this.startService(service);
					Toast.makeText(this, "已经启动下载", Toast.LENGTH_LONG).show();
					MainActivity.type = "run";
					updatebutstate();
				}
			} else {
				Toast.makeText(this, "你设置的下载流量值太小", Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(this, "你还没有设置最大下载流量", Toast.LENGTH_LONG).show();
		}

	}

	public void autostart(long max) {
		try {
			Date cdate = new Date();
			String stime = starttime.getText().toString();
			Date sdate = sDateFormat.parse(stime);
			String etime = endtime.getText().toString();
			Date edate = sDateFormat.parse(etime);

			if (cdate.before(sdate)) {
				if (edate.after(sdate)) {
					ComUtils.setLongConfig(this, "lmax", max);
					ComUtils.setStringConfig(this, "type", "auto");
					ComUtils.setStringConfig(this, "starttime", stime);
					ComUtils.setStringConfig(this, "endtime", etime);
					Intent service = new Intent(this, DownLoadService.class);
					this.startService(service);
					Toast.makeText(this, "自动模式启动", Toast.LENGTH_LONG).show();
					MainActivity.type = "run";
					updatebutstate();
				} else {
					Toast.makeText(this, "停止时间不能小于开始时间", Toast.LENGTH_LONG)
							.show();
				}
			} else {
				Toast.makeText(this, "设置时间小于当前时间", Toast.LENGTH_LONG).show();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(this, "设置时间格式不正确", Toast.LENGTH_LONG).show();
		}
	}

	public void repet(long max) {
		String stime = starttime.getText().toString();
		String etime = endtime.getText().toString();
		ComUtils.setLongConfig(this, "lmax", max);
		ComUtils.setStringConfig(this, "type", "repeat");
		ComUtils.setStringConfig(this, "starttime", stime);
		ComUtils.setStringConfig(this, "endtime", etime);
		Intent service = new Intent(this, DownLoadService.class);
		this.startService(service);
		Toast.makeText(this, "重复模式启动", Toast.LENGTH_LONG).show();
		MainActivity.type = "run";
		updatebutstate();
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == 1) {
				updatetime();
				handler.sendEmptyMessageDelayed(1, 1000);
			}
		}

	};

	public void updatetime() {
		currtime.setText(sDateFormat.format(new Date()));
	}

	public void updatebutstate() {
		if ("stop".equals(type)) {
			startbut.setEnabled(true);
			clear.setEnabled(true);
			stopbut.setEnabled(false);
		} else {
			stopbut.setEnabled(true);
			startbut.setEnabled(false);
			clear.setEnabled(false);
		}
	}

	public void stopDown() {
		Intent service = new Intent(this, DownLoadService.class);
		this.stopService(service);
		MainActivity.type = "stop";
		updatebutstate();
	}

	class Receiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			if ("com.android.gxnnsoft".equals(action)) {
				long total = ComUtils.getLongConfig(MainActivity.this, "total",0);
				totalliuliang.setText(ComUtils.formatFileSize(total));
				String sspeed = intent.getStringExtra("speed");
				speed.setText(sspeed);
				updatebutstate();
			} else if ("android.provider.Telephony.SMS_RECEIVED".equals(action)) {
				String msg = ComUtils.getMessagesFromIntent(intent);
				msgbody.setText(ComUtils.getliuliang(msg).toString());
			} else if ("com.android.gxnnsoft.butstate".equals(action)) {
				updatebutstate();
			}

		}

	}
}
