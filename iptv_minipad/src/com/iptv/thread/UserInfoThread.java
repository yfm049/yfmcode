package com.iptv.thread;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.iptv.utils.HttpUtils;

public class UserInfoThread extends Thread {

	private Handler handler;
	private String url;
	private HttpUtils hu;
	private Context context;
	public UserInfoThread(Context context,Handler handler, String url) {
		this.handler = handler;
		this.url = url;
		this.context=context;
		hu = new HttpUtils(context);
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
			msg.arg1=1;
			msg.obj=hu.getUserInfo(xml);
		}else{
			msg.arg1=3;//ÍøÂçÊ§°Ü
		}
		handler.sendMessage(msg);
	}

}
