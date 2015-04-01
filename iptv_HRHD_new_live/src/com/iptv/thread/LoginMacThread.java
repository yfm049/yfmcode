package com.iptv.thread;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.iptv.pro.PlayTVActivity;
import com.iptv.utils.ComUtils;
import com.iptv.utils.DomUtils;
import com.iptv.utils.ForceTvUtils;
import com.iptv.utils.HttpClientHelper;
import com.iptv.utils.LogUtils;

public class LoginMacThread extends Thread {

	private static String TAG = LoginMacThread.class.getName();
	public static int loginmacsuc = 4;
	private Handler handler;

	private Context context;
	public LoginMacThread(Context context, Handler handler) {
		this.handler = handler;
		this.context=context;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		String mac=ComUtils.getLocalMacAddressFromIp(context);
		Message msg=handler.obtainMessage();
		msg.what=loginmacsuc;
		msg.obj=mac;
		handler.sendMessage(msg);
	}

}
