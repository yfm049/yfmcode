package com.iptv.thread;

import java.util.List;

import com.iptv.pojo.TvInfo;
import com.iptv.utils.HttpUtils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class TvListThread extends Thread {

	private Handler handler;
	private String url;
	private HttpUtils hu;
	private int type;

	public TvListThread(Handler handler, String url,int type) {
		this.handler = handler;
		this.url = url;
		hu = new HttpUtils();
		this.type=type;
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
		msg.arg1=type;
		handler.sendMessage(msg);
	}

}
