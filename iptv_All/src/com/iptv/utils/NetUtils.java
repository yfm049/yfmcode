package com.iptv.utils;

import java.io.File;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;

import com.iptv.pojo.Channel;
import com.iptv.thread.AppDownloadThreadListener;
import com.iptv.thread.AppUpdateThread;
import com.iptv.thread.BackChannelThread;
import com.iptv.thread.DownLoadThread;
import com.iptv.thread.LoginThread;
import com.iptv.thread.LogoDownloadThreadListener;
import com.iptv.thread.LogoThread;
import com.iptv.thread.NoticeThread;
import com.iptv.thread.ProFilmThread;
import com.iptv.thread.ShouCangThread;
import com.iptv.thread.TimeThread;

public class NetUtils {

	public static void showDialog(ProgressDialog pd, String title,
			String message) {
		pd.setTitle(title);
		pd.setMessage(message);
		pd.setCancelable(false);
		pd.show();
	}

	public static void clolseDialog(ProgressDialog pd) {
		if (pd != null) {
			pd.dismiss();
		}
	}

	public static void login(String name, String pass,
			Handler handler, SqliteUtils su) {
		new LoginThread(name,ComUtils.md5(pass, null), handler, su).start();
	}

	private static NoticeThread noticethread;

	public static void GetNotice(Context context,Channel channel, Handler handler) {
		if (noticethread != null) {
			noticethread.setIsrun(false);
		}
		noticethread = new NoticeThread(context,channel.getEpg(), handler);
		noticethread.start();
	}

	public static void ShouCang(Channel channel, Context context,
			Handler handler) {
		new ShouCangThread(handler, context, channel).start();
	}
	
	public static void GetBackChannelData(Context context,Handler handler){
		new BackChannelThread(context,handler).start();
	}
	private static ProFilmThread profilmthread;
	public static void GetBackProData(Context context,String cid,String rq,Handler handler){
		if(profilmthread!=null){
			profilmthread.setIsrun(false);
		}
		profilmthread=new ProFilmThread(context,handler,cid,rq);
		profilmthread.start();
	}
	
	private static TimeThread timeupdate = null;

	public static void TimeUpdate(Handler handler) {
		if (timeupdate != null) {
			timeupdate.setIsrun(false);
		}
		timeupdate = new TimeThread(handler);
		timeupdate.start();
	}
	public static void LogoShow(Handler handler) {
		LogoThread logotread = new LogoThread(handler);
		logotread.start();
	}

	public static void Downlogo(Handler handler) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File file = new File(Environment.getExternalStorageDirectory()
					.getAbsoluteFile() + "/iptv/logo");
			if (!file.exists()) {
				file.mkdirs();
			}
			File logo = new File(file, "logo.zip");
			if (logo.exists()) {
				logo.delete();
			}
			DownLoadThread dt = new DownLoadThread(HttpClientHelper.baseurl
					+ "logo/logo.zip", logo, new LogoDownloadThreadListener(
					handler));
			dt.start();
		}
	}

	public static void appCheckUpdate(Handler handler) {
		AppUpdateThread thread = new AppUpdateThread(handler);
		thread.start();
	}

	public static void appDownLoad(String url, Handler handler) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File file = new File(Environment.getExternalStorageDirectory()
					.getAbsoluteFile() + "/iptv/appupdate");
			if (!file.exists()) {
				file.mkdirs();
			}
			File app = new File(file, "appupdte.apk");
			if (app.exists()) {
				app.delete();
			}
			DownLoadThread dt = new DownLoadThread(url, app,
					new AppDownloadThreadListener(handler));
			dt.start();
		}
	}
}
