package com.iptv.thread;

import java.util.List;

import android.os.Handler;
import android.os.Message;

import com.iptv.pojo.Film;
import com.iptv.pojo.PrgItem;
import com.iptv.utils.HttpUtils;

public class ProFilmThread extends Thread {

	private Handler handler;
	private String url;
	private HttpUtils hu;

	public ProFilmThread(Handler handler, String url) {
		this.handler = handler;
		this.url = url;
		hu = new HttpUtils();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		handler.sendEmptyMessage(9);
		List<PrgItem> lprg=hu.getPrg(url);
		Message msg=handler.obtainMessage();
		if(lprg!=null){
			 msg.what = 2;
			 msg.obj=lprg;
		}else{
			msg.what = 100;
		}
		handler.sendMessage(msg);
	}

}
