package com.iptv.thread;

import com.iptv.utils.HttpUtils;

import android.content.Context;

public class UrlThread extends Thread {

	private String urll;
	private Context context;
	private HttpUtils hu;
	public UrlThread(Context context,String url){
		this.urll=url;
		this.context=context;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		hu = new HttpUtils(context);
		String loginurl=hu.doget(HttpUtils.baseurl+urll);
		if(loginurl!=null&&!"".equals(loginurl)){
			hu.doget(loginurl);
		}
	}

}
