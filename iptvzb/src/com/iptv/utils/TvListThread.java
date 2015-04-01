package com.iptv.utils;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class TvListThread extends Thread {

	private Handler handler;
	private String url;
	private HttpUtils hu;

	public TvListThread(Handler handler, String url) {
		this.handler = handler;
		this.url = url;
		hu = new HttpUtils();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		handler.sendEmptyMessage(9);
		List<TvInfo> tvlist =null;
		tvlist = hu.gettvList(url);
		Message msg = handler.obtainMessage();
		msg.obj = tvlist;
		msg.what = 10;
		handler.sendMessage(msg);
	}

}
