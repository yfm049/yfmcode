package com.iptv.thread;

import java.util.List;

import android.os.Handler;
import android.os.Message;

import com.iptv.pojo.PrgItem;
import com.iptv.utils.ComUtils;
import com.iptv.utils.DomUtils;
import com.iptv.utils.HttpClientHelper;
import com.iptv.utils.LogUtils;

public class ProFilmThread extends Thread {

	public static int prgitemsuc=60,prgitemfail=61;
	private Handler handler;
	private String url;
	private boolean isrun=true;
	public boolean isIsrun() {
		return isrun;
	}


	public void setIsrun(boolean isrun) {
		this.isrun = isrun;
	}


	public ProFilmThread(Handler handler, String url) {
		this.handler = handler;
		this.url = url;
	}
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		Message msg=handler.obtainMessage();
		LogUtils.write("tvinfo", url+"");
		String xml=HttpClientHelper.DoGet(url);
		if(xml!=null){
			xml=ComUtils.Base64decode(xml);
			List<PrgItem> listpro=DomUtils.ParsePrgItem(xml);
			LogUtils.write("tvinfo", listpro.size()+"");
			msg.what=prgitemsuc;
			msg.obj=listpro;
		}else{
			msg.what=prgitemfail;
		}
		if(isrun){
			handler.sendMessage(msg);
		}
		isrun=false;
	}

}
