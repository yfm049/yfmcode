package com.iptv.thread;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.iptv.pojo.TvInfo;
import com.iptv.utils.HttpUtils;

public class PlayThread extends Thread {

	private Handler handler;
	private HttpUtils hu;
	private boolean isqh = true;
	private TvInfo tvinfo;
	private String userid;
	private String hotlink;
	private Context context;

	public PlayThread(Context context, Handler handler, String userid,
			TvInfo tvinfo) {
		this.handler = handler;
		this.tvinfo = tvinfo;
		this.userid = userid;
		this.context = context;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		try {
			sendmsg(1, "正在切换至 ");
			Thread.sleep(200);
			if (!isqh) {
				return;
			}
			hu = new HttpUtils(context);
			if (tvinfo.getHttpurl().startsWith("http")) {
				hu.doget(tvinfo.stopsoplay());
				sendmsg(3, "开始播放 ");
			} else {
				hu = new HttpUtils(context);
				String xml = hu.doget(tvinfo.getparam(userid));
				Log.i("tvinfo", xml);
				if (xml != null && isqh) {
					sendmsg(2, "开始加载");
					if (isqh) {
						sendmsg(3, "开始播放 ");
					} else {
						sendmsg(4, "加载失败 ");
					}
				} else {
					sendmsg(4, "切换失败 ");
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void close() {
		isqh = false;
	}

	public void sendmsg(int i, String message) {
		Message msg = handler.obtainMessage();
		msg.what = i;
		msg.obj = message;
		handler.sendMessage(msg);
	}

}
