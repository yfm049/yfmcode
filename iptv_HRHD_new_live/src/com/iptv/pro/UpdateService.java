package com.iptv.pro;

import java.io.File;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.WindowManager;
import android.widget.Toast;

import com.iptv.pojo.Appinfo;
import com.iptv.thread.AppDownloadThreadListener;
import com.iptv.thread.AppUpdateThread;
import com.iptv.thread.LogoDownloadThreadListener;
import com.iptv.utils.ComUtils;
import com.iptv.utils.LogUtils;
import com.iptv.utils.NetUtils;

public class UpdateService extends Service {

	private static String TAG=UpdateService.class.getName();
	public String path="/iptv";
	public static int appupdate=800,logodown=801;
	public static String logoupdate="com.iptv.logoupdate";
	private AlertDialog dialog;
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		if(!LogUtils.isdebug){
			NetUtils.appCheckUpdate(handler);
		}
		NetUtils.Downlogo(handler);
		return START_NOT_STICKY;
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == AppUpdateThread.appchecksuc) {
				Appinfo info = (Appinfo) msg.obj;
				int appversion=ComUtils.getversioncode(UpdateService.this);
				if (info.getVercode() >appversion ) {
					show(info);
				} 

			} else if (msg.what == AppDownloadThreadListener.appdownloadsuc && msg.obj != null) {
				LogUtils.write(TAG, "下载完成");
				showinstall((File) msg.obj);
			}else if(msg.what == AppDownloadThreadListener.appdownloadfail){
				Toast.makeText(UpdateService.this, "下载失败", Toast.LENGTH_SHORT).show();
			}else if(msg.what==LogoDownloadThreadListener.downloadsuc){
				Intent intent=new Intent(logoupdate);
				UpdateService.this.sendBroadcast(intent);
			}else if(msg.what==LogoDownloadThreadListener.downloadfail){
				Toast.makeText(UpdateService.this, "频道图标下载失败", Toast.LENGTH_SHORT).show();
			}
		}

	};

	private void show(final Appinfo info) {
		if(dialog!=null&&dialog.isShowing()){
			dialog.dismiss();
		}
		Builder builder = new Builder(UpdateService.this);
		builder.setTitle("更新");
		builder.setMessage(info.getMsg());
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				NetUtils.appDownLoad(info.getUrl(), handler);
				Toast.makeText(UpdateService.this, "开始下载", Toast.LENGTH_SHORT).show();
			}
		});
		builder.setNegativeButton("取消", null);
		dialog = builder.create();
		dialog.getWindow()
				.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		dialog.show();
	}


	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}


	public void showinstall(final File file) {
		Builder builder = new Builder(UpdateService.this);
		builder.setTitle("安装");
		builder.setMessage("下载完成,是否安装...");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.fromFile(file),
						"application/vnd.android.package-archive");
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);

			}
		});
		builder.setNegativeButton("取消", null);
		AlertDialog dialog = builder.create();
		dialog.getWindow()
				.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		dialog.show();
	}

}
