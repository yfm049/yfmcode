package com.iptv.thread;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.iptv.utils.HttpUtils;

public class ShouCangThread extends Thread {

	private Handler handler;
	private String url;
	private HttpUtils hu;
	public ShouCangThread(Handler handler, String url) {
		this.handler = handler;
		this.url = url;
		hu = new HttpUtils(2000,2000);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		String xml = hu.doget(HttpUtils.baseurl+url);
		Log.i("tvinfo", xml+"");
		Message msg = handler.obtainMessage();
		msg.what = 100;
		if(xml!=null){
			if(Boolean.parseBoolean(xml)){
				msg.arg1=1;// ’≤ÿ≥…π¶;
			}else{
				msg.arg1=2;// ’≤ÿ ß∞‹;
			}
		}else{
			msg.arg1=3;//Õ¯¬Á ß∞‹
		}
		handler.sendMessage(msg);
	}

}
