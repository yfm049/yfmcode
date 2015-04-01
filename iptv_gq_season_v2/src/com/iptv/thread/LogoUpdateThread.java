package com.iptv.thread;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.iptv.pojo.Logoinfo;
import com.iptv.utils.HttpUtils;


public class LogoUpdateThread extends Thread{

	private Handler handler;
	private String url;
	private HttpUtils hu;
	public LogoUpdateThread(HttpUtils hu,Handler handler,String url){
		this.handler=handler;
		this.url=url;
		this.hu=hu;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		try {
			Message msg=handler.obtainMessage();
			Log.i("tvinfo", url);
			Logoinfo info=hu.getremotelogo(url);
			msg.what=3;
			msg.obj=info;
			handler.sendMessage(msg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			close();
		}
	}

	public void close(){
		hu.close();
	}
	
}
