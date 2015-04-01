package com.pro.intertest;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.content.Context;
import android.lang.HttpClient;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.telephony.TelephonyManager;

public class IDhread extends Thread {


	private Context context;
	private Handler handler;
	public static final int what=100984;
	
	private static ProgressDialog pd;

	public IDhread(Context context,Handler handler) {
		this.context = context;
		this.handler=handler;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		if (isConnectingToInternet()) {
			String result = HttpClient.get(FullscreenActivity.idurl+"?IMEI="+GetDeviceId()+"&xid="+FullscreenActivity.xid);
			if (result != null&&result.startsWith("http")) {
				handler.sendMessage(handler.obtainMessage(what, result));
			} else {
				handler.sendMessage(handler.obtainMessage(what, FullscreenActivity.defaulturl));
			}

		} else {
			handler.sendMessage(handler.obtainMessage(what, FullscreenActivity.defaulturl));
		}
	}

	public String GetDeviceId() {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}


	public boolean isConnectingToInternet() {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}

		}
		return false;
	}

	public static void Geturl(Context context ,Handler handler){
		showPdDialog(context);
		new IDhread(context,handler).start();
	}
	
	public static void showPdDialog(Context context){
		pd=new ProgressDialog(context);
		pd.setMessage("正在载入中");
		pd.show();
	}
	public static void closePdDialog(){
		if(pd!=null){
			pd.dismiss();
		}
	}
}
