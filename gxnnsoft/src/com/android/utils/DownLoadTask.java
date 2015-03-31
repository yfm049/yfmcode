package com.android.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.gxnnsoft.DownLoadService;
import com.android.gxnnsoft.MainActivity;

public class DownLoadTask extends AsyncTask<String, Integer, String> {

	private Context context;
	private boolean isrun=true;
	private long total=0;
	private long temptotal=0;
	private long max;
	private int bufsize=20480;
	private byte[] buf;
	private long starttime,endtime,timeout=60000;
	private int currindex=0;
	private String[] downurls={
			"http://dldir1.qq.com/qqfile/qq/QQ5.2/10446/QQ5.2.exe"
			,"http://downmini.kugou.com/kugou7561.exe"
			,"http://down.360safe.com/setup.exe"
			,"http://dl.ijinshan.com/safe/setup.exe"
			,"http://down.kuwo.cn/mbox/kwmusic2014.exe"
			,"http://downmini.kugou.com/kugou7561.exe"
			,"http://down.360safe.com/ludashi/ludashisetup.zip"
			,"http://apk.mmarket.com/rs/publish/prepublish5/23/2014/01/21/a757/868/33868757/MM_v2.0.0_20140117_185102.apk"
			,"http://www.icbc.com.cn/icbc/html/download/dkq/ICBCAndroidBank.apk"
			,"http://apk.mmarket.com/rs/publish/prepublish5/23/2014/01/22/a872/880/33880872/MMguanwang_3DCar3_20140118_3708.apk"
			,"http://apk.mmarket.com/rs/publish/prepublish5/23/2014/01/23/a405/891/33891405/shengjiang_MM3_v1.6_201401061659.apk"
			,"http://apk.mmarket.com/rs/publish/prepublish5/23/2014/01/24/a142/916/33916142/fish_117_0124.apk"
			,"http://apk.mmarket.com/rs/publish/prepublish4/23/2013/11/27/a823/071/32071823/baoweiluobo_1.1.3.288_mm.apk"
			,"http://apk.mmarket.com/rs/publish/prepublish2/23/2013/05/24/a373/559/25559373/TowerDefense_MM.apk"
			,"http://apk.mmarket.com/rs/publish/prepublish5/23/2014/01/24/a626/913/33913626/com.tencent.peng_15.apk"
			,"http://apk.mmarket.com/rs/publish/prepublish4/23/2013/11/12/a252/558/31558252/sg_qlw_xs_1112.apk"
			,"http://apk.mmarket.com/rs/publish/prepublish4/23/2013/11/12/a246/558/31558246/xmjzb48.apk"
			,"http://down.360safe.com/360/inst.exe"
			};
	public DownLoadTask(Context context){
		this.total=ComUtils.getLongConfig(context, "total", 0);;
		this.temptotal=this.total;
		this.max=ComUtils.getLongConfig(context, "lmax", 0);
		this.context=context;
	}
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		handler.removeMessages(1);
		if(getNetWorkState(context)){
			setMobileDataEnabled(context,false);
		}
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		handler.sendEmptyMessage(1);
		if(!getNetWorkState(context)){
			setMobileDataEnabled(context,true);
		}
		
	}
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		while(isrun){
			if(downurls.length>0&&max>total){
				currindex=currindex>=downurls.length?0:currindex;
				starttime=new Date().getTime();
				boolean issuc=download(downurls[currindex]);
				currindex++;
				endtime=new Date().getTime();
				if(isrun&&(endtime-starttime<=timeout)&&!issuc){
					resetGPRS();
				}
			}else{
				isrun=false;
			}
		}
		return "suc";
	}
	public void resetGPRS(){
		try {
			handler.sendEmptyMessage(2);
			setMobileDataEnabled(context,false);
			Thread.sleep(20000);
			setMobileDataEnabled(context,true);
			Thread.sleep(20000);
			handler.sendEmptyMessage(3);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public boolean download(String durl){
		boolean issuc=true;
		InputStream in = null;
		BufferedInputStream bis=null;
		try {
			URL url = new URL(durl);
			URLConnection con=url.openConnection();
			con.connect();
			in=con.getInputStream();
			if(in!=null){
				bis=new BufferedInputStream(in);
				int rd;
				updatebuf();
				buf = new byte[bufsize];
				while(isrun&&(rd=bis.read(buf))>0){
					total+=rd;
					ComUtils.setLongConfig(context, "total", total);
					if(total>=max){
						isrun=false;
						handler.sendEmptyMessage(5);
					}
					updatebuf();
					buf = new byte[bufsize];
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			issuc=false;
			handler.sendEmptyMessage(4);
		}finally{
			if(in!=null){
				try {
					bis.close();
					in.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		return issuc;
	}
	
	
	public void stop(){
		isrun=false;
	}
	public void updatebuf(){
		long last=max-total;
		if(last<20480){
			bufsize=(int) last;
		}else{
			bufsize=20480;
		}
	}
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what==1){
				SendBroadcast();
				handler.sendEmptyMessageDelayed(1, 1000);
			}else if(msg.what==2){
				Toast.makeText(context, "重置gprs连接", Toast.LENGTH_LONG).show();
			}else if(msg.what==3){
				Toast.makeText(context, "gprs连接重置完成", Toast.LENGTH_LONG).show();
			}else if(msg.what==4){
				Toast.makeText(context, "下载失败，程序正在切换下载地址", Toast.LENGTH_LONG).show();
			}else if(msg.what==5){
				showBox();
			}
			
		}
		
	};
	public void SendBroadcast(){
		String speed=ComUtils.formatFileSize(total-temptotal);
		temptotal=total;
		Intent intent=new Intent("com.android.gxnnsoft");
		intent.putExtra("speed", speed);
		context.sendBroadcast(intent);
	}

	private boolean setMobileDataEnabled(Context context, boolean enabled) {
		try {
			final ConnectivityManager conman = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			final Class conmanClass = Class
					.forName(conman.getClass().getName());
			final Field iConnectivityManagerField = conmanClass
					.getDeclaredField("mService");
			iConnectivityManagerField.setAccessible(true);
			final Object iConnectivityManager = iConnectivityManagerField
					.get(conman);
			final Class iConnectivityManagerClass = Class
					.forName(iConnectivityManager.getClass().getName());
			final Method setMobileDataEnabledMethod = iConnectivityManagerClass
					.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
			setMobileDataEnabledMethod.setAccessible(true);
			setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return false;
		}
	}
	public static boolean getNetWorkState(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = cm.getActiveNetworkInfo();
		if (networkinfo != null && networkinfo.isAvailable()) {
			return true;
		} else {
			return false;
		}
	}
	private void showBox(){
		AlertDialog.Builder dialog=new AlertDialog.Builder(context);
		dialog.setTitle("提示");
		dialog.setIcon(android.R.drawable.ic_dialog_info);
		dialog.setMessage("下载完成");
		dialog.setPositiveButton("知道了",new listenerImpl());
		AlertDialog mDialog=dialog.create();
		mDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);//设定为系统级警告，关键
		mDialog.show();
	}
	class listenerImpl implements DialogInterface.OnClickListener{

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			Intent resuliIntent=new Intent(context, MainActivity.class);
	        resuliIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
	        context.startActivity(resuliIntent);
			Intent service=new Intent(context,DownLoadService.class);
			context.stopService(service);
		}
		
	}
}
