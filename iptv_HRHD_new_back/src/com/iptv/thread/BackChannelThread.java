package com.iptv.thread;

import java.util.List;

import android.os.Handler;
import android.os.Message;

import com.iptv.pojo.Channel;
import com.iptv.utils.ComUtils;
import com.iptv.utils.DomUtils;
import com.iptv.utils.HttpClientHelper;

public class BackChannelThread extends Thread {

	public static int backchannelsuc=30,backchannelfail=40;
	private Handler handler;

	public BackChannelThread(Handler handler) {
		this.handler = handler;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		String xml=HttpClientHelper.DoGet(HttpClientHelper.baseurl+"tv_list.jsp");
		Message msg = handler.obtainMessage();
		if(xml!=null){
			 msg.what = backchannelsuc;
			 xml=ComUtils.Base64decode(xml);
			 List<Channel> lf=DomUtils.ParseBackChannelXml(xml);
			 msg.obj=lf;
		}else{
			msg.what = backchannelfail;
		}
		handler.sendMessage(msg);
	}

}
