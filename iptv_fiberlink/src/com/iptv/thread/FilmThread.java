package com.iptv.thread;

import java.util.List;

import android.os.Handler;
import android.os.Message;

import com.iptv.pojo.Film;
import com.iptv.utils.HttpUtils;

public class FilmThread extends Thread {

	private Handler handler;
	private String url;
	private HttpUtils hu;

	public FilmThread(Handler handler, String url) {
		this.handler = handler;
		this.url = url;
		hu = new HttpUtils();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		handler.sendEmptyMessage(9);
		String xml=hu.doget(HttpUtils.baseurl+url);
		Message msg = handler.obtainMessage();
		if(xml!=null){
			 msg.what = 1;
			 List<Film> lf=hu.getfilmlist(xml);
			 msg.obj=lf;
		}else{
			msg.what = 100;
		}
		handler.sendMessage(msg);
	}

}
