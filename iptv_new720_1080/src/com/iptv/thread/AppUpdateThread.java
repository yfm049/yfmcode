package com.iptv.thread;

import android.os.Handler;
import android.os.Message;

import com.iptv.pojo.Appinfo;
import com.iptv.utils.DomUtils;
import com.iptv.utils.HttpClientHelper;
import com.iptv.utils.LogUtils;


public class AppUpdateThread extends Thread{

	public static int appchecksuc=1000;
	private Handler handler;
	private String url;
	public AppUpdateThread(Handler handler,String url){
		this.handler=handler;
		this.url=url;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		try {
			LogUtils.write("tvinfo", url);
			String xml=HttpClientHelper.DoGet(HttpClientHelper.baseurl+url);
			if(xml!=null){
				Message msg=handler.obtainMessage();
				Appinfo info=DomUtils.parseRemoteapp(xml);
				msg.what=appchecksuc;
				msg.obj=info;
				handler.sendMessage(msg);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	
}
