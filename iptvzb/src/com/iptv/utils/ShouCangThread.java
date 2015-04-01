package com.iptv.utils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ShouCangThread extends Thread {

	private Handler handler;
	private String url;
	private HttpUtils hu;

	public ShouCangThread(Handler handler, String url) {
		this.handler = handler;
		this.url = url;
		hu = new HttpUtils();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		String xml = hu.doget(HttpUtils.baseurl+url,2000,2000);
		Log.i("tvinfo", xml+"");
		Message msg = handler.obtainMessage();
		msg.obj = xml;
		msg.what = 100;
		handler.sendMessage(msg);
	}

}
