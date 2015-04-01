package com.iptv.thread;

import android.os.Handler;
import android.os.Message;

import com.iptv.utils.DomUtils;
import com.iptv.utils.HttpClientHelper;

public class GongGaoThread extends Thread {

	public static int gonggaosuc=31,gonggaofail=32;
	private Handler handler;
	public GongGaoThread(Handler handler){
		this.handler=handler;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		String xml=HttpClientHelper.DoGet(HttpClientHelper.baseurl+"/notify.xml");
		if(xml!=null){
			String gg=DomUtils.ParseGongGaoXml(xml);
			Message msg=handler.obtainMessage();
			msg.what=gonggaosuc;
			msg.obj=gg;
			handler.sendMessage(msg);
		}
		
		
	}

}
