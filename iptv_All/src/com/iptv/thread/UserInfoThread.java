package com.iptv.thread;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.iptv.App.IptvApp;
import com.iptv.utils.ComUtils;
import com.iptv.utils.DomUtils;
import com.iptv.utils.HttpClientHelper;

public class UserInfoThread extends Thread {

	public static int userinfosuc=20,userinfofail=21;
	private Handler handler;
	private String uid;
	public UserInfoThread(Handler handler, String uid) {
		this.handler = handler;
		this.uid = uid;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		String xml = IptvApp.play.getuserdate(uid);
		xml = ComUtils.Base64decode(xml);
		xml = DomUtils.getUserInfo(xml);
		Message msg = handler.obtainMessage();
		if (xml != null) {
			msg.what = userinfosuc;
			msg.obj = xml;
		} else {
			msg.what = userinfofail;// ÍøÂçÊ§°Ü
		}
		handler.sendMessage(msg);
	}

}
