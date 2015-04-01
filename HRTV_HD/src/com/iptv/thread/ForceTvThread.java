package com.iptv.thread;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.iptv.pro.PlayTVActivity;
import com.iptv.utils.DomUtils;
import com.iptv.utils.ForceTvUtils;
import com.iptv.utils.HttpClientHelper;
import com.iptv.utils.LogUtils;

public class ForceTvThread extends Thread {

	private static String TAG=ForceTvThread.class.getName();
	public static int ForceTvsuc = 10, ForceTvfail = 20;
	private String url;
	private String playurl;
	private Handler handler;
	private boolean isrun = true;

	public ForceTvThread(String url, String playurl, Handler handler) {
		this.url = url;
		this.playurl = playurl;
		this.handler = handler;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		if (url.equals(ForceTvUtils.paramurl)) {
			LogUtils.write(TAG, "no switch_chan");
			Message msg = handler.obtainMessage();
			msg.obj = playurl;
			msg.what = ForceTvsuc;
			handler.sendMessage(msg);
		} else {
			String xml = HttpClientHelper.DoGet(url);
			if (isrun) {
				LogUtils.write(TAG, ""+xml);
				boolean flag = DomUtils.ParseForceTvXml(xml);
				Message msg = handler.obtainMessage();
				if (flag) {
					msg.obj = playurl;
					msg.what = ForceTvsuc;
					ForceTvUtils.paramurl=url;
				} else {
					msg.what = ForceTvfail;
				}
				handler.sendMessage(msg);
			}
		}

	}
	public void stopreq(){
		isrun=false;
	}

}
