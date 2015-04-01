package com.iptv.thread;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.iptv.App.IptvApp;
import com.iptv.pojo.PrgItem;
import com.iptv.utils.ComUtils;
import com.iptv.utils.DomUtils;
import com.iptv.utils.LogUtils;

public class ProFilmThread extends Thread {

	public static int prgitemsuc=60,prgitemfail=61;
	private Handler handler;
	private String cid;
	private String rq;
	private String name;
	private String pass;
	
	private boolean isrun=true;
	public boolean isIsrun() {
		return isrun;
	}


	public void setIsrun(boolean isrun) {
		this.isrun = isrun;
	}


	public ProFilmThread(Context context,Handler handler, String cid,String rq) {
		this.handler = handler;
		this.cid = cid;
		this.rq=rq;
		name=ComUtils.getConfig(context, "name", "");
		pass=ComUtils.getConfig(context, "pass", "");
	}
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		Message msg=handler.obtainMessage();
		LogUtils.write("myplay", cid+" "+rq);
		String xml=IptvApp.play.getplaybackdata(name, pass, cid, rq);
		LogUtils.write("myplay", xml+" ");
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
