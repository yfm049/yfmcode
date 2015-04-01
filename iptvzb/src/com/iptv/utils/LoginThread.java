package com.iptv.utils;

import android.os.Handler;
import android.os.Message;


public class LoginThread extends Thread{

	private Handler handler;
	private String url;
	private HttpUtils hu;
	public LoginThread(Handler handler,String url){
		this.handler=handler;
		this.url=url;
		hu=new HttpUtils();
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		try {
			handler.sendEmptyMessage(1);
			Thread.sleep(2000);
			String xml=hu.doget(HttpUtils.baseurl+url);
			User user=hu.parsexml(xml);
			Message msg=handler.obtainMessage();
			msg.obj=user;
			msg.what=2;
			handler.sendMessage(msg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}
